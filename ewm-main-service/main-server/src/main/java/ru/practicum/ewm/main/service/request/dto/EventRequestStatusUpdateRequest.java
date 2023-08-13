package ru.practicum.ewm.main.service.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.main.service.request.model.RequestStatus;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
public class EventRequestStatusUpdateRequest {

    @NotNull
    private List<Long> requestIds;

    @NotNull
    private RequestStatus status;
}
