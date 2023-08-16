package ru.practicum.ewm.main.service.compilation.service;

import ru.practicum.ewm.main.service.compilation.dto.CompilationDto;
import ru.practicum.ewm.main.service.compilation.dto.CompilationNewDto;
import ru.practicum.ewm.main.service.compilation.dto.CompilationUpdateDto;

import java.util.List;

public interface CompilationService {
    CompilationDto create(CompilationNewDto compilationNewDto);

    void delete(Long compId);

    CompilationDto update(Long compId, CompilationUpdateDto compilationUpdateDto);

    List<CompilationDto> getAll(Boolean pinned, Integer from, Integer size);

    CompilationDto getCompilation(Long compId);
}
