package ru.practicum.ewm.stats.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ViewStatsResponseDto {
    String app;
    String uri;
    Long hits;
}
