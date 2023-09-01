package ru.practicum.ewm.main.service.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventRateDto {
    private Long eventId;
    private Long userId;
    private Long rate;
}
