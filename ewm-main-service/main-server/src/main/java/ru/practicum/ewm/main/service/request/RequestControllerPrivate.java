package ru.practicum.ewm.main.service.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.service.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.main.service.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.main.service.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.main.service.request.service.RequestService;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Validated
@Slf4j
public class RequestControllerPrivate {

    private final RequestService requestService;

    @PostMapping("/users/{userId}/requests")
    public ResponseEntity<ParticipationRequestDto> createRequest(@PathVariable Long userId,
                                                                 @RequestParam Long eventId) {
        log.info("Participation request is created, userId={}, eventId={}", userId, eventId);
        return ResponseEntity.status(CREATED).body(requestService.create(userId, eventId));
    }

    @GetMapping("/users/{userId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getRequestsByUser(@PathVariable Long userId) {
        log.info("Get participation request by userId={}", userId);
        return ResponseEntity.ok(requestService.getRequestsByRequester(userId));
    }

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> cancelRequest(@PathVariable Long userId,
                                                    @RequestParam Long requestId) {
        log.info("Participation request is canceled, requestId={}", requestId);
        return ResponseEntity.ok(requestService.cancelRequest(userId, requestId));
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getRequestsForEvent(@PathVariable Long userId,
                                                                @PathVariable Long eventId) {
        log.info("Get participation request by eventId={}, userId={}", eventId, userId);
        return ResponseEntity.ok(requestService.getRequestsForEvent(userId, eventId));
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResult> updateRequestStatus(@PathVariable Long userId,
                                                                              @PathVariable Long eventId,
                                                                              @RequestBody EventRequestStatusUpdateRequest requests) {
        log.info("Update participation requests size={} by eventId={}, userId={}",requests.getRequestIds().size(), eventId, userId);
        return ResponseEntity.ok(requestService.updateRequestStatus(userId, eventId, requests));
    }
}
