package ru.practicum.ewm.main.service.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.main.service.event.model.EventRateView;
import ru.practicum.ewm.main.service.event.model.EventUserRate;
import ru.practicum.ewm.main.service.event.model.InitiatorRateView;
import ru.practicum.ewm.main.service.util.Pagination;

import java.util.List;
import java.util.Optional;

public interface RateRepository extends JpaRepository<EventUserRate, Long> {

    @Query(value = "SELECT event.id as eventId, event.title as title, SUM(e.rate) AS ratesSum " +
            "FROM EventUserRate e " +
            "JOIN e.event event " +
            "WHERE e.event.id = :eventId " +
            "GROUP BY event.id " +
            "ORDER BY ratesSum DESC")
    Optional<EventRateView> getEventRateView(Long eventId);

    @Query(value = "SELECT event.id as eventId, event.title as title, SUM(e.rate) AS ratesSum " +
            "FROM EventUserRate e " +
            "JOIN e.event event " +
            "GROUP BY eventId " +
            "ORDER BY ratesSum DESC")
    List<EventRateView> getAllEventsRateViews(Pagination page);

    @Query(value = "SELECT event.initiator.id as initiatorId, event.initiator.name as name, SUM(e.rate) AS ratesSum " +
            "FROM EventUserRate e " +
            "JOIN e.event event " +
            "GROUP BY initiatorId, name " +
            "ORDER BY ratesSum DESC")
    List<InitiatorRateView> getAllUsersRateViews(Pagination page);

    boolean existsByUserIdAndEventId(Long userId, Long eventId);

}
