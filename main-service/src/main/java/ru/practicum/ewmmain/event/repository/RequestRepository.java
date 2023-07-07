package ru.practicum.ewmmain.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmmain.event.enums.RequestStatus;
import ru.practicum.ewmmain.event.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {

    Optional<Request> findRequestById(Long requestId);

    Boolean existsByRequesterIdAndEventId(Long userId, Long eventId);

    Integer countByEventIdAndStatus(Long eventId, RequestStatus status);

    List<Request> findAllByRequesterId(Long userId);

    List<Request> findAllByEvent_InitiatorIdAndEventId(Long userId, Long eventId);

    List<Request> findAllByEvent_IdAndIdNotInAndStatus(Long id, List<Long> listId, RequestStatus status);

    List<Request> findByEventIdIn(List<Long> events);

    Integer countByEvent_IdAndStatus(Long eventId, RequestStatus status);
}
