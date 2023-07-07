package ru.practicum.model;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.EndpointHitDto;

@UtilityClass
public class EndpointHitMapper {

    public EndpointHit fromEndpointHitDto(EndpointHitDto endpointHitDto) {
        return EndpointHit.builder()
                .ip(endpointHitDto.getIp())
                .uri(endpointHitDto.getUri())
                .app(endpointHitDto.getApp())
                .created(endpointHitDto.getTimestamp())
                .build();
    }
}