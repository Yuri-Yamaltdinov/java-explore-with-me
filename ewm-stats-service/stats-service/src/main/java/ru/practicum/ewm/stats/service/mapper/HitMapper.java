package ru.practicum.ewm.stats.service.mapper;

import org.mapstruct.Mapper;
import ru.practicum.ewm.stats.dto.HitRequestDto;
import ru.practicum.ewm.stats.service.model.Hit;

@Mapper(componentModel = "spring")
public interface HitMapper {
    Hit hitFromRequestDto(HitRequestDto hitRequestDto);

    HitRequestDto hitToRequestDto(Hit hit);
}
