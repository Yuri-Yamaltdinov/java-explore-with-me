package ru.practicum.ewm.main.service.compilation.mapper;

import org.mapstruct.*;
import ru.practicum.ewm.main.service.compilation.dto.CompilationDto;
import ru.practicum.ewm.main.service.compilation.dto.CompilationNewDto;
import ru.practicum.ewm.main.service.compilation.dto.CompilationUpdateDto;
import ru.practicum.ewm.main.service.compilation.model.Compilation;
import ru.practicum.ewm.main.service.event.dto.EventShortDto;
import ru.practicum.ewm.main.service.event.model.Event;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface CompilationMapper {
    @Mapping(target = "events", source = "events")
    Compilation compilationFromNewDto(CompilationNewDto compilationNewDto, Set<Event> events);

    @Mapping(target = "events", source = "events")
    CompilationDto compilationToDto(Compilation compilation, Set<EventShortDto> events);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "events", source = "eventsNew")
    void updateCompilationFromDto(CompilationUpdateDto compilationUpdateDto,
                                  @MappingTarget Compilation compOld,
                                  Set<Event> eventsNew);
}
