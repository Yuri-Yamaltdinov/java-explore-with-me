package ru.practicum.ewm.stats.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.stats.dto.HitRequestDto;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Validated
@Slf4j
public class StatsClientController {
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    @Autowired
    private StatsClient statsClient;

    @PostMapping("/hit")
    public ResponseEntity<Void> saveStat(@RequestBody @Valid HitRequestDto hitRequestDto) {
        log.info("Got request POST /hit with {}", hitRequestDto);
        statsClient.createStat(hitRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> getStats(
                            @RequestParam(name = "start", required = true)
                            @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime start,
                            @RequestParam(name = "end", required = true)
                            @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime end,
                            @RequestParam(name = "uris", required = false) List<String> uris,
                            @RequestParam(name = "unique", required = false, defaultValue = "false") Boolean unique) {
        log.info("Got request GET /stats with start: {}, end: {}, uris: {}, unique: {}",
                start, end, uris, unique);
        return statsClient.getStatistics(start, end, uris, unique);
    }
}
