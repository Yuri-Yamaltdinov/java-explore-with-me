package ru.practicum.ewm.main.service.event.service;

import ru.practicum.ewm.main.service.event.dto.EventFullDto;
import ru.practicum.ewm.main.service.event.dto.EventShortDto;
import ru.practicum.ewm.main.service.event.dto.NewEventDto;
import ru.practicum.ewm.main.service.event.dto.UpdateEventUserRequest;
import ru.practicum.ewm.main.service.event.model.Event;
import ru.practicum.ewm.main.service.event.model.State;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface EventService {
    EventFullDto create(Long userId, NewEventDto newEventDto);

    List<EventShortDto> getAll(Long userId, Integer from, Integer size);

    Set<EventShortDto> getAllShortDto(Set<Long> ids);

    Set<Event> getAll(Set<Long> ids);

    EventFullDto getEvent(Long userId, Long eventId);

    EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    Event getOrThrow(Long eventId);

    Map<String, Long> getViewsFromStatServer(List<Event> events);

    void increaseConfirmedRequests(Event event);

    void decreaseConfirmedRequests(Event event);

    void updateEvent(Event event);

    List<EventShortDto> getAllEventsPublic(String text,
                                           List<Long> categories,
                                           Boolean paid,
                                           LocalDateTime rangeStart,
                                           LocalDateTime rangeEnd,
                                           Boolean onlyAvailable,
                                           String sort,
                                           Integer from,
                                           Integer size,
                                           HttpServletRequest request);

    EventFullDto getEventPublic(Long eventId, HttpServletRequest request);

    List<EventFullDto> getAllEventsAdmin(List<Long> users,
                                         List<State> states,
                                         List<Long> categories,
                                         LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd,
                                         Integer from,
                                         Integer size);

    EventFullDto updateEventAdmin(Long eventId, UpdateEventUserRequest updateEventUserRequest);
}
