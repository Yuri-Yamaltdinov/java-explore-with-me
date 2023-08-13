package ru.practicum.ewm.main.service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.service.event.dto.EventFullDto;
import ru.practicum.ewm.main.service.event.dto.EventShortDto;
import ru.practicum.ewm.main.service.event.dto.NewEventDto;
import ru.practicum.ewm.main.service.event.dto.UpdateEventUserRequest;
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

    @PostMapping
    public ResponseEntity<EventFullDto> createEvent(@PathVariable Long userId,
                                                    @RequestBody @Valid NewEventDto newEventDto) {
        log.info("Create new event, title={}", newEventDto.getTitle());

        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.create(userId, newEventDto));
    }

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getAllEvents(@PathVariable Long userId,
                                                            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get events with userId={}, from={}, size={}", userId, from, size);
        return ResponseEntity.ok(eventService.getAll(userId, from, size));
    }
    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getEvent(@PathVariable Long userId,
                                                 @PathVariable Long eventId) {
        log.info("Get event with userId={}, eventId={}", userId, eventId);
        return ResponseEntity.ok(eventService.getEvent(userId, eventId));
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateEvent(@PathVariable Long userId,
                                                    @PathVariable Long eventId,
                                                    @RequestBody @Valid UpdateEventUserRequest updateEventUserRequest) {
        log.info("Update event with userId={}, eventId={}", userId, eventId);
        return ResponseEntity.ok(eventService.updateEvent(userId, eventId, updateEventUserRequest));
    }
}
