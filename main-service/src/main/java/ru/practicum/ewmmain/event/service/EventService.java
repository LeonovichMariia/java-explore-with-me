package ru.practicum.ewmmain.event.service;

import ru.practicum.ewmmain.event.dto.*;
import ru.practicum.ewmmain.utils.EventRequestsParams;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    List<EventFullDto> getEventsFullAdmin(EventRequestsParams parameters, Integer from, Integer size);

    EventFullDto renewalEventAdmin(UpdateEventAdminRequest updateEventAdminRequest, Long eventId);

    EventFullDto addEventPrivate(NewEventDto newEventDto, Long userId);

    EventFullDto renewalEventPrivate(UpdateEventUserRequest updateEventUserRequest, Long userId, Long eventId);

    List<EventShortDto> getEventsPrivate(Long userId, Integer size, Integer from);

    EventFullDto getEventByIdPrivate(Long userId, Long eventId);

    List<EventShortDto> getEventsPublic(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                        LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, Integer from,
                                        Integer size, HttpServletRequest request);

    EventFullDto getEventByIdPublic(Long id, HttpServletRequest httpServletRequest);
}
