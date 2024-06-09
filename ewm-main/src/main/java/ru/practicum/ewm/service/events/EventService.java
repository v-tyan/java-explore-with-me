package ru.practicum.ewm.service.events;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import ru.practicum.ewm.model.events.dto.EventFullDto;
import ru.practicum.ewm.model.events.dto.EventShortDto;

@Transactional(readOnly = true)
public interface EventService {
    List<EventShortDto> getEvents(String text,
            List<Integer> categories,
            Boolean paid,
            String rangeStart,
            String rangeEnd,
            String sort,
            Boolean onlyAvailable,
            Pageable pageable,
            HttpServletRequest request);

    EventFullDto getEvent(Long eventId, HttpServletRequest request);

}
