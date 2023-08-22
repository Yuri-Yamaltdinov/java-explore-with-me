package ru.practicum.ewm.stats.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ViewStatsResponseDto {
    String app;
    String uri;
    Long hits;
}
