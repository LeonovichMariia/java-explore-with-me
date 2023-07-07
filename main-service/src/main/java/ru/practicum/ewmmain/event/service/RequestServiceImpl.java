package ru.practicum.ewmmain.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmmain.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewmmain.event.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewmmain.event.dto.ParticipationRequestDto;
import ru.practicum.ewmmain.event.enums.EventState;
import ru.practicum.ewmmain.event.enums.RequestStatus;
import ru.practicum.ewmmain.event.mapper.RequestMapper;
import ru.practicum.ewmmain.event.model.Event;
import ru.practicum.ewmmain.event.model.Request;
import ru.practicum.ewmmain.event.repository.EventRepository;
import ru.practicum.ewmmain.event.repository.RequestRepository;
import ru.practicum.ewmmain.exception.*;
import ru.practicum.ewmmain.user.model.User;
import ru.practicum.ewmmain.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

    @Override
    public ParticipationRequestDto addEventRequestPrivate(Long userId, Long eventId) {
        User user = validateUser(userId);
        Event event = validateEvent(eventId);
        if (requestRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            log.error("Запрос уже существует");
            throw new AlreadyExistException("Запрос уже существует");
        }
        if (event.getInitiator().getId().equals(userId)) {
            log.error("Инициатор события не может добавить запрос на участие в своём событии");
            throw new WrongUserException("Инициатор события не может добавить запрос на участие в своём событии");
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            log.error("Данное событие еще не опубликовано");
            throw new EventIsNotPublishedException("Данное событие еще не опубликовано");
        }
        Integer confirmedRequests = requestRepository.countByEventIdAndStatus(event.getId(), RequestStatus.CONFIRMED);
        if (!event.getRequestModeration() && confirmedRequests >= event.getParticipantLimit()) {
            log.error("Превышен лимит возможных участников");
            throw new ParticipantLimitException("Превышен лимит возможных участников");
        }
        RequestStatus requestStatus = (!event.getRequestModeration() || event.getParticipantLimit() == 0)
                ? RequestStatus.CONFIRMED : RequestStatus.PENDING;
        Request request = Request.builder()
                .created(LocalDateTime.now())
                .requester(user)
                .event(event)
                .status(requestStatus)
                .build();
        ParticipationRequestDto savedRequest = RequestMapper.toParticipationRequestDto(requestRepository.save(request));
        log.info("Запрос {} сохранен", savedRequest);
        return savedRequest;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getEventRequestsPrivate(Long userId) {
        log.info("Получение информации о заявках от пользователя с id {} в чужих событиях", userId);
        validateUser(userId);
        List<Request> requestList = requestRepository.findAllByRequesterId(userId);
        return requestList.stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto cancelEventRequest(Long userId, Long requestId) {
        userRepository.findUserById(userId).orElseThrow(() -> new NotFoundException(
                "Пользователь с id " + userId + " не найден"));
        Request request = validateRequest(requestId);
        request.setStatus(RequestStatus.CANCELED);
        ParticipationRequestDto cancelledRequest = RequestMapper.toParticipationRequestDto(requestRepository.save(request));
        log.info("Запрос {} отменен", cancelledRequest);
        return cancelledRequest;
    }

    @Override
    public EventRequestStatusUpdateResult renewalEventRequest(EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest, Long userId, Long eventId) {
        validateUser(userId);
        Event event = validateEvent(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            log.error("Только организатор может менять статус запроса");
            throw new WrongUserException("Только организатор может менять статус запроса");
        }
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            log.error("Лимит заявок равен 0 или отключена пре-модерация заявок");
            throw new ParticipantLimitException("Лимит заявок равен 0 или отключена пре-модерация заявок");
        }
        List<Long> requestIds = eventRequestStatusUpdateRequest.getRequestIds();
        switch (eventRequestStatusUpdateRequest.getStatus()) {
            case REJECTED:
                return addStatusRejected(requestIds);
            case CONFIRMED:
                return addStatusConfirmed(requestIds, event);
            default:
                log.error("Указан неверный статус");
                throw new ConflictException("Указан неверный статус");
        }
    }

    private EventRequestStatusUpdateResult addStatusRejected(List<Long> requestIds) {
        List<Request> requests = requestRepository.findAllById(requestIds);
        checkStatusPending(requests);
        requests.forEach(r -> r.setStatus(RequestStatus.REJECTED));
        requestRepository.saveAll(requests);
        List<ParticipationRequestDto> rejectedRequests = requests
                .stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
        return new EventRequestStatusUpdateResult(List.of(), rejectedRequests);
    }

    private EventRequestStatusUpdateResult addStatusConfirmed(List<Long> requestsIds, Event event) {
        int limit = event.getParticipantLimit();
        int confirmedReq = event.getConfirmedRequests();
        if (limit > 0 && confirmedReq == limit) {
            log.error("Достигнут лимит по заявкам на данное событие");
            throw new ParticipantLimitException("Достигнут лимит по заявкам на данное событие");
        }
        List<Request> confirmedRequests;
        if (requestsIds.size() > (limit - confirmedReq)) {
            confirmedRequests = requestRepository.findAllById(requestsIds.stream()
                    .limit(limit - confirmedReq)
                    .collect(Collectors.toList()));
        } else {
            confirmedRequests = requestRepository.findAllById(requestsIds);
        }
        checkStatusPending(confirmedRequests);
        for (Request req : confirmedRequests) {
            req.setStatus(RequestStatus.CONFIRMED);
            confirmedReq++;
        }
        List<Request> rejectedRequests = new ArrayList<>();
        List<Long> listId = confirmedRequests.stream().map(Request::getId).collect(Collectors.toList());
        if (limit == confirmedReq) {
            rejectedRequests = requestRepository.findAllByEvent_IdAndIdNotInAndStatus(event.getId(), listId, RequestStatus.PENDING)
                    .stream()
                    .peek(req -> req.setStatus(RequestStatus.REJECTED))
                    .collect(Collectors.toList());
        }
        List<Request> updateRequests = new ArrayList<>(confirmedRequests);
        updateRequests.addAll(rejectedRequests);
        requestRepository.saveAll(updateRequests);
        event.setConfirmedRequests(confirmedReq);
        eventRepository.save(event);
        return new EventRequestStatusUpdateResult(
                confirmedRequests.stream()
                        .map(RequestMapper::toParticipationRequestDto)
                        .collect(Collectors.toList()),
                rejectedRequests.stream()
                        .map(RequestMapper::toParticipationRequestDto)
                        .collect(Collectors.toList()));
    }

    private void checkStatusPending(List<Request> requests) {
        boolean isConfirmedRequest = requests.stream()
                .anyMatch(r -> !r.getStatus().equals(RequestStatus.PENDING));
        if (isConfirmedRequest) {
            log.error("Статус можно изменить только у заявок со статусом PENDING");
            throw new ConflictException("Статус можно изменить только у заявок со статусом PENDING");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId) {
        log.info("Получение информации о запросах на участие в событии с id {} пользователя с id {}", eventId, userId);
        validateUser(userId);
        validateEvent(eventId);
        List<Request> requestList = requestRepository.findAllByEvent_InitiatorIdAndEventId(userId, eventId);
        return requestList.stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    private User validateUser(Long userId) {
        return userRepository.findUserById(userId).orElseThrow(() -> new NotFoundException(
                "Пользователь с id " + userId + " не найден"));
    }

    private Event validateEvent(Long eventId) {
        return eventRepository.findEventById(eventId).orElseThrow(() -> new NotFoundException(
                "Событие с id " + eventId + " не найдено"));
    }

    private Request validateRequest(Long requestId) {
        return requestRepository.findRequestById(requestId).orElseThrow(() -> new NotFoundException(
                "Запрос с id " + requestId + " не найден"));
    }
}
