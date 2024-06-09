package ru.practicum.ewm.statistic;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dto.EndpointHitDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.ewm.client.StatClient;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StatService {
    private final StatClient statClient;

    public ResponseEntity<Object> getViewStats(
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            List<String> uris,
            Boolean unique) {
        log.debug("StatsService - method call 'getViewStats' with params: rangeStart={}, rangeEnd={}, uris={}, " +
                "unique={}", rangeStart, rangeEnd, uris, unique);
        return statClient.getStat(rangeStart, rangeEnd, uris, unique);
    }

    @Transactional
    public void createView(EndpointHitDTO endpointHitDTO) {
        log.debug("StatsService - method call 'createView' with params: endpointHitDto={}", endpointHitDTO);
        statClient.save(endpointHitDTO);
    }
}
