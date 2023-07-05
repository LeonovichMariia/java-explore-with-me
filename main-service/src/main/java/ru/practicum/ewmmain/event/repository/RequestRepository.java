package ru.practicum.ewmmain.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmmain.event.enums.RequestStatus;
import ru.practicum.ewmmain.event.model.Request;
import ru.practicum.ewmmain.exception.NotFoundException;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    default Request validateRequest(Long requestId) {
        return findById(requestId).orElseThrow(() -> new NotFoundException(
                "Запрос с id " + requestId + " не найден"));
    }

    Boolean existsByRequesterIdAndEventId(Long userId, Long eventId);

    Integer countByEventIdAndStatus(Long eventId, RequestStatus status);

    List<Request> findAllByRequesterId(Long userId);

    List<Request> findAllByEvent_InitiatorIdAndEventId(Long userId, Long eventId);

    List<Request> findAllByEvent_IdAndIdNotInAndStatus(Long id, List<Long> listId, RequestStatus status);

    List<Request> findByEventIdIn(List<Long> events);

    Integer countByEvent_IdAndStatus(Long eventId, RequestStatus status);
}
