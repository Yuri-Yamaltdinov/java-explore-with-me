package ru.practicum.ewm.stats.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.stats.dto.HitRequestDto;
import ru.practicum.ewm.stats.dto.ViewStatsResponseDto;
import ru.practicum.ewm.stats.server.service.StatsService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Validated
@Slf4j
public class StatsController {
    @Autowired
    private StatsService statsService;

    @PostMapping("/hit")
    public ResponseEntity<Void> saveStat(@RequestBody @Valid HitRequestDto hitRequestDto) {
        log.info("Got request POST /hit with {}", hitRequestDto);
        statsService.createStat(hitRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/stats")
    public ResponseEntity<List<ViewStatsResponseDto>> getStats(@RequestParam String start,
                                                               @RequestParam String end,
                                                               @RequestParam(required = false) List<String> uris,
                                                               @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("Got request GET /stats with start: {}, end: {}, uris: {}, unique: {}",
                start, end, uris, unique);
        return ResponseEntity.ok(statsService.getStats(start, end, uris, unique));
    }
}
