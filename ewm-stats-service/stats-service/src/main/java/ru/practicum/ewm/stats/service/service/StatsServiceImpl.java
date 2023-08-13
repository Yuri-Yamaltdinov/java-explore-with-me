package ru.practicum.ewm.stats.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.stats.dto.HitRequestDto;
import ru.practicum.ewm.stats.dto.ViewStatsResponseDto;
import ru.practicum.ewm.stats.service.mapper.HitMapper;
import ru.practicum.ewm.stats.service.mapper.ViewStatsMapper;
import ru.practicum.ewm.stats.service.model.Hit;
import ru.practicum.ewm.stats.service.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;
    private final HitMapper hitMapper;
    private final ViewStatsMapper viewMapper;

    @Override
    @Transactional
    public void createStat(HitRequestDto hitRequestDto) {
        Hit hit = hitMapper.hitFromRequestDto(hitRequestDto);
        statsRepository.save(hit);
    }

    @Override
    public List<ViewStatsResponseDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {

        if (unique && uris != null) {
            return statsRepository.getUrisWithUniqueIP(start, end, uris).stream()
                    .map(viewMapper::dtoFromView)
                    .collect(Collectors.toList());
        } else if (uris != null) {
            return statsRepository.getUrisStats(start, end, uris).stream()
                    .map(viewMapper::dtoFromView)
                    .collect(Collectors.toList());
        } else if (unique) {
            return statsRepository.getAllUrisWithUniqueIP(start, end).stream()
                    .map(viewMapper::dtoFromView)
                    .collect(Collectors.toList());
        } else {
            return statsRepository.getAllUrisStats(start, end).stream()
                    .map(viewMapper::dtoFromView)
                    .collect(Collectors.toList());
        }
    }
}
