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
import ru.practicum.ewm.main.service.event.model.SortValues;
import ru.practicum.ewm.main.service.event.model.State;
import ru.practicum.ewm.main.service.event.model.StateAction;
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
import ru.practicum.ewm.stats.dto.HitRequestDto;
import ru.practicum.ewm.stats.dto.ViewStatsResponseDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ru.practicum.ewm.main.service.event.model.State.*;

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
        Long timeDiff = 2L;
        checkEventDateOrThrow(newEventDto.getEventDate(), timeDiff);
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
    public List<EventShortDto> getAllShortDto(List<Long> ids) {
        List<Event> events = eventRepository.findAllById(ids);
        List<EventShortDto> result = new ArrayList<>();

        for (Event event : events) {
            Long views = getViewsFromStatServer(event);
            result.add(eventMapper.eventShortDtoFromEvent(event, views));
        }
        return result;
    }

    @Override
    public List<Event> getAll(List<Long> ids) {
        if (ids == null) {
            return Collections.emptyList();
        }
        return eventRepository.findByIdIn(ids);
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
        Long timeDiff = 2L;
        checkEventInitiatorOrThrow(event, user);
        if (event.getState().equals(PUBLISHED)) {
            throw new AccessException("Event is already published");
        }
        if (updateEventUserRequest.getEventDate() != null) {
            checkEventDateOrThrow(updateEventUserRequest.getEventDate(), timeDiff);
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

    @Override
    public List<EventShortDto> getAllEventsPublic(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size, HttpServletRequest request) {
        if (rangeStart.isAfter(rangeEnd)) {
            throw new ValidationException("Start must be after End");
        }
        SortValues sortValue = SortValues.from(sort)
                .orElseThrow(() -> new ValidationException(String.format("Unsupported status = %s", sort)));
        Pagination page = new Pagination(from, size);

        List<Event> events = eventRepository.findAllByParamsPublic(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, page);
        List<EventShortDto> result = new ArrayList<>();
        sendStat(request);
        for (Event event : events) {
            Long views = getViewsFromStatServer(event);
            result.add(eventMapper.eventShortDtoFromEvent(event, views));
        }
        return result;
    }

    @Override
    public EventFullDto getEventPublic(Long eventId, HttpServletRequest request) {
        Event event = getOrThrow(eventId);
        if (!event.getState().equals(PUBLISHED)) {
            throw new EntityNotFoundException(Event.class, "Event is not PUBLISHED.");
        }
        Long views = getViewsFromStatServer(event);
        sendStat(request);
        return eventMapper.eventFullDtoFromEvent(event, views);
    }

    @Override
    public List<EventFullDto> getAllEventsAdmin(List<Long> users, List<State> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        if (rangeStart.isAfter(rangeEnd)) {
            throw new ValidationException("Start date and time must be after End");
        }

        Pagination page = new Pagination(from, size);
        List<Event> events = eventRepository.findAllByParamsAdmin(users, states, categories, rangeStart, rangeEnd, page);
        List<EventFullDto> result = new ArrayList<>();
        for (Event event : events) {
            Long views = getViewsFromStatServer(event);
            result.add(eventMapper.eventFullDtoFromEvent(event, views));
        }

        return result;
    }

    @Override
    public EventFullDto updateEventAdmin(Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        Event event = getOrThrow(eventId);
        Long timeDiff = 1L;
        if (updateEventUserRequest.getStateAction().equals(StateAction.PUBLISH_EVENT) &&
                event.getState().equals(PENDING)) {
            event.setState(PUBLISHED);
        } else {
            throw new AccessException("Event status is not PENDING. Event can't be PUBLISHED.");
        }

        if (updateEventUserRequest.getStateAction().equals(StateAction.REJECT_EVENT) &&
                !event.getState().equals(PUBLISHED)) {
            event.setState(CANCELED);
        } else {
            throw new AccessException("Event status is PUBLISHED. Event can't be CANCELED.");
        }

        if (updateEventUserRequest.getEventDate() != null) {
            checkEventDateOrThrow(updateEventUserRequest.getEventDate(), timeDiff);
        }
        Long views = getViewsFromStatServer(event);
        Location location = locationRepository.save(locationMapper.locationFromDto(updateEventUserRequest.getLocation()));

        eventMapper.updateEventFromDto(event,
                updateEventUserRequest,
                location);
        return eventMapper.eventFullDtoFromEvent(eventRepository.save(event), views);
    }

    private void sendStat(HttpServletRequest request) {
        HitRequestDto hitDto = HitRequestDto.builder()
                .app("ewm-service")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now()).build();
        statsClient.createStat(hitDto);
    }

    private void checkEventDateOrThrow(LocalDateTime eventTime, Long timeDiff) {
        LocalDateTime actualTime = LocalDateTime.now().plusHours(timeDiff);
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
