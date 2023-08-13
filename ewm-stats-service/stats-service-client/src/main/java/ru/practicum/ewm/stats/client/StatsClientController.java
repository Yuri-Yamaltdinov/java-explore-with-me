package ru.practicum.ewm.stats.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.stats.dto.HitRequestDto;
import ru.practicum.ewm.stats.dto.ViewStatsResponseDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static ru.practicum.ewm.stats.dto.util.Constants.FORMATTER;

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
    public ResponseEntity<List<ViewStatsResponseDto>> getStats(@RequestParam String start,
                                                               @RequestParam String end,
                                                               @RequestParam(required = false) String[] uris,
                                                               @RequestParam(required = false, defaultValue = "false") Boolean unique,
                                                               HttpServletRequest request) {
        log.info("Got request GET /stats with start: {}, end: {}, uris: {}, unique: {}",
                start, end, uris, unique);
        LocalDateTime startTime = LocalDateTime.parse(URLDecoder.decode(start, UTF_8), FORMATTER);
        LocalDateTime endTime = LocalDateTime.parse(URLDecoder.decode(end, UTF_8), FORMATTER);
        return ResponseEntity.ok(statsClient.getStatistics(startTime, endTime, uris, unique));
    }

}
