package ru.practicum.ewm.stats.service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.stats.dto.HitRequestDto;
import ru.practicum.ewm.stats.dto.ViewStatsResponseDto;
import ru.practicum.ewm.stats.service.service.StatsService;

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
@Slf4j
public class StatsController {
    @Autowired
    private final StatsService statsService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public HitRequestDto saveStat(@RequestBody @Valid HitRequestDto hitRequestDto) {
        log.info("Got request POST /hit, with {}", hitRequestDto);
        return statsService.createStat(hitRequestDto);
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<ViewStatsResponseDto> getStats(@RequestParam String start,
                                               @RequestParam String end,
                                               @RequestParam(required = false) List<String> uris,
                                               @RequestParam(required = false, defaultValue = "false") Boolean unique,
                                               HttpServletRequest request) {
        log.info("Got request GET /stats, with start: {}, end: {}, uris: {}, unique: {}",
                start, end, uris, unique);
        LocalDateTime startTime = LocalDateTime.parse(URLDecoder.decode(start, UTF_8), FORMATTER);
        LocalDateTime endTime = LocalDateTime.parse(URLDecoder.decode(end, UTF_8), FORMATTER);
        return statsService.getStats(startTime, endTime, uris, unique);
    }

}
