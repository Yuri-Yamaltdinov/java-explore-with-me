package ru.practicum.ewm.main.service.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.main.service.compilation.dto.CompilationDto;
import ru.practicum.ewm.main.service.compilation.dto.CompilationNewDto;
import ru.practicum.ewm.main.service.compilation.dto.CompilationUpdateDto;
import ru.practicum.ewm.main.service.compilation.mapper.CompilationMapper;
import ru.practicum.ewm.main.service.compilation.model.Compilation;
import ru.practicum.ewm.main.service.compilation.repository.CompilationRepository;
import ru.practicum.ewm.main.service.event.dto.EventShortDto;
import ru.practicum.ewm.main.service.event.model.Event;
import ru.practicum.ewm.main.service.event.repository.EventRepository;
import ru.practicum.ewm.main.service.event.service.EventService;
import ru.practicum.ewm.main.service.exception.EntityNotFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;
    private final EventRepository eventRepository;
    private final EventService eventService;

    @Override
    public CompilationDto create(CompilationNewDto compilationNewDto) {
        List<Event> events;
        if (compilationNewDto.getEvents() == null) {
            events = Collections.emptyList();
        } else {
            events = eventRepository.findByIdIn(compilationNewDto.getEvents());
        }

        Compilation compilation = compilationMapper.compilationFromNewDto(compilationNewDto, events);
        compilation = compilationRepository.saveAndFlush(compilation);
        List<EventShortDto> eventShortDtos = eventService.getAllShortDto(compilationNewDto.getEvents());
        return compilationMapper.compilationToDto(compilation, eventShortDtos);
    }

    @Override
    public void delete(Long compId) {
        Compilation compilation = getOrThrow(compId);
        compilationRepository.delete(compilation);
    }

    @Override
    public CompilationDto update(Long compId, CompilationUpdateDto compilationUpdateDto) {
        Compilation compilation = getOrThrow(compId);
        List<Event> eventsNew = eventService.getAll(compilationUpdateDto.getEvents());
        List<EventShortDto> eventShortDtos = eventService.getAllShortDto(compilationUpdateDto.getEvents());
        compilationMapper.updateCompilationFromDto(compilationUpdateDto, compilation, eventsNew);
        compilation = compilationRepository.saveAndFlush(compilation);
        return compilationMapper.compilationToDto(compilation, eventShortDtos);
    }

    @Override
    public List<CompilationDto> getAll(Boolean pinned, Integer from, Integer size) {
        Pageable page = PageRequest.of(from > 0 ? from / size : 0, size);
        List<Compilation> compilations = compilationRepository.findAllByPinned(pinned, page);
        List<CompilationDto> result = new ArrayList<>();
        for (Compilation compilation : compilations) {
            List<Long> eventsIds = compilation.getEvents().stream()
                    .map(Event::getId)
                    .collect(Collectors.toList());
            List<EventShortDto> eventShortDtos = eventService.getAllShortDto(eventsIds);
            result.add(compilationMapper.compilationToDto(compilation, eventShortDtos));
        }
        return result;
    }

    @Override
    public CompilationDto getCompilation(Long compId) {
        Compilation compilation = getOrThrow(compId);
        List<Long> eventsIds = compilation.getEvents().stream()
                .map(Event::getId)
                .collect(Collectors.toList());
        List<EventShortDto> eventShortDtos = eventService.getAllShortDto(eventsIds);

        return compilationMapper.compilationToDto(compilation, eventShortDtos);
    }

    private Compilation getOrThrow(Long compId) {
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new EntityNotFoundException(Compilation.class, "Compilation not found"));
    }
}