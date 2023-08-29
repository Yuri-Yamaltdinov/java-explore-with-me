package ru.practicum.ewm.main.service.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.main.service.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.main.service.request.model.ParticipationRequest;

@Mapper(componentModel = "spring")
public interface ParticipationRequestMapper {

    @Mapping(target = "event", source = "request.event.id")
    @Mapping(target = "requester", source = "request.requester.id")
    ParticipationRequestDto requestToDto(ParticipationRequest request);
}
