package ru.practucum.ewm.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import dto.EndpointHitDTO;
import dto.ViewStatsDTO;
import lombok.RequiredArgsConstructor;
import ru.practucum.ewm.exception.BadRequestException;
import ru.practucum.ewm.model.EndpointHitMapper;
import ru.practucum.ewm.model.ViewStatsMapper;
import ru.practucum.ewm.repository.StatRepository;

@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {
    private final StatRepository repository;

    @Override
    public EndpointHitDTO save(EndpointHitDTO endpointHitDTO) {
        repository.save(EndpointHitMapper.toEndpointHit(endpointHitDTO));
        return endpointHitDTO;
    }

    @Override
    public List<ViewStatsDTO> getStat(LocalDateTime start,
            LocalDateTime end,
            List<String> uris,
            boolean unique) {
        if (start.isAfter(end)) {
            throw new BadRequestException("Bad Request");
        }

        if (uris == null || uris.isEmpty()) {
            if (unique) {
                return repository.getStatsWithoutUriUnique(start, end).stream()
                        .map(ViewStatsMapper::toViewStatsDTO)
                        .collect(Collectors.toList());
            } else {
                return repository.getStatsWithoutUriNotUnique(start, end).stream()
                        .map(ViewStatsMapper::toViewStatsDTO)
                        .collect(Collectors.toList());
            }
        } else if (unique) {
            return repository.getStatsUnique(start, end, uris).stream()
                    .map(ViewStatsMapper::toViewStatsDTO)
                    .collect(Collectors.toList());
        } else {
            return repository.getStatsNotUnique(start, end, uris).stream()
                    .map(ViewStatsMapper::toViewStatsDTO)
                    .collect(Collectors.toList());
        }
    }
}
