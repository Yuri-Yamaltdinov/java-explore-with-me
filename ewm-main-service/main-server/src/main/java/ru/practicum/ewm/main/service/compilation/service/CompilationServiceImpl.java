package ru.practicum.ewm.main.service.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import ru.practicum.ewm.main.service.util.Pagination;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;
    private final EventRepository eventRepository;
    private final EventService eventService;

    @Override
    @Transactional
    public CompilationDto create(CompilationNewDto compilationNewDto) {
        if (compilationNewDto.getPinned() == null) {
            compilationNewDto.setPinned(false);
        }
        Set<Event> events;
        if (compilationNewDto.getEvents() == null) {
            events = Collections.emptySet();
        } else {
            events = eventRepository.findByIdIn(compilationNewDto.getEvents());
        }

        Compilation compilation = compilationMapper.compilationFromNewDto(compilationNewDto, events);
        compilation = compilationRepository.saveAndFlush(compilation);
        Set<EventShortDto> eventShortDtos = eventService.getAllShortDto(compilationNewDto.getEvents());
        return compilationMapper.compilationToDto(compilation, eventShortDtos);
    }

    @Override
    @Transactional
    public CompilationDto update(Long compId, CompilationUpdateDto compilationUpdateDto) {
        Compilation compilation = getOrThrow(compId);
        Set<Event> eventsNew = eventService.getAll(compilationUpdateDto.getEvents());
        Set<EventShortDto> eventShortDtos = eventService.getAllShortDto(compilationUpdateDto.getEvents());
        compilationMapper.updateCompilationFromDto(compilationUpdateDto, compilation, eventsNew);
        return compilationMapper.compilationToDto(compilation, eventShortDtos);
    }

    @Override
    @Transactional
    public void delete(Long compId) {
        Compilation compilation = getOrThrow(compId);
        compilationRepository.delete(compilation);
    }

    @Override
    public List<CompilationDto> getAll(Boolean pinned, Integer from, Integer size) {
        Pagination page = new Pagination(from, size);
        List<Compilation> compilations;
        if (pinned == null) {
            compilations = compilationRepository.findAll(page).getContent();
        } else {
            compilations = compilationRepository.findAllByPinned(pinned, page);
        }
        List<CompilationDto> result = new ArrayList<>();
        for (Compilation compilation : compilations) {
            Set<Long> eventsIds = compilation.getEvents().stream()
                    .map(Event::getId)
                    .collect(Collectors.toSet());
            Set<EventShortDto> eventShortDtos = eventService.getAllShortDto(eventsIds);
            result.add(compilationMapper.compilationToDto(compilation, eventShortDtos));
        }
        return result;
    }

    @Override
    public CompilationDto getCompilation(Long compId) {
        Compilation compilation = getOrThrow(compId);
        Set<Long> eventsIds = compilation.getEvents().stream()
                .map(Event::getId)
                .collect(Collectors.toSet());
        Set<EventShortDto> eventShortDtos = eventService.getAllShortDto(eventsIds);

        return compilationMapper.compilationToDto(compilation, eventShortDtos);
    }

    private Compilation getOrThrow(Long compId) {
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new EntityNotFoundException(Compilation.class, "Compilation not found"));
    }
}
