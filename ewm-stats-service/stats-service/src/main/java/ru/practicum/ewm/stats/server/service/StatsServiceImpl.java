package ru.practicum.ewm.stats.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.stats.dto.HitRequestDto;
import ru.practicum.ewm.stats.dto.ViewStatsResponseDto;
import ru.practicum.ewm.stats.server.mapper.HitMapper;
import ru.practicum.ewm.stats.server.mapper.ViewStatsMapper;
import ru.practicum.ewm.stats.server.model.Hit;
import ru.practicum.ewm.stats.server.repository.StatsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;
    private final HitMapper hitMapper;
    private final ViewStatsMapper viewMapper;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public StatsServiceImpl(StatsRepository statsRepository, HitMapper hitMapper, ViewStatsMapper viewMapper) {
        this.statsRepository = statsRepository;
        this.hitMapper = hitMapper;
        this.viewMapper = viewMapper;
    }

    @Override
    public void createStat(HitRequestDto hitRequestDto) {
        Hit hit = hitMapper.hitFromRequestDto(hitRequestDto);
        statsRepository.save(hit);
    }

    @Override
    public List<ViewStatsResponseDto> getStats(String start, String end, List<String> uris, Boolean unique) {
        LocalDateTime startTime = LocalDateTime.parse(start, formatter);
        LocalDateTime endTime = LocalDateTime.parse(end, formatter);

        if (unique && uris != null) {
            return statsRepository.getUrisWithUniqueIP(startTime, endTime, uris).stream()
                    .map(viewMapper::dtoFromView)
                    .collect(Collectors.toList());
        } else if (uris != null) {
            return statsRepository.getUrisStats(startTime, endTime, uris).stream()
                    .map(viewMapper::dtoFromView)
                    .collect(Collectors.toList());
        } else if (unique) {
            return statsRepository.getAllUrisWithUniqueIP(startTime, endTime).stream()
                    .map(viewMapper::dtoFromView)
                    .collect(Collectors.toList());
        } else {
            return statsRepository.getAllUrisStats(startTime, endTime).stream()
                    .map(viewMapper::dtoFromView)
                    .collect(Collectors.toList());
        }
    }
}
