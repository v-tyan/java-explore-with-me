package ru.practicum.ewm.controller.events.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.practicum.ewm.controller.events.EventsPublicController;
import ru.practicum.ewm.model.events.dto.EventFullDto;
import ru.practicum.ewm.model.events.dto.EventShortDto;
import ru.practicum.ewm.service.events.EventService;

@RestController
@RequiredArgsConstructor
public class EventsPublicPublicControllerImpl implements EventsPublicController {
    private final EventService service;

    public List<EventShortDto> getEvents(String text, List<Integer> categories, Boolean paid, String rangeStart,
            String rangeEnd, String sort, Boolean onlyAvailable, Integer from, Integer size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(from, size);
        return service.getEvents(text, categories, paid, rangeStart, rangeEnd, sort, onlyAvailable, pageable, request);
    }

    public EventFullDto getEvent(Long eventId, HttpServletRequest request) {
        return service.getEvent(eventId, request);
    }
}
