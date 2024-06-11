package ru.practicum.ewm.service.events;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ru.practicum.ewm.model.events.State;
import ru.practicum.ewm.model.events.dto.EventFullDto;
import ru.practicum.ewm.model.events.dto.UpdateEventAdminRequest;

@Transactional(readOnly = true)
public interface EventsAdminService {
    List<EventFullDto> getEvents(
            List<Integer> users,
            List<Integer> categories,
            List<State> states,
            String rangeStart,
            String rangeEnd,
            Integer from,
            Integer size);

    @Transactional
    EventFullDto updateEvent(
            UpdateEventAdminRequest updateEventAdminRequest,
            Long eventId);
}
