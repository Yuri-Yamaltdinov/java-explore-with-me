package ru.practicum.ewm.main.service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.service.event.dto.EventFullDto;
import ru.practicum.ewm.main.service.event.dto.UpdateEventUserRequest;
import ru.practicum.ewm.main.service.event.model.State;
import ru.practicum.ewm.main.service.event.service.EventService;
import ru.practicum.ewm.main.service.util.Constants;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Validated
@Slf4j
public class EventControllerAdmin {

    private final EventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> getAll(@RequestParam(required = false) List<Long> users,
                                     @RequestParam(required = false) List<State> states,
                                     @RequestParam(required = false) List<Long> categories,
                                     @RequestParam(required = false) @DateTimeFormat(pattern = Constants.DATE_FORMAT) LocalDateTime rangeStart,
                                     @RequestParam(required = false) @DateTimeFormat(pattern = Constants.DATE_FORMAT) LocalDateTime rangeEnd,
                                     @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                     @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Got request to GET all events with parameters");
        return eventService.getAllEventsAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto update(@PathVariable Long eventId,
                               @RequestBody @Valid UpdateEventUserRequest updateEventUserRequest) {
        log.info("Got request to PATCH event id={}", eventId);
        return eventService.updateEventAdmin(eventId, updateEventUserRequest);
    }
}
