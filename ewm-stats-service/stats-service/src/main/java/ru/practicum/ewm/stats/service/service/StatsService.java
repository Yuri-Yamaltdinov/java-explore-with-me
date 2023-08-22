package ru.practicum.ewm.stats.service.service;

import ru.practicum.ewm.stats.dto.HitRequestDto;
import ru.practicum.ewm.stats.dto.ViewStatsResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    HitRequestDto createStat(HitRequestDto hitRequestDto);

    List<ViewStatsResponseDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
