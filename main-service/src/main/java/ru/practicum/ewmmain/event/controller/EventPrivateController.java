package ru.practicum.ewmmain.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmmain.event.dto.*;
import ru.practicum.ewmmain.event.service.EventService;
import ru.practicum.ewmmain.event.service.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/events")
public class EventPrivateController {
    private final EventService eventService;
    private final RequestService requestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEventPrivate(@RequestBody @Valid NewEventDto newEventDto, @PathVariable Long userId) {
        log.info("Запрос на добавление нового события {} от пользоателя с id {}", newEventDto, userId);
        return eventService.addEventPrivate(newEventDto, userId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto renewalEventPrivate(@RequestBody @Valid UpdateEventUserRequest updateEventUserRequest,
                                            @PathVariable Long userId,
                                            @PathVariable Long eventId) {
        log.info("Запрос на обновление события с id {} от пользоателя с id {}", eventId, userId);
        return eventService.renewalEventPrivate(updateEventUserRequest, userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult renewalEventRequest(@RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest,
                                                              @PathVariable Long userId,
                                                              @PathVariable Long eventId) {
        log.info("Запрос на изменение статуса заявки на участие с id {} от пользоателя с id {}", eventId, userId);
        return requestService.renewalEventRequest(eventRequestStatusUpdateRequest, userId, eventId);
    }

    @GetMapping
    public List<EventShortDto> getEventsPrivate(@PathVariable Long userId,
                                                @RequestParam(name = "size", defaultValue = "10") @Positive Integer size,
                                                @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from) {
        log.info("Запрос на получение событий, добавленных пользователем с id {}, from={}, size={}", userId, from, size);
        return eventService.getEventsPrivate(userId, size, from);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventByIdPrivate(@PathVariable Long userId,@PathVariable Long eventId) {
        log.info("Запрос на получение информации о событии с id {} от пользователя с id {}", eventId, userId);
        return eventService.getEventByIdPrivate(userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getEventRequests(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Запрос на получение информации о запросах на участие в событии с id {} от пользователя с id {}", eventId, userId);
        return requestService.getEventRequests(userId, eventId);
    }
}
