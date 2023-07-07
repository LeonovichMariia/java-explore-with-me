package ru.practicum.ewmmain.event.service;

import ru.practicum.ewmmain.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewmmain.event.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewmmain.event.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {

    ParticipationRequestDto addEventRequestPrivate(Long userId, Long eventId);

    List<ParticipationRequestDto> getEventRequestsPrivate(Long userId);

    ParticipationRequestDto cancelEventRequest(Long userId, Long requestId);

    EventRequestStatusUpdateResult renewalEventRequest(EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest, Long userId, Long eventId);

    List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId);
}
