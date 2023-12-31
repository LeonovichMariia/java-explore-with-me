package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.Constants;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.service.StatsService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void addHit(@Valid @RequestBody EndpointHitDto endpointHitDto) {
        log.info("Запрос на сохранение информации о запросе: {}", endpointHitDto);
        statsService.addHit(endpointHitDto);
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> getStats(@RequestParam(name = "start") @DateTimeFormat(pattern = Constants.pattern) LocalDateTime start,
                                       @RequestParam(name = "end") @DateTimeFormat(pattern = Constants.pattern) LocalDateTime end,
                                       @RequestParam(name = "uris", required = false) List<String> uris,
                                       @RequestParam(name = "unique", defaultValue = "false", required = false) Boolean unique) {
        log.info("Запрос на получение статистики по посещениям с {} по {}", start, end);
        return statsService.getStats(start, end, uris, unique);
    }
}