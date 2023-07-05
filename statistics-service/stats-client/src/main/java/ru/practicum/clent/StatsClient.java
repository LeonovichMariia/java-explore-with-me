package ru.practicum.clent;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;

import java.util.List;

@Service
public class StatsClient {
    private final WebClient client;

    public StatsClient(@Value("${STATISTICS_SERVICE_URL}") String serverUrl) {
        this.client = WebClient.builder()
                .baseUrl(serverUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public void addHit(EndpointHitDto endpointHitDto) {
        client.post()
                .uri("/hit")
                .bodyValue(endpointHitDto)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public ResponseEntity<List<ViewStatsDto>> getStats(String start, String end, List<String> uris, Boolean unique) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/stats")
                        .queryParam("start", start)
                        .queryParam("end", end)
                        .queryParam("uris", uris)
                        .queryParam("unique", unique)
                        .build())
                .retrieve()
                .toEntityList(ViewStatsDto.class)
                .block();
    }
}