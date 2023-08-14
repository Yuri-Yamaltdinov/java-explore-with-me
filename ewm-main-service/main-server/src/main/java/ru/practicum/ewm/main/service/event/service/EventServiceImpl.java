package ru.practicum.ewm.main.service.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.main.service.event.dto.EventFullDto;
import ru.practicum.ewm.main.service.event.dto.EventShortDto;
import ru.practicum.ewm.main.service.event.dto.NewEventDto;
import ru.practicum.ewm.main.service.event.dto.UpdateEventUserRequest;
import ru.practicum.ewm.main.service.event.mapper.EventMapper;
import ru.practicum.ewm.main.service.event.model.Event;
import ru.practicum.ewm.main.service.event.repository.EventRepository;
import ru.practicum.ewm.main.service.exception.AccessException;
import ru.practicum.ewm.main.service.exception.EntityNotFoundException;
import ru.practicum.ewm.main.service.exception.ValidationException;
import ru.practicum.ewm.main.service.location.mapper.LocationMapper;
import ru.practicum.ewm.main.service.location.model.Location;
import ru.practicum.ewm.main.service.location.repository.LocationRepository;
import ru.practicum.ewm.main.service.user.model.User;
import ru.practicum.ewm.main.service.user.service.UserService;
import ru.practicum.ewm.main.service.util.Pagination;
import ru.practicum.ewm.stats.client.StatsClient;
import ru.practicum.ewm.stats.dto.ViewStatsResponseDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.ewm.main.service.event.model.State.PUBLISHED;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final StatsClient statsClient;

    private final UserService userService;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    @Override
    public EventFullDto create(Long userId, NewEventDto newEventDto) {
        User initiator = userService.getOrThrow(userId);
        checkEventDateOrThrow(newEventDto.getEventDate());
        Location location;
        try {
            location = locationRepository.save(locationMapper.locationFromDto(newEventDto.getLocation()));
        } catch (DataIntegrityViolationException e) {
            throw new ValidationException("Incorrect location values");
        }


        Event event = eventMapper.eventFromNewEventDto(newEventDto, initiator, location);
        Event savedEvent = eventRepository.save(event);
        Long views = 0L;
        return eventMapper.eventFullDtoFromEvent(savedEvent, views);
    }

    @Override
    public List<EventShortDto> getAll(Long userId, Integer from, Integer size) {
        User user = userService.getOrThrow(userId);
        Pagination page = new Pagination(from, size);
        List<Event> events = eventRepository.findAllByInitiator(user, page);
        List<EventShortDto> result = new ArrayList<>();
        for (Event event : events) {
            Long views = getViewsFromStatServer(event);
            result.add(eventMapper.eventShortDtoFromEvent(event, views));
        }

        return result;
    }

    @Override
    public EventFullDto getEvent(Long userId, Long eventId) {
        User user = userService.getOrThrow(userId);
        Event event = getOrThrow(eventId);
        checkEventInitiatorOrThrow(event, user);
        Long views = getViewsFromStatServer(event);

        return eventMapper.eventFullDtoFromEvent(event, views);
    }

    @Override
    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        User user = userService.getOrThrow(userId);
        Event event = getOrThrow(eventId);

        checkEventInitiatorOrThrow(event, user);
        if (event.getState().equals(PUBLISHED)) {
            throw new AccessException("Event is already published");
        }
        if (updateEventUserRequest.getEventDate() != null) {
            checkEventDateOrThrow(updateEventUserRequest.getEventDate());
        }
        Long views = getViewsFromStatServer(event);
        Location location = locationRepository.save(locationMapper.locationFromDto(updateEventUserRequest.getLocation()));

        eventMapper.updateEventFromDto(event,
                updateEventUserRequest,
                location);
        return eventMapper.eventFullDtoFromEvent(eventRepository.save(event), views);
    }

    @Override
    public void decreaseConfirmedRequests(Event event) {
        Long confirmedRequestsNew = event.getConfirmedRequests() - 1L;
        event.setConfirmedRequests(confirmedRequestsNew);
        eventRepository.saveAndFlush(event);
    }

    @Override
    public void updateEvent(Event event) {
        eventRepository.saveAndFlush(event);
    }

    private void checkEventDateOrThrow(LocalDateTime eventTime) {
        LocalDateTime actualTime = LocalDateTime.now().plusHours(2L);
        if (eventTime.isBefore(actualTime)) {
            throw new ValidationException("Event date and time has to be in the future");
        }
    }

    private Long getViewsFromStatServer(Event event) {
        LocalDateTime start = LocalDateTime.MIN;
        LocalDateTime end = LocalDateTime.MAX;
        String uri = String.format("/events/%d", event.getId());
        String[] uris = {uri};
        Boolean unique = false;

        List<ViewStatsResponseDto> statistics = statsClient.getStatistics(start, end, uris, unique);

        if (statistics.isEmpty()) {
            return 0L;
        } else {
            return statistics.get(0).getHits();
        }
    }

    public void increaseConfirmedRequests(Event event) {
        Long confirmedRequestsNew = event.getConfirmedRequests() + 1L;
        event.setConfirmedRequests(confirmedRequestsNew);
        eventRepository.saveAndFlush(event);
    }

    public Event getOrThrow(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(Event.class, String.format("ID: %s", eventId)));
    }

    private void checkEventInitiatorOrThrow(Event event, User user) {
        if (!event.getInitiator().equals(user)) {
            throw new AccessException("User not initiator for event");
        }
    }
}
