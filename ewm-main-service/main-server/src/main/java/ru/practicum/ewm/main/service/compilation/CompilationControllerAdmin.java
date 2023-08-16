package ru.practicum.ewm.main.service.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.service.compilation.dto.CompilationDto;
import ru.practicum.ewm.main.service.compilation.dto.CompilationNewDto;
import ru.practicum.ewm.main.service.compilation.dto.CompilationUpdateDto;
import ru.practicum.ewm.main.service.compilation.service.CompilationService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CompilationControllerAdmin {

    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto create(@RequestBody @Valid CompilationNewDto compilationNewDto) {
        log.info("Got request to POST compilation, title={}.", compilationNewDto.getTitle());
        return compilationService.create(compilationNewDto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long compId) {
        log.info("Got request to DELETE compilation id={}.", compId);
        compilationService.delete(compId);
    }

    @PatchMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto update(@PathVariable Long compId,
                                 @RequestBody CompilationUpdateDto compilationUpdateDto) {
        log.info("Got request to PATCH compilation id={}.", compId);
        return compilationService.update(compId, compilationUpdateDto);
    }
}
