package ru.practicum.ewm.stats.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.stats.dto.HitRequestDto;
import ru.practicum.ewm.stats.dto.ViewStatsResponseDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static ru.practicum.ewm.stats.dto.util.Constants.FORMATTER;

@Service
public class StatsClient {

    private final RestTemplate rest;
    private final ObjectMapper objectMapper;
    private static final String POST_HIT_PATH = "/hit";
    private static final String GET_STATS_PATH = "/stats?start={start}&end={end}&uris={uris}&unique={unique}";
    private static final String GET_STATS_PATH_WITHOUT_URIS = "/stats?start={start}&end={end}&unique={unique}";

    @Autowired
    public StatsClient(@Value("${stats-service.url}") String serverUrl, RestTemplateBuilder builder, ObjectMapper objectMapper) {
        this.rest = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
        this.objectMapper = objectMapper;
    }

    public void createStat(HitRequestDto hitRequestDto) {
        makeAndSendRequest(HttpMethod.POST, POST_HIT_PATH, null, hitRequestDto);
    }

    public List<ViewStatsResponseDto> getStatistics(LocalDateTime start,
                                                    LocalDateTime end,
                                                    String[] uris,
                                                    Boolean unique) {
        String startTime = start.format(FORMATTER);
        String endTime = end.format(FORMATTER);
        Map<String, Object> parameters;
        ResponseEntity<Object> statsServerResponse;

        if (uris != null) {
            parameters = Map.of(
                    "start", startTime,
                    "end", endTime,
                    "uris", uris,
                    "unique", unique.toString());

            statsServerResponse = makeAndSendRequest(HttpMethod.GET, GET_STATS_PATH, parameters, null);
        } else {
            parameters = Map.of(
                    "start", startTime,
                    "end", endTime,
                    "unique", unique.toString());

            statsServerResponse = makeAndSendRequest(HttpMethod.GET, GET_STATS_PATH_WITHOUT_URIS, parameters, null);
        }
        return objectMapper.convertValue(statsServerResponse.getBody(), new TypeReference<List<ViewStatsResponseDto>>(){});


    }

    private ResponseEntity<Object> makeAndSendRequest(HttpMethod method,
                                                      String path,
                                                      @Nullable Map<String, Object> parameters,
                                                      @Nullable HitRequestDto body) {
        HttpEntity<HitRequestDto> requestEntity = new HttpEntity<>(body, defaultHeaders());

        ResponseEntity<Object> statsServerResponse;
        try {
            if (parameters != null) {
                statsServerResponse = rest.exchange(path, method, requestEntity, Object.class, parameters);
            } else {
                statsServerResponse = rest.exchange(path, method, requestEntity, Object.class);
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }

        return prepareGatewayResponse(statsServerResponse);
    }

    private static ResponseEntity<Object> prepareGatewayResponse(ResponseEntity<Object> response) {

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        return headers;
    }
}
