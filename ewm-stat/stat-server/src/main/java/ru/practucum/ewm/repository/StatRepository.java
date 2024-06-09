package ru.practucum.ewm.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ru.practucum.ewm.model.EndpointHit;
import ru.practucum.ewm.model.ViewStats;

public interface StatRepository extends JpaRepository<EndpointHit, Integer> {
    @Query("select new ru.practucum.ewm.model.ViewStats(hit.app, hit.uri, count(distinct hit.ip)) " +
            "from EndpointHit hit " +
            "where (hit.timestamp between :start and :end) " +
            "group by hit.app, hit.uri " +
            "order by count(distinct hit.ip) desc")
    List<ViewStats> getStatsWithoutUriUnique(@Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    @Query("select new ru.practucum.ewm.model.ViewStats(hit.app, hit.uri, count(hit.ip)) " +
            "from EndpointHit hit " +
            "where (hit.timestamp between :start and :end) " +
            "group by hit.app, hit.uri " +
            "order by count(hit.ip) desc")
    List<ViewStats> getStatsWithoutUriNotUnique(@Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    @Query("select new ru.practucum.ewm.model.ViewStats(hit.app,hit.uri,count(distinct hit.ip)) " +
            "from EndpointHit hit " +
            "where (hit.timestamp between :start and :end) " +
            "and hit.uri IN (:uris) " +
            "group by hit.app, hit.uri " +
            "order by count(distinct hit.ip) desc")
    List<ViewStats> getStatsUnique(@Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("uris") List<String> uris);

    @Query("select new ru.practucum.ewm.model.ViewStats(hit.app, hit.uri, count(hit.ip)) " +
            "from EndpointHit hit " +
            "where (hit.timestamp between :start and :end) " +
            "and hit.uri IN (:uris) " +
            "group by hit.app, hit.uri " +
            "order by count(hit.ip) desc")
    List<ViewStats> getStatsNotUnique(@Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("uris") List<String> uris);

}
