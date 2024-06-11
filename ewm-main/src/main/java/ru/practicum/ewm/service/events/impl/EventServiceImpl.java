package ru.practicum.ewm.service.events.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.practicum.ewm.model.errors.BadRequestException;
import ru.practicum.ewm.model.errors.NotFoundException;
import ru.practicum.ewm.model.events.Event;
import ru.practicum.ewm.model.events.EventMapper;
import ru.practicum.ewm.model.events.State;
import ru.practicum.ewm.model.events.dto.EventFullDto;
import ru.practicum.ewm.model.events.dto.EventShortDto;
import ru.practicum.ewm.model.requests.RequestStatus;
import ru.practicum.ewm.repository.events.EventsRepository;
import ru.practicum.ewm.repository.events.util.EventUtil;
import ru.practicum.ewm.repository.requests.RequestsRepository;
import ru.practicum.ewm.service.events.EventService;
import ru.practicum.ewm.statistic.HitMapper;
import ru.practicum.ewm.statistic.StatService;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final EventsRepository eventsRepository;
    private final RequestsRepository requestsRepository;
    private final StatService statService;

    @Override
    public List<EventShortDto> getEvents(String text,
            List<Integer> categories,
            Boolean paid,
            String rangeStart,
            String rangeEnd,
            String sort,
            Boolean onlyAvailable,
            Pageable pageable,
            HttpServletRequest request) {
        if (categories != null && !categories.isEmpty()
                && categories.stream().sorted().collect(Collectors.toList()).get(0) <= 0) {
            throw new BadRequestException("bad category Id");
        }
        LocalDateTime start = null;
        LocalDateTime end = null;
        if (rangeStart != null) {
            start = LocalDateTime.parse(rangeStart, FORMATTER);
        }
        if (rangeEnd != null) {
            end = LocalDateTime.parse(rangeEnd, FORMATTER);
        }
        if (text == null)
            text = "";
        List<Event> events = eventsRepository.findByParamsOrderByDate(text.toLowerCase(),
                List.of(State.PUBLISHED),
                categories,
                paid,
                start,
                end,
                pageable);
        List<EventFullDto> fullEventDtoList = events.stream()
                .map(EventMapper.EVENT_MAPPER::toEventFullDto)
                .collect(Collectors.toList());

        if (Boolean.TRUE.equals(onlyAvailable)) {
            fullEventDtoList = fullEventDtoList.stream()
                    .filter(event -> event.getParticipantLimit() <= event.getConfirmedRequests())
                    .collect(Collectors.toList());
        }

        statService.createView(HitMapper.toEndpointHit("ewm-main-service", request));
        List<EventShortDto> eventsShort = fullEventDtoList.stream()
                .map(EventMapper.EVENT_MAPPER::toEventShortDto)
                .collect(Collectors.toList());
        if (sort != null && sort.equalsIgnoreCase("VIEWS")) {
            eventsShort.sort((e1, e2) -> e2.getViews().compareTo(e1.getViews()));
        }
        return eventsShort;
    }

    @Override
    public EventFullDto getEvent(Long eventId,
            HttpServletRequest request) {
        Event event = eventsRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));
        if (!event.getState().name().equalsIgnoreCase("published")) {
            throw new NotFoundException("Event not found");
        }
        EventFullDto fullEventDto = EventMapper.EVENT_MAPPER.toEventFullDto(event);

        fullEventDto.setConfirmedRequests(requestsRepository.findAllByEventIdAndStatus(event.getId(),
                RequestStatus.CONFIRMED).size());
        statService.createView(HitMapper.toEndpointHit("ewm-main-service", request));
        LocalDateTime min = fullEventDto.getPublishedOn();
        Map<String, EventFullDto> views = Stream.of(fullEventDto).collect(
                Collectors.toMap(fullEventDTO -> "/events/" + fullEventDto.getId(), fullEventDTO -> fullEventDto));
        Object responseBody = statService.getViewStats(
                min,
                LocalDateTime.now(),
                new ArrayList<>(views.keySet()),
                true)
                .getBody();
        return EventUtil.getViews(responseBody, views).get(0);
    }
}
