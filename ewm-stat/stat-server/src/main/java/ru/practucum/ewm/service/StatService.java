package ru.practucum.ewm.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import dto.EndpointHitDTO;
import dto.ViewStatsDTO;

public interface StatService {
    @Transactional
    EndpointHitDTO save(EndpointHitDTO endpointHitDTO);

    List<ViewStatsDTO> getStat(LocalDateTime start,
            LocalDateTime end,
            List<String> uris,
            boolean unique);
}
