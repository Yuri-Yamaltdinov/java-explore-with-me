package ru.practicum.ewm.main.service.event.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.main.service.event.dto.EventRateDto;
import ru.practicum.ewm.main.service.event.model.EventUserRate;

@Mapper(componentModel = "spring")
public interface EventRateMapper {

    @Mapping(target = "eventId", source = "eventUserRate.event.id")
    @Mapping(target = "userId", source = "eventUserRate.user.id")
    @Mapping(target = "rate", source = "eventUserRate.rate")
    EventRateDto eventRateToDto(EventUserRate eventUserRate);

}
