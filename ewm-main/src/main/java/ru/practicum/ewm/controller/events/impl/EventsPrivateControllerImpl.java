package ru.practicum.ewm.controller.events.impl;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.practicum.ewm.controller.events.EventsPrivateController;
import ru.practicum.ewm.model.events.dto.EventFullDto;
import ru.practicum.ewm.model.events.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.model.events.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.model.events.dto.EventShortDto;
import ru.practicum.ewm.model.events.dto.NewEventDto;
import ru.practicum.ewm.model.events.dto.UpdateEventUserRequest;
import ru.practicum.ewm.model.requests.dto.ParticipationRequestDto;
import ru.practicum.ewm.service.events.EventsPrivateService;

@RestController
@RequiredArgsConstructor
public class EventsPrivateControllerImpl implements EventsPrivateController {

    private final EventsPrivateService service;

    public List<EventShortDto> getEvents(Long userId, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        return service.getEvents(userId, pageable);
    }

    public EventFullDto setEvent(NewEventDto newEventDto, Long userId) {
        return service.setEvent(newEventDto, userId);
    }

    public EventFullDto getEvent(Long userId, Long eventId) {
        return service.getEvent(userId, eventId);
    }

    public EventFullDto updateEvent(Long userId, Long eventId,
            UpdateEventUserRequest updateEventUserRequest) {
        return service.updateEvent(userId, eventId, updateEventUserRequest);
    }

    public List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId) {
        return service.getEventRequests(userId, eventId);
    }

    public EventRequestStatusUpdateResult updateEventStatusRequest(
            EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest,
            Long userId,
            Long eventId) {
        return service.updateEventStatusRequest(eventRequestStatusUpdateRequest, userId, eventId);
    }
}
