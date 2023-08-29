package ru.practicum.ewm.main.service.location.mapper;

import org.mapstruct.Mapper;
import ru.practicum.ewm.main.service.location.dto.LocationDto;
import ru.practicum.ewm.main.service.location.model.Location;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    Location locationFromDto(LocationDto locationDto);
}
