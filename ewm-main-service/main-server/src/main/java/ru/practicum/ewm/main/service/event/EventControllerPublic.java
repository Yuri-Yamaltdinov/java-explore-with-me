package ru.practicum.ewm.main.service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.service.event.dto.EventFullDto;
import ru.practicum.ewm.main.service.event.dto.EventShortDto;
import ru.practicum.ewm.main.service.event.model.EventRateView;
import ru.practicum.ewm.main.service.event.model.InitiatorRateView;
import ru.practicum.ewm.main.service.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Validated
@Slf4j
public class EventControllerPublic {
    private final EventService eventService;
/*

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getAllEvents(@RequestParam(required = false) String text,
                                            @RequestParam(required = false) List<Long> categories,
                                            @RequestParam(required = false) Boolean paid,
                                            @RequestParam(required = false) String rangeStart,
                                            @RequestParam(required = false) String rangeEnd,
                                            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                            @RequestParam(required = false) String sort,
                                            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                            @Positive @RequestParam(defaultValue = "10") Integer size,
                                            HttpServletRequest request) {
        log.info("Got request GET all events with parameters: text = {}, categories = {}, paid={}, rangeStart = {}, rangeEnd = {}" +
                ", onlyAvailable={}, sort={}, from = {}, size = {}", text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        LocalDateTime start = (rangeStart == null) ? null : LocalDateTime.parse(rangeStart, FORMATTER);
        LocalDateTime end = (rangeEnd == null) ? null : LocalDateTime.parse(rangeEnd, FORMATTER);
        return eventService.getAllEventsPublic(text, categories, paid, start, end, onlyAvailable, sort, from, size, request);
    }
*/

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getAllEvents(@RequestParam(required = false) String text,
                                            @RequestParam(required = false) List<Long> categories,
                                            @RequestParam(required = false) Boolean paid,
                                            @RequestParam(name = "rangeStart", required = false)
                                            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                            @RequestParam(name = "rangeEnd", required = false)
                                            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                            @RequestParam(required = false) String sort,
                                            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                            @Positive @RequestParam(defaultValue = "10") Integer size,
                                            HttpServletRequest request) {
        log.info("Got request GET all events with parameters: text = {}, categories = {}, paid={}, rangeStart = {}, rangeEnd = {}" +
                ", onlyAvailable={}, sort={}, from = {}, size = {}", text, categories, paid, rangeStart.toString(), rangeEnd.toString(), onlyAvailable, sort, from, size);
        //LocalDateTime start = (rangeStart == null) ? null : LocalDateTime.parse(rangeStart, FORMATTER);
        //LocalDateTime end = (rangeEnd == null) ? null : LocalDateTime.parse(rangeEnd, FORMATTER);
        return eventService.getAllEventsPublic(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request);
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEvent(@PathVariable Long eventId, HttpServletRequest request) {
        log.info("Got request GET event, id={}", eventId);
        return eventService.getEventPublic(eventId, request);
    }

    @GetMapping("/ratings")
    @ResponseStatus(HttpStatus.OK)
    public List<EventRateView> getEventRatings(@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                               @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Got request to GET ratings");
        return eventService.getEventsRatings(from, size);
    }

    @GetMapping("/users/ratings")
    @ResponseStatus(HttpStatus.OK)
    public List<InitiatorRateView> getUsersRatings(@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                   @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Got request to GET ratings");
        return eventService.getUsersRatings(from, size);
    }


}
