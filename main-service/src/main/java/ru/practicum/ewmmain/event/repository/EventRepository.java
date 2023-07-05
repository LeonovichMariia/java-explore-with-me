package ru.practicum.ewmmain.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmmain.event.enums.EventState;
import ru.practicum.ewmmain.event.model.Event;
import ru.practicum.ewmmain.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {

    default Event validateEvent(Long eventId) {
        return findById(eventId).orElseThrow(() -> new NotFoundException(
                "Событие с id " + eventId + " не найдено"));
    }

    List<Event> findAllByIdIn(List<Long> eventId);

    List<Event> findByCategoryId(Long catId);

    Optional<Event> findByIdAndState(Long id, EventState eventState);

    Page<Event> findAllByInitiatorId(Long userId, Pageable pageable);

    Event findByIdAndInitiatorId(Long eventId, Long userId);

    @Query("SELECT e FROM Event e " +
            "WHERE (UPPER(e.annotation) LIKE UPPER(CONCAT('%',:text,'%')) " +
            "OR UPPER(e.description) LIKE UPPER(CONCAT('%',:text,'%')) OR :text IS NULL) " +
            "AND (:categories IS NULL OR e.category.id IN (:categories)) " +
            "AND (:paid IS NULL OR e.paid = :paid) " +
            "and (CAST(:rangeStart AS timestamp) IS NULL OR e.eventDate >= :rangeStart) " +
            "and (CAST(:rangeEnd AS timestamp) IS NULL OR e.eventDate <= :rangeEnd)")
    List<Event> getEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);
}
