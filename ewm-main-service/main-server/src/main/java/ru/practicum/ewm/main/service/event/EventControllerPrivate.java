package ru.practicum.ewm.main.service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.service.event.dto.*;
import ru.practicum.ewm.main.service.event.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@Validated
@Slf4j
public class EventControllerPrivate {
    private final EventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getAllEvents(@PathVariable Long userId,
                                            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get events with userId={}, from={}, size={}", userId, from, size);
        return eventService.getAll(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable Long userId,
                                    @RequestBody @Valid NewEventDto newEventDto) {
        log.info("Create new event, title={}", newEventDto.getTitle());

        return eventService.create(userId, newEventDto);
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEvent(@PathVariable Long userId,
                                 @PathVariable Long eventId) {
        log.info("Get event with userId={}, eventId={}", userId, eventId);
        return eventService.getEvent(userId, eventId);
    }


    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEvent(@PathVariable Long userId,
                                    @PathVariable Long eventId,
                                    @RequestBody @Valid UpdateEventUserRequest updateEventUserRequest) {
        log.info("Update event with userId={}, eventId={}", userId, eventId);
        return eventService.updateEvent(userId, eventId, updateEventUserRequest);
    }

    @PostMapping("/{eventId}/rate")
    @ResponseStatus(HttpStatus.CREATED)
    public EventRateDto addLike(@PathVariable Long userId,
                                @PathVariable Long eventId,
                                @RequestParam(required = true) Boolean isLike) {
        log.info("Add like or dislike to event={}, by user={}", eventId, userId);
        return eventService.addLike(userId, eventId, isLike);
    }

    @DeleteMapping("/{eventId}/rate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLike(@PathVariable Long userId,
                                   @PathVariable Long eventId) {
        log.info("Delete like or dislike from event={}, by user={}", eventId, userId);
        eventService.deleteLike(userId, eventId);
    }

}
