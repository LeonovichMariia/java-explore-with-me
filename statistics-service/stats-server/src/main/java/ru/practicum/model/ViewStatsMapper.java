package ru.practicum.model;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.ViewStatsDto;

@UtilityClass
public class ViewStatsMapper {

    public ViewStatsDto toViewStatsDto(ViewStats viewStats) {
        return ViewStatsDto.builder()
                .app(viewStats.getApp())
                .uri(viewStats.getUri())
                .hits(viewStats.getHits())
                .build();
    }

    public ViewStats toViewStats(ViewStatsDto viewStatsDto) {
        return ViewStats.builder()
                .app(viewStatsDto.getApp())
                .uri(viewStatsDto.getUri())
                .hits(viewStatsDto.getHits())
                .build();
    }
}