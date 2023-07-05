package ru.practicum.ewmmain.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmmain.event.dto.ParticipationRequestDto;
import ru.practicum.ewmmain.event.service.RequestService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/requests")
public class RequestPrivateController {
    private final RequestService requestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addEventRequestPrivate(@PathVariable Long userId, @RequestParam Long eventId) {
        log.info("Запрос на добавление запроса от пользователя с id {} на участие в событии с id {}", userId, eventId);
        return requestService.addEventRequestPrivate(userId, eventId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getEventRequestsPrivate(@PathVariable Long userId) {
        log.info("Запрос на получение информации от пользователя с id {} на участие в чужих событиях", userId);
        return requestService.getEventRequestsPrivate(userId);
    }

    @PatchMapping("/{requestId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public ParticipationRequestDto cancelEventRequest(@PathVariable Long userId, @PathVariable Long requestId) {
        log.info("Запрос на отмену запроса с id {} от пользователя с id {} на участие в событии ", requestId, userId);
        return requestService.cancelEventRequest(userId, requestId);
    }
}
