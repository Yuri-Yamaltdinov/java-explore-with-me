package ru.practicum.ewm.main.service.request.service;

import ru.practicum.ewm.main.service.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.main.service.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.main.service.request.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    ParticipationRequestDto create(Long userId, Long eventId);

    List<ParticipationRequestDto> getRequestsByRequester(Long userId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);

    List<ParticipationRequestDto> getRequestsForEvent(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateRequestStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest requests);
}
