package ru.practicum.ewm.stats.server.service;

import ru.practicum.ewm.stats.dto.HitRequestDto;
import ru.practicum.ewm.stats.dto.ViewStatsResponseDto;

import java.util.List;

public interface StatsService {
    void createStat(HitRequestDto hitRequestDto);

    List<ViewStatsResponseDto> getStats(String start, String end, List<String> uris, Boolean unique);
}
