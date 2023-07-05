package ru.practicum.ewmmain.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.Constants;
import ru.practicum.ewmmain.event.dto.EventFullDto;
import ru.practicum.ewmmain.event.dto.UpdateEventAdminRequest;
import ru.practicum.ewmmain.event.enums.EventState;
import ru.practicum.ewmmain.event.service.EventService;
import ru.practicum.ewmmain.utils.EventRequestsParams;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/events")
public class EventAdminController {
    private final EventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> getEventsFullByAdmin(@RequestParam(required = false) List<Long> users,
                                                   @RequestParam(required = false) List<EventState> states,
                                                   @RequestParam(required = false) List<Long> categories,
                                                   @RequestParam(required = false) @DateTimeFormat(pattern = Constants.pattern) LocalDateTime rangeStart,
                                                   @RequestParam(required = false) @DateTimeFormat(pattern = Constants.pattern) LocalDateTime rangeEnd,
                                                   @RequestParam(name = "size", defaultValue = "10") @Positive Integer size,
                                                   @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from) {
        EventRequestsParams eventRequestsParams = EventRequestsParams.builder()
                .users(users)
                .states(states)
                .categories(categories)
                .rangeEnd(rangeStart)
                .rangeEnd(rangeEnd)
                .build();
        log.info("Запрос на получение списка событий для пользователей с id {}, from={}, size={}", users, from, size);
        return eventService.getEventsFullAdmin(eventRequestsParams, from, size);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto renewalEventByAdmin(@RequestBody @Valid UpdateEventAdminRequest updateEventAdminRequest,
                                            @PathVariable Long eventId) {
        log.info("Запрос на обновление события {}", updateEventAdminRequest);
        return eventService.renewalEventAdmin(updateEventAdminRequest, eventId);
    }
}
