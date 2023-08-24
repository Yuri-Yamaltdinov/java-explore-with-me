package ru.practicum.ewm.main.service.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.service.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.main.service.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.main.service.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.main.service.request.service.RequestService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}")
@RequiredArgsConstructor
@Validated
@Slf4j
public class RequestControllerPrivate {

    private final RequestService requestService;

    @PostMapping("/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createRequest(@PathVariable Long userId,
                                                 @RequestParam Long eventId) {
        log.info("Participation request is created, userId={}, eventId={}", userId, eventId);
        return requestService.create(userId, eventId);
    }

    @GetMapping("/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getRequestsByUser(@PathVariable Long userId) {
        log.info("Get participation request by userId={}", userId);
        return requestService.getRequestsByRequester(userId);
    }

    @PatchMapping("/requests/{requestId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public ParticipationRequestDto cancelRequest(@PathVariable Long userId,
                                                 @PathVariable Long requestId) {
        log.info("Participation request is canceled, requestId={}", requestId);
        return requestService.cancelRequest(userId, requestId);
    }

    @GetMapping("/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getRequestsForEvent(@PathVariable Long userId,
                                                             @PathVariable Long eventId) {
        log.info("Get participation request by eventId={}, userId={}", eventId, userId);
        return requestService.getRequestsForEvent(userId, eventId);
    }

    @PatchMapping("/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public EventRequestStatusUpdateResult updateRequestStatus(@PathVariable Long userId,
                                                              @PathVariable Long eventId,
                                                              @RequestBody @Valid EventRequestStatusUpdateRequest requests) {
        log.info("Update participation requests size={} by eventId={}, userId={}", requests.getRequestIds().size(), eventId, userId);
        return requestService.updateRequestStatus(userId, eventId, requests);
    }
}
