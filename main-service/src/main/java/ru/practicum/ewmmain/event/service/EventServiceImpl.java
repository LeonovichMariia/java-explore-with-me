package ru.practicum.ewmmain.event.service;

import com.querydsl.core.BooleanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.clent.StatsClient;
import ru.practicum.dto.Constants;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.ewmmain.category.model.Category;
import ru.practicum.ewmmain.category.repository.CategoryRepository;
import ru.practicum.ewmmain.event.dto.*;
import ru.practicum.ewmmain.event.enums.EventState;
import ru.practicum.ewmmain.event.mapper.EventMapper;
import ru.practicum.ewmmain.event.model.Event;
import ru.practicum.ewmmain.event.model.QEvent;
import ru.practicum.ewmmain.event.repository.EventRepository;
import ru.practicum.ewmmain.event.repository.RequestRepository;
import ru.practicum.ewmmain.exception.ConflictException;
import ru.practicum.ewmmain.exception.NotFoundException;
import ru.practicum.ewmmain.exception.ValidationException;
import ru.practicum.ewmmain.exception.WrongUserException;
import ru.practicum.ewmmain.user.model.User;
import ru.practicum.ewmmain.user.repository.UserRepository;
import ru.practicum.ewmmain.utils.EventRequestsParams;
import ru.practicum.ewmmain.utils.PageSetup;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EventServiceImpl implements EventService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final StatsClient client;

    public EventServiceImpl(UserRepository userRepository, CategoryRepository categoryRepository,
                            EventRepository eventRepository, RequestRepository requestRepository,
                            @Value("${STATISTICS_SERVICE_URL}") String serverUrl) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.eventRepository = eventRepository;
        this.client = new StatsClient(serverUrl);
        this.requestRepository = requestRepository;
    }

    @Override
    public List<EventFullDto> getEventsFullAdmin(EventRequestsParams parameters, Integer from, Integer size) {
        log.info("Поиск событий по параметрам");
        PageRequest pageRequest = PageRequest.of(from, size);
        BooleanBuilder predicate = getAdminPredicate(parameters);
        List<Event> events = eventRepository.findAll(predicate, pageRequest).getContent();
        return events.stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto renewalEventAdmin(UpdateEventAdminRequest updateEventAdminRequest, Long eventId) {
        Event event = eventRepository.validateEvent(eventId);
        if (updateEventAdminRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventAdminRequest.getAnnotation());
        }
        if (updateEventAdminRequest.getDescription() != null) {
            event.setDescription(updateEventAdminRequest.getDescription());
        }
        if (updateEventAdminRequest.getCategory() != null) {
            Category category = categoryRepository.validateCategory(updateEventAdminRequest.getCategory());
            event.setCategory(category);
        }
        if (updateEventAdminRequest.getEventDate() != null) {
            event.setEventDate(updateEventAdminRequest.getEventDate());
        }
        if (updateEventAdminRequest.getEventDate() != null && updateEventAdminRequest.getEventDate().isBefore(LocalDateTime.now())) {
            log.error("Невозможно измененить дату события на уже наступившую");
            throw new ValidationException("Невозможно измененить дату события на уже наступившую");
        }
        if (updateEventAdminRequest.getPaid() != null) {
            event.setPaid(updateEventAdminRequest.getPaid());
        }
        if (updateEventAdminRequest.getLocation() != null) {
            event.setLat(updateEventAdminRequest.getLocation().getLat());
            event.setLon(updateEventAdminRequest.getLocation().getLon());
        }
        if (updateEventAdminRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        }
        if (updateEventAdminRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventAdminRequest.getRequestModeration());
        }
        if (updateEventAdminRequest.getTitle() != null) {
            event.setTitle(updateEventAdminRequest.getTitle());
        }
        if (updateEventAdminRequest.getStateAction() != null) {
            switch (updateEventAdminRequest.getStateAction()) {
                case REJECT_EVENT:
                    if (event.getState().equals(EventState.PUBLISHED)) {
                        log.error("Нельзя отменить уже опубликованное событие");
                        throw new ConflictException("Нельзя отменить уже опубликованное событие");
                    }
                    event.setState(EventState.CANCELED);
                    break;
                case PUBLISH_EVENT:
                    if (event.getState() != EventState.PENDING) {
                        log.error("Событие должно иметь статус PENDING");
                        throw new ConflictException("Событие должно иметь статус PENDING");
                    }
                    event.setState(EventState.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                    break;
            }
        }
        EventFullDto eventFullDto = EventMapper.toEventFullDto(eventRepository.save(event));
        log.info("Событие с id {} изменено администратором", eventId);
        return eventFullDto;
    }

    @Override
    @Transactional
    public EventFullDto addEventPrivate(NewEventDto newEventDto, Long userId) {
        if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            log.error("Событие не может начаться раньше, чем через 2 часа от текущего времени");
            throw new ValidationException("Событие не может начаться раньше, чем через 2 часа от текущего времени");
        }
        User user = userRepository.validateUser(userId);
        Category category = categoryRepository.validateCategory(newEventDto.getCategory());
        Event event = EventMapper.toEvent(newEventDto, category, user);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(eventRepository.save(event));
        log.info("Событие с id {} сохранено", eventFullDto.getId());
        return eventFullDto;
    }

    @Override
    @Transactional
    public EventFullDto renewalEventPrivate(UpdateEventUserRequest updateEventUserRequest, Long userId, Long eventId) {
        userRepository.validateUser(userId);
        Event event = eventRepository.validateEvent(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            log.error("Только организатор может менять двнные события запроса");
            throw new WrongUserException("Только организатор может менять данные события");
        }
        if (updateEventUserRequest.getEventDate() != null) {
            LocalDateTime updatedEvenDate = updateEventUserRequest.getEventDate();
            if (updatedEvenDate.isBefore(LocalDateTime.now().plusHours(2L))) {
                log.error("Событие не может начаться раньше, чем через 2 часа от текущего времени");
                throw new ValidationException("Событие не может начаться раньше, чем через 2 часа от текущего времени");
            }
            event.setEventDate(updatedEvenDate);
        }
        if (event.getState().equals(EventState.PUBLISHED)) {
            log.error("Нельзя изменить опубликованное событие");
            throw new ConflictException("Нельзя изменить опубликованное событие");
        }
        if (updateEventUserRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventUserRequest.getAnnotation());
        }
        if (updateEventUserRequest.getCategory() != null) {
            Category category = categoryRepository.validateCategory(updateEventUserRequest.getCategory());
            event.setCategory(category);
        }
        if (updateEventUserRequest.getDescription() != null) {
            event.setDescription(updateEventUserRequest.getDescription());
        }
        if (updateEventUserRequest.getLocation() != null) {
            event.setLat(updateEventUserRequest.getLocation().getLat());
            event.setLon(updateEventUserRequest.getLocation().getLon());
        }
        if (updateEventUserRequest.getPaid() != null) {
            event.setPaid(updateEventUserRequest.getPaid());
        }
        if (updateEventUserRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventUserRequest.getParticipantLimit());
        }
        if (updateEventUserRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventUserRequest.getRequestModeration());
        }
        if (updateEventUserRequest.getTitle() != null) {
            event.setTitle(updateEventUserRequest.getTitle());
        }
        if (updateEventUserRequest.getStateAction() != null) {
            switch (updateEventUserRequest.getStateAction()) {
                case CANCEL_REVIEW:
                    event.setState(EventState.CANCELED);
                    break;
                case SEND_TO_REVIEW:
                    event.setState(EventState.PENDING);
                    break;
            }
        }
        EventFullDto eventFullDto = EventMapper.toEventFullDto(eventRepository.save(event));
        log.info("Событие с id {} изменено пользователем с id {}", eventId, userId);
        return eventFullDto;
    }

    @Override
    public List<EventShortDto> getEventsPrivate(Long userId, Integer size, Integer from) {
        log.info("Получение событий, добавленных пользователем с id {}, from={}, size={}", userId, from, size);
        userRepository.validateUser(userId);
        PageRequest pageRequest = new PageSetup(from, size, Sort.unsorted());
        List<Event> events = eventRepository.findAllByInitiatorId(userId, pageRequest).getContent();
        return events.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEventByIdPrivate(Long userId, Long eventId) {
        log.info("Получение полной информации о событии с id {}, добавленных пользователем с id {}", eventId, userId);
        userRepository.validateUser(userId);
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        return eventFullDto;
    }

    @Override
    public List<EventShortDto> getEventsPublic(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                               LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, Integer from,
                                               Integer size, HttpServletRequest request) {
        log.info("Получение событий с возможностью фильтрации");
        LocalDateTime now = LocalDateTime.now();
        if (rangeStart == null) {
            rangeStart = now.minusYears(10);
        }
        if (rangeEnd == null) {
            rangeEnd = now.plusYears(5);
        }
        if (rangeStart.isAfter(rangeEnd)) {
            log.error("Дата начала должна быть раньше даты окончания");
            throw new ValidationException("Дата начала должна быть раньше даты окончания");
        }
        String sorting;
        if (sort.equals("EVENT_DATE")) {
            sorting = "eventDate";
        } else if (sort.equals("VIEWS")) {
            sorting = "views";
        } else {
            sorting = "id";
        }
        Pageable pageable = PageRequest.of(from / size, size, Sort.by(sorting));
        List<Event> sortedEvents = eventRepository.getEvents(text, categories, paid, rangeStart, rangeEnd, pageable);
        if (onlyAvailable) {
            sortedEvents.removeIf(event -> event.getParticipantLimit().equals(event.getConfirmedRequests()));
        }
        if (sortedEvents.isEmpty()) {
            return List.of();
        }
        String uri = request.getRequestURI();
        LocalDateTime startDate = sortedEvents.stream().map(Event::getCreatedOn).min(Comparator.naturalOrder()).orElse(now);
        addHit(request);
        Long viewsBefore = getViews(uri, startDate, now);
        sortedEvents.forEach(e -> e.setViews(viewsBefore));
        List<EventShortDto> eventShortDtos = sortedEvents.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
        return eventShortDtos;
    }

    @Override
    public EventFullDto getEventByIdPublic(Long id, HttpServletRequest httpServletRequest) {
        log.info("Получение полной информации о событии с id {}", id);
        Event event = eventRepository.validateEvent(id);
        if (!event.getState().equals(EventState.PUBLISHED)) {
            log.error("Событие не найдено");
            throw new NotFoundException("Событие не найдено");
        }
        String uri = httpServletRequest.getRequestURI();
        addHit(httpServletRequest);
        Long views = getViews(uri, event.getCreatedOn(), LocalDateTime.now());
        event.setViews(views);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        return eventFullDto;
    }

    private long getViews(String uri, LocalDateTime from, LocalDateTime to) {
        return Optional.ofNullable(client.getStats(from.format(Constants.formatter), to.format(Constants.formatter), List.of(uri), true))
                .map(ResponseEntity::getBody)
                .stream()
                .flatMap(Collection::stream)
                .filter(v -> v.getUri().equals(uri))
                .mapToLong(ViewStatsDto::getHits)
                .sum();
    }

    private BooleanBuilder getAdminPredicate(EventRequestsParams parameters) {
        BooleanBuilder predicate = new BooleanBuilder();
        List<Long> users = parameters.getUsers();
        List<EventState> states = parameters.getStates();
        List<Long> categories = parameters.getCategories();
        LocalDateTime rangeStart = parameters.getRangeStart();
        LocalDateTime rangeEnd = parameters.getRangeEnd();
        if (users == null) {
            predicate.and(QEvent.event.initiator.id.notIn(new ArrayList<>()));
        } else {
            predicate.and(QEvent.event.initiator.id.in(users));
        }
        if (states != null) {
            predicate.and(QEvent.event.state.in(states));
        }
        if (categories != null) {
            predicate.and(QEvent.event.category.id.in(categories));
        }
        if (rangeStart != null && rangeEnd != null) {
            predicate.and(QEvent.event.eventDate.between(rangeStart, rangeEnd));
        }
        return predicate;
    }

    private void addHit(HttpServletRequest httpServletRequest) {
        EndpointHitDto endpointHitDto = EndpointHitDto.builder()
                .app("main-service")
                .ip(httpServletRequest.getRemoteAddr())
                .uri(httpServletRequest.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        client.addHit(endpointHitDto);
    }
}
