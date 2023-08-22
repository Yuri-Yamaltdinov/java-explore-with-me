package ru.practicum.ewm.main.service.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.main.service.event.model.Event;
import ru.practicum.ewm.main.service.event.model.State;
import ru.practicum.ewm.main.service.user.model.User;
import ru.practicum.ewm.main.service.util.Pagination;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByInitiator(User user, Pagination page);

    List<Event> findByIdIn(List<Long> ids);

    @Query(value = "SELECT e " +
            "FROM Event AS e " +
            "WHERE (:categories IS NULL OR e.category.id IN :categories) " +
            "AND (e.paid = :paid) " +
            "AND (e.state = 'PUBLISHED') "  +
            "OR (LOWER(e.annotation) LIKE LOWER(CONCAT('%', :text, '%'))) " +
            "OR (LOWER(e.description) LIKE LOWER(CONCAT('%', :text, '%'))) " +
            "OR (LOWER(e.title) LIKE LOWER(CONCAT('%', :text, '%'))) " +
            "OR (CAST(:rangeStart AS date) IS NULL AND CAST(:rangeStart AS date) IS NULL)" +
            "OR (CAST(:rangeStart AS date) IS NULL AND e.eventDate < CAST(:rangeEnd AS date)) " +
            "OR (CAST(:rangeEnd AS date) IS NULL AND e.eventDate > CAST(:rangeStart AS date)) " +
            "AND (e.confirmedRequests < e.participantLimit OR :onlyAvailable = FALSE)" +
            "GROUP BY e.id ")
    List<Event> findAllByParamsPublic(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, Pagination page);

    @Query(value = "SELECT e " +
            "FROM Event AS e " +
            "WHERE (:users IS NULL OR e.initiator.id IN :users) " +
            "AND (:states IS NULL OR e.state IN :states) " +
            "AND (:categories IS NULL OR e.category.id IN :categories) " +
            "OR (CAST(:rangeStart AS date) IS NULL AND CAST(:rangeStart AS date) IS NULL)" +
            "OR (CAST(:rangeStart AS date) IS NULL AND e.eventDate < CAST(:rangeEnd AS date)) " +
            "OR (CAST(:rangeEnd AS date) IS NULL AND e.eventDate > CAST(:rangeStart AS date)) " +
            "GROUP BY e.id " +
            "ORDER BY e.id ASC")
    List<Event> findAllByParamsAdmin(List<Long> users, List<State> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pagination page);

    List<Event> findAllByCategoryId(Long categoryId);
}
