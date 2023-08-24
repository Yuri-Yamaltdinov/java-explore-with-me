package ru.practicum.ewm.stats.client;

/*@RestController
@RequestMapping
@RequiredArgsConstructor
@Validated
@Slf4j
public class StatsClientController {
    @Autowired
    private final StatsClient statsClient;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveStat(@RequestBody @Valid HitRequestDto hitRequestDto) {
        log.info("Got request POST /hit with {}", hitRequestDto);
        statsClient.createStat(hitRequestDto);
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<ViewStatsResponseDto> getStats(@RequestParam String start,
                                               @RequestParam String end,
                                               @RequestParam(required = false) String[] uris,
                                               @RequestParam(required = false, defaultValue = "false") Boolean unique,
                                               HttpServletRequest request) {
        log.info("Got request GET /stats with start: {}, end: {}, uris: {}, unique: {}",
                start, end, uris, unique);
        LocalDateTime startTime = LocalDateTime.parse(URLDecoder.decode(start, UTF_8), FORMATTER);
        LocalDateTime endTime = LocalDateTime.parse(URLDecoder.decode(end, UTF_8), FORMATTER);
        return statsClient.getStatistics(startTime, endTime, uris, unique);
    }

}*/
