package ru.practicum.ewm.stats.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.stats.service.model.Hit;
import ru.practicum.ewm.stats.service.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<Hit, Long> {

    @Query("SELECT new ru.practicum.ewm.stats.service.model.ViewStats(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
            "FROM Hit AS h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "GROUP BY h.uri, h.app " +
            "ORDER BY COUNT(h.ip) DESC")
    List<ViewStats> getAllUrisWithUniqueIP(@Param("start") LocalDateTime start,
                                           @Param("end") LocalDateTime end);

    @Query("SELECT new ru.practicum.ewm.stats.service.model.ViewStats(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
            "FROM Hit AS h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "AND h.uri in :uris " +
            "GROUP BY h.uri, h.app " +
            "ORDER BY COUNT(h.ip) DESC")
    List<ViewStats> getUrisWithUniqueIP(@Param("start") LocalDateTime start,
                                   @Param("end") LocalDateTime end,
                                   @Param("uris") List<String> uris);

    @Query("SELECT new ru.practicum.ewm.stats.service.model.ViewStats(h.app, h.uri, COUNT(h.id)) " +
            "FROM Hit AS h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "AND h.uri in :uris " +
            "GROUP BY h.uri, h.app " +
            "ORDER BY COUNT(h.id) DESC")
    List<ViewStats> getUrisStats(@Param("start") LocalDateTime start,
                            @Param("end") LocalDateTime end,
                            @Param("uris") List<String> uris);

    @Query("SELECT new ru.practicum.ewm.stats.service.model.ViewStats(h.app, h.uri, COUNT(h.id)) " +
            "FROM Hit AS h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "GROUP BY h.uri, h.app " +
            "ORDER BY COUNT(h.id) DESC")
    List<ViewStats> getAllUrisStats(@Param("start") LocalDateTime start,
                               @Param("end") LocalDateTime end);
}
