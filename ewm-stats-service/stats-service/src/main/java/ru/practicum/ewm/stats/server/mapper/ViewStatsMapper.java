package ru.practicum.ewm.stats.server.mapper;

import org.mapstruct.Mapper;
import ru.practicum.ewm.stats.dto.ViewStatsResponseDto;
import ru.practicum.ewm.stats.server.model.ViewStats;

@Mapper(componentModel = "spring")
public interface ViewStatsMapper {
    ViewStatsResponseDto dtoFromView(ViewStats viewStats);
}
