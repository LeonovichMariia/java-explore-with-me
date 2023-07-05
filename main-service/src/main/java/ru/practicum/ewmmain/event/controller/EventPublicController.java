package ru.practicum.ewmmain.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.Constants;
import ru.practicum.ewmmain.event.dto.EventFullDto;
import ru.practicum.ewmmain.event.dto.EventShortDto;
import ru.practicum.ewmmain.event.enums.EventSort;
import ru.practicum.ewmmain.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/events")
public class EventPublicController {
    private final EventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getEventsPublic(@RequestParam(required = false) String text,
                                               @RequestParam(required = false) List<Long> categories,
                                               @RequestParam(required = false) Boolean paid,
                                               @RequestParam(required = false) @DateTimeFormat(pattern = Constants.pattern) LocalDateTime rangeStart,
                                               @RequestParam(required = false) @DateTimeFormat(pattern = Constants.pattern) LocalDateTime rangeEnd,
                                               @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                               @RequestParam(required = false) EventSort sort,
                                               @RequestParam(name = "size", defaultValue = "10") @Positive Integer size,
                                               @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                               HttpServletRequest httpServletRequest) {
        log.info("Запрос на получение событий from={}, size={}", from, size);
        List<EventShortDto> eventShortDtos = eventService.getEventsPublic(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, String.valueOf(sort), from, size, httpServletRequest);
        return eventShortDtos;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEventByIdPublic(@PathVariable Long id, HttpServletRequest httpServletRequest) {
        log.info("Запрос на получение подробной информации о событии с id {}", id);
        EventFullDto eventFullDto = eventService.getEventByIdPublic(id, httpServletRequest);
        return eventFullDto;
    }
}
