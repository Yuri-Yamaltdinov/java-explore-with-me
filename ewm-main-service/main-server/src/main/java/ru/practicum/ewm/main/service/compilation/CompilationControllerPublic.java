package ru.practicum.ewm.main.service.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.service.compilation.dto.CompilationDto;
import ru.practicum.ewm.main.service.compilation.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CompilationControllerPublic {

    private final CompilationService compilationService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CompilationDto> getAll(@RequestParam(required = false) Boolean pinned,
                                       @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                       @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Got request to GET compilations with pinned={}, from={}, size={}", pinned, from, size);
        return compilationService.getAll(pinned, from, size);
    }

    @GetMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto getCompilation(@PathVariable Long compId) {
        log.info("Got request to GET compilation with id={}", compId);
        return compilationService.getCompilation(compId);
    }
}
