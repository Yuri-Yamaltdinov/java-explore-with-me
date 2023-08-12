package ru.practicum.ewm.stats.service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.stats.dto.HitRequestDto;
import ru.practicum.ewm.stats.dto.ViewStatsResponseDto;
import ru.practicum.ewm.stats.service.service.StatsService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Validated
@Slf4j
public class StatsController {
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private StatsService statsService;

    @PostMapping("/hit")
    public ResponseEntity<Void> saveStat(@RequestBody @Valid HitRequestDto hitRequestDto) {
        log.info("Got request POST /hit with {}", hitRequestDto);
        statsService.createStat(hitRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/stats")
    public ResponseEntity<List<ViewStatsResponseDto>> getStats(
                                @RequestParam(name = "start", required = true)
                                @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime start,
                                @RequestParam(name = "end", required = true)
                                @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime end,
                                @RequestParam(name = "uris", required = false) List<String> uris,
                                @RequestParam(name = "unique", required = false, defaultValue = "false") boolean unique
    ) {
        log.info("Got request GET /stats with start: {}, end: {}, uris: {}, unique: {}",
                start, end, uris, unique);
        return ResponseEntity.ok(statsService.getStats(start, end, uris, unique));
    }
}
