package ru.practicum.ewm.main.service.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.main.service.event.dto.*;
import ru.practicum.ewm.main.service.event.mapper.EventMapper;
import ru.practicum.ewm.main.service.event.mapper.EventRateMapper;
import ru.practicum.ewm.main.service.event.model.*;
import ru.practicum.ewm.main.service.event.repository.EventRepository;
import ru.practicum.ewm.main.service.event.repository.RateRepository;
import ru.practicum.ewm.main.service.exception.ConflictException;
import ru.practicum.ewm.main.service.exception.CustomValidationException;
import ru.practicum.ewm.main.service.exception.EntityNotFoundException;
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
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.ewm.main.service.event.model.State.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {

    private final StatsClient statsClient;
    private final UserService userService;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final EventRateMapper eventRateMapper;
    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;
    private final RateRepository rateRepository;

    @Override
    @Transactional
    public EventFullDto create(Long userId, NewEventDto newEventDto) {
        User initiator = userService.getOrThrow(userId);
        Long timeDiff = 2L;
        checkEventDateOrThrow(newEventDto.getEventDate(), timeDiff);
        Location location;
        try {
            location = locationRepository.save(locationMapper.locationFromDto(newEventDto.getLocation()));
        } catch (DataIntegrityViolationException e) {
            throw new CustomValidationException("Incorrect location values");
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
        if (events == null) {
            return null;
        }
        if (events.isEmpty()) {
            return Collections.emptyList();
        }
        Map<String, Long> statistics = getViewsFromStatServer(events);
        List<EventShortDto> result = new ArrayList<>();
        for (Event event : events) {
            EventShortDto eventShortDto = eventMapper.eventShortDtoFromEvent(event, statistics.get("/events/" + event.getId()));
            result.add(eventShortDto);
        }
        return result;
    }

    @Override
    public Set<EventShortDto> getAllShortDto(Set<Long> ids) {
        if (ids == null) {
            return Collections.emptySet();
        }
        List<Event> events = eventRepository.findAllById(ids);
        Map<String, Long> statistics = getViewsFromStatServer(events);
        List<EventShortDto> result = new ArrayList<>();
        for (Event event : events) {
            EventShortDto eventShortDto = eventMapper.eventShortDtoFromEvent(event, statistics.get("/events/" + event.getId()));
            result.add(eventShortDto);
        }

        return new HashSet<>(result);
    }

    @Override
    public Set<Event> getAll(Set<Long> ids) {
        if (ids == null) {
            return Collections.emptySet();
        }
        return eventRepository.findByIdIn(ids);
    }

    @Override
    public EventFullDto getEvent(Long userId, Long eventId) {
        User user = userService.getOrThrow(userId);
        Event event = getOrThrow(eventId);
        checkEventInitiatorOrThrow(event, user);

        String uri = "/events/" + event.getId();
        Map<String, Long> result = getViewsFromStatServer(List.of(event));
        Long views = result.get(uri);

        return eventMapper.eventFullDtoFromEvent(event, views);
    }

    @Override
    public void updateEvent(Event event) {
        eventRepository.saveAndFlush(event);
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        User user = userService.getOrThrow(userId);
        Event event = getOrThrow(eventId);
        Long timeDiff = 2L;
        checkEventInitiatorOrThrow(event, user);
        if (event.getState().equals(PUBLISHED)) {
            throw new ConflictException("Event is already published");
        }
        if (updateEventUserRequest.getEventDate() != null) {
            checkEventDateOrThrow(updateEventUserRequest.getEventDate(), timeDiff);
        }
        if (updateEventUserRequest.getStateAction() != null) {
            switch (updateEventUserRequest.getStateAction()) {
                case SEND_TO_REVIEW:
                    event.setState(PENDING);
                    break;
                case CANCEL_REVIEW:
                    event.setState(CANCELED);
                    break;
            }
        }

        eventMapper.updateEventFromURDto(event, updateEventUserRequest);

        String uri = "/events/" + event.getId();
        Map<String, Long> result = getViewsFromStatServer(List.of(event));
        Long views = result.get(uri);

        return eventMapper.eventFullDtoFromEvent(event, views);
    }

    @Override
    @Transactional
    public EventFullDto updateEventAdmin(Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        Event event = getOrThrow(eventId);
        Long timeDiff = 1L;

        if (updateEventUserRequest.getStateAction() != null) {
            switch (updateEventUserRequest.getStateAction()) {
                case PUBLISH_EVENT:
                    if (event.getState().equals(PENDING)) {
                        event.setState(PUBLISHED);
                        event.setPublishedOn(LocalDateTime.now());
                    } else {
                        throw new ConflictException("Event status is not PENDING. Event can't be PUBLISHED.");
                    }
                    break;
                case REJECT_EVENT:
                    if (!event.getState().equals(PUBLISHED)) {
                        event.setState(CANCELED);
                    } else {
                        throw new ConflictException("Event status is PUBLISHED. Event can't be CANCELED.");
                    }
                    break;
            }
        }

        if (updateEventUserRequest.getEventDate() != null) {
            checkEventDateOrThrow(updateEventUserRequest.getEventDate(), timeDiff);
        }

        String uri = "/events/" + event.getId();
        Map<String, Long> result = getViewsFromStatServer(List.of(event));
        Long views = result.get(uri);

        eventMapper.updateEventFromURDto(event, updateEventUserRequest);
        return eventMapper.eventFullDtoFromEvent(event, views);
    }

    @Override
    @Transactional
    public EventRateDto addLike(Long userId, Long eventId, Boolean isLike) {
        User user = userService.getOrThrow(userId);
        Event event = getOrThrow(eventId);
        long rate;
        if (isLike) {
            rate = 1L;
        } else {
            rate = -1L;
        }
        EventUserRate eventUserRate = EventUserRate.builder()
                                        .event(event)
                                        .user(user)
                                        .rate(rate)
                                        .build();
        return eventRateMapper.eventRateToDto(rateRepository.save(eventUserRate));
    }

    @Override
    @Transactional
    public void deleteLike(Long userId, Long eventId) {
        User user = userService.getOrThrow(userId);
        Event event = getOrThrow(eventId);
        if (!rateRepository.existsByUserIdAndEventId(userId,eventId)) {
            throw new EntityNotFoundException(EventUserRate.class, "Event Rate does not exist. Nothing to delete.");
        }
        EventUserRate eventUserRate = EventUserRate.builder()
                                            .event(event)
                                            .user(user)
                                            .build();
        rateRepository.delete(eventUserRate);
    }

    @Override
    public List<EventRateView> getEventsRatings(Integer from, Integer size) {
        Pagination page = new Pagination(from, size);
        return rateRepository.getAllEventsRateViews(page);
    }

    @Override
    public List<InitiatorRateView> getUsersRatings(Integer from, Integer size) {
        Pagination page = new Pagination(from, size);
        return rateRepository.getAllUsersRateViews(page);
    }


    @Override
    public void increaseConfirmedRequests(Event event) {
        Long confirmedRequestsNew = event.getConfirmedRequests() + 1L;
        event.setConfirmedRequests(confirmedRequestsNew);
        eventRepository.saveAndFlush(event);
    }

    @Override
    public void decreaseConfirmedRequests(Event event) {
        Long confirmedRequestsNew = event.getConfirmedRequests() - 1L;
        event.setConfirmedRequests(confirmedRequestsNew);
    }

    @Override
    public EventFullDto getEventPublic(Long eventId, HttpServletRequest request) {
        Event event = getOrThrow(eventId);
        if (!event.getState().equals(PUBLISHED)) {
            throw new EntityNotFoundException(Event.class, "Event is not PUBLISHED.");
        }
        addHit(request);

        String uri = "/events/" + event.getId();
        Map<String, Long> result = getViewsFromStatServer(List.of(event));
        Long views = result.get(uri);

        return eventMapper.eventFullDtoFromEvent(event, views);
    }

    @Override
    public List<EventShortDto> getAllEventsPublic(String text,
                                                  List<Long> categories,
                                                  Boolean paid,
                                                  LocalDateTime rangeStart,
                                                  LocalDateTime rangeEnd,
                                                  Boolean onlyAvailable,
                                                  String sort,
                                                  Integer from,
                                                  Integer size,
                                                  HttpServletRequest request) {
        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new CustomValidationException("Start must be after End");
        }
        Pagination page = new Pagination(from, size);
        List<Event> events = eventRepository
                            .findAllByParamsPublic(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, page);
        List<EventShortDto> result = new ArrayList<>();
        Map<String, Long> statistics = getViewsFromStatServer(events);

        for (Event event : events) {
            result.add(eventMapper.eventShortDtoFromEvent(event, statistics.get("/events/" + event.getId())));
        }

        if (sort != null) {
            SortValues sortValue = SortValues.from(sort).orElseThrow(() ->
                    new CustomValidationException(String.format("Unsupported status = %s", sort)));
            switch (sortValue) {
                case VIEWS:
                    result.sort((o1, o2) -> (int) (o1.getViews() - o2.getViews()));
                    break;
                case EVENT_DATE:
                    result.sort(Comparator.comparing(EventShortDto::getEventDate));
                    break;
                case RATES:
                    result.sort((o1, o2) -> (int) (o2.getRatesSum() - o1.getRatesSum()));
            }
        }
        addHit(request);
        return result;
    }

    @Override
    public List<EventFullDto> getAllEventsAdmin(List<Long> users,
                                                List<State> states,
                                                List<Long> categories,
                                                LocalDateTime rangeStart,
                                                LocalDateTime rangeEnd,
                                                Integer from,
                                                Integer size) {
        if (rangeStart != null && rangeEnd != null
                && rangeStart.isAfter(rangeEnd)) {
            throw new CustomValidationException("Start date and time must be after End");
        }
        Pagination page = new Pagination(from, size);
        List<Event> events = eventRepository.findAllByParamsAdmin(users, states, categories, rangeStart, rangeEnd, page);
        List<EventFullDto> result = new ArrayList<>();
        Map<String, Long> statistics = getViewsFromStatServer(events);

        for (Event event : events) {
            result.add(eventMapper.eventFullDtoFromEvent(event, statistics.get("/events/" + event.getId())));
        }
        return result;
    }

    @Override
    public Map<String, Long> getViewsFromStatServer(List<Event> events) {
        LocalDateTime start = LocalDateTime.of(1970, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.now();
        Boolean unique = true;
        List<String> uris = new ArrayList<>();
        for (Event event : events) {
            String uri = "/events/" + event.getId();
            uris.add(uri);
        }
        List<ViewStatsResponseDto> statistics = statsClient.getStatistics(start, end, uris.toArray(String[]::new), unique);
        if (statistics.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, Long> resultMap = statistics.stream()
                .collect(Collectors.toMap(ViewStatsResponseDto::getUri, ViewStatsResponseDto::getHits, (a, b) -> b));

        return resultMap;
    }

    private void addHit(HttpServletRequest request) {
        HitRequestDto hitRequestDto = HitRequestDto.builder()
                .app("ewm-service")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now()).build();

        try {
            statsClient.createStat(hitRequestDto);
            log.info("Create stat uri={}", hitRequestDto.getUri());
        } catch (RuntimeException e) {
            log.warn("Attempt to duplicate statistics. " + e.getMessage());
        }
    }

    private void checkEventDateOrThrow(LocalDateTime eventTime, Long timeDiff) {
        LocalDateTime actualTime = LocalDateTime.now().plusHours(timeDiff);
        if (eventTime.isBefore(actualTime)) {
            throw new CustomValidationException("Event date and time has to be in the future");
        }
    }

    public Event getOrThrow(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new EntityNotFoundException(Event.class, String.format("ID: %s", eventId)));
    }

    private void checkEventInitiatorOrThrow(Event event, User user) {
        if (!event.getInitiator().equals(user)) {
            throw new ConflictException("User not initiator for event");
        }
    }
}
