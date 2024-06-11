package ru.practicum.ewm.repository.events.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import dto.ViewStatsDTO;
import lombok.experimental.UtilityClass;
import ru.practicum.ewm.model.events.dto.EventFullDto;
import ru.practicum.ewm.model.requests.ParticipationRequest;

@UtilityClass
public class EventUtil {
    public List<EventFullDto> getViews(Object responseBody, Map<String, EventFullDto> views) {
        List<ViewStatsDTO> viewStatsDtos = new ObjectMapper().convertValue(responseBody, new TypeReference<>() {
        });
        viewStatsDtos.forEach(viewStatsDto -> {
            if (views.containsKey(viewStatsDto.getUri())) {
                views.get(viewStatsDto.getUri()).setViews(viewStatsDto.getHits());
            }
        });
        return new ArrayList<>(views.values());
    }

    public void getConfirmedRequests(List<EventFullDto> eventDtos, List<ParticipationRequest> requests) {
        Map<Long, Integer> counter = new HashMap<>();
        if (!requests.isEmpty()) {
            requests.forEach(request -> counter.put(request.getEvent().getId(),
                    counter.getOrDefault(request.getEvent().getId(), 0) + 1));

            eventDtos.forEach(event -> event.setConfirmedRequests(counter.get(event.getId())));
        } else {
            eventDtos.forEach(event -> event.setConfirmedRequests(0));
        }
    }
}
