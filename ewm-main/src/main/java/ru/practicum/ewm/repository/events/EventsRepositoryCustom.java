package ru.practicum.ewm.repository.events;

import java.util.List;

import ru.practicum.ewm.model.events.Event;
import ru.practicum.ewm.model.events.State;

public interface EventsRepositoryCustom {
    List<Event> findEventsByCriteria(List<Integer> users,
            List<State> states,
            List<Integer> categories,
            String rangeStart,
            String rangeEnd,
            Integer from,
            Integer size);
}
