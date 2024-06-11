package ru.practicum.ewm.service.events;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import ru.practicum.ewm.model.events.dto.EventFullDto;
import ru.practicum.ewm.model.events.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.model.events.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.model.events.dto.EventShortDto;
import ru.practicum.ewm.model.events.dto.NewEventDto;
import ru.practicum.ewm.model.events.dto.UpdateEventUserRequest;
import ru.practicum.ewm.model.requests.dto.ParticipationRequestDto;

@Transactional(readOnly = true)
public interface EventsPrivateService {

    List<EventShortDto> getEvents(Long userId,
            Pageable pageable);

    @Transactional
    EventFullDto setEvent(NewEventDto newEventDto,
            Long userId);

    EventFullDto getEvent(Long userId,
            Long eventId);

    @Transactional
    EventFullDto updateEvent(Long userId,
            Long eventId,
            UpdateEventUserRequest updateEventUserRequest);

    List<ParticipationRequestDto> getEventRequests(Long userId,
            Long eventId);

    @Transactional
    EventRequestStatusUpdateResult updateEventStatusRequest(
            EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest,
            Long userId,
            Long eventId);
}
