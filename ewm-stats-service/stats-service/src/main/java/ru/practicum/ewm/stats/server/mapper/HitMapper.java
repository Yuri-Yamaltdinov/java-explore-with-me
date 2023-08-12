package ru.practicum.ewm.stats.server.mapper;

import org.mapstruct.Mapper;
import ru.practicum.ewm.stats.dto.HitRequestDto;
import ru.practicum.ewm.stats.server.model.Hit;

@Mapper(componentModel = "spring")
public interface HitMapper {
    Hit hitFromRequestDto(HitRequestDto hitRequestDto);
}
