package ru.practicum.ewm.service.events.impl;

import static ru.practicum.ewm.model.events.EventMapper.EVENT_MAPPER;
import static ru.practicum.ewm.model.events.State.CANCELED;
import static ru.practicum.ewm.model.events.State.PENDING;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.practicum.ewm.model.categories.Category;
import ru.practicum.ewm.model.errors.BadRequestException;
import ru.practicum.ewm.model.errors.ConflictException;
import ru.practicum.ewm.model.errors.NotFoundException;
import ru.practicum.ewm.model.events.Event;
import ru.practicum.ewm.model.events.EventMapper;
import ru.practicum.ewm.model.events.State;
import ru.practicum.ewm.model.events.dto.EventFullDto;
import ru.practicum.ewm.model.events.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.model.events.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.model.events.dto.EventShortDto;
import ru.practicum.ewm.model.events.dto.NewEventDto;
import ru.practicum.ewm.model.events.dto.UpdateEventUserRequest;
import ru.practicum.ewm.model.location.Location;
import ru.practicum.ewm.model.location.LocationMapper;
import ru.practicum.ewm.model.requests.ParticipationRequest;
import ru.practicum.ewm.model.requests.RequestStatus;
import ru.practicum.ewm.model.requests.RequestsMapper;
import ru.practicum.ewm.model.requests.dto.ParticipationRequestDto;
import ru.practicum.ewm.model.users.User;
import ru.practicum.ewm.repository.categories.CategoriesRepository;
import ru.practicum.ewm.repository.events.EventsRepository;
import ru.practicum.ewm.repository.location.LocationRepository;
import ru.practicum.ewm.repository.requests.RequestsRepository;
import ru.practicum.ewm.repository.users.UsersRepository;
import ru.practicum.ewm.service.events.EventsPrivateService;

@Service
@RequiredArgsConstructor
public class EventsPrivateServiceImpl implements EventsPrivateService {

    private final EventsRepository eventsRepository;
    private final UsersRepository usersRepository;
    private final CategoriesRepository categoriesRepository;
    private final LocationRepository locationRepository;
    private final RequestsRepository requestsRepository;

    @Override
    public List<EventShortDto> getEvents(Long userId, Pageable pageable) {
        return eventsRepository.findAllByInitiatorId(userId, pageable).stream()
                .map(EventMapper.EVENT_MAPPER::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto setEvent(NewEventDto newEventDto, Long userId) {
        if (newEventDto.getEventDate().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Event contains wrong date");
        }
        User initiator = usersRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User with id = " + userId + " not found"));
        Category category = categoriesRepository.findById(newEventDto.getCategory()).orElseThrow(
                () -> new NotFoundException("Category with id = " + newEventDto.getCategory() + "not found"));

        Location location = locationRepository.save(LocationMapper.toLocation(newEventDto.getLocation()));

        newEventDto.setLocation(LocationMapper.toLocationDto(location));

        Event eventAfterMapping = EVENT_MAPPER.toEvent(initiator, category, newEventDto);

        Event event = eventsRepository.save(eventAfterMapping);

        return EVENT_MAPPER.toEventFullDto(event);
    }

    @Override
    public EventFullDto getEvent(Long userId, Long eventId) {
        Event event = eventsRepository.findByInitiatorIdAndId(userId, eventId)
                .orElseThrow(() -> new NotFoundException("Event not found"));

        EventFullDto fullEventDto = EventMapper.EVENT_MAPPER.toEventFullDto(event);
        fullEventDto.setConfirmedRequests(requestsRepository
                .findAllByEventIdAndStatus(eventId, RequestStatus.CONFIRMED).size());
        return fullEventDto;
    }

    @Override
    public EventFullDto updateEvent(Long userId,
            Long eventId,
            UpdateEventUserRequest updateEventUserRequest) {

        Event event = eventsRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));

        if (!event.getInitiator().getId().equals(userId)) {
            throw new BadRequestException("You can't update this event");
        }

        if (updateEventUserRequest.getEventDate() != null) {
            LocalDateTime time = updateEventUserRequest.getEventDate();
            if (LocalDateTime.now().isAfter(time.minusHours(2))) {
                throw new BadRequestException("Event starts in less then 2 hours");
            }
        }

        if (event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("You can't update published event");
        }

        if (updateEventUserRequest.getCategory() != null
                && !Objects.equals(updateEventUserRequest.getCategory().getId(), event.getCategory().getId())) {
            Category category = categoriesRepository.findById(updateEventUserRequest.getCategory().getId())
                    .orElseThrow(() -> new NotFoundException("Category not found"));
            event.setCategory(category);
        }
        if (updateEventUserRequest.getLocation() != null) {
            Location location = locationRepository.save(updateEventUserRequest.getLocation());
            event.setLocation(location);
        }
        if (updateEventUserRequest.getStateAction() != null) {
            switch (updateEventUserRequest.getStateAction()) {
                case CANCEL_REVIEW:
                    event.setState(CANCELED);
                    break;
                case SEND_TO_REVIEW:
                    event.setState(PENDING);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + updateEventUserRequest.getStateAction());
            }
        }

        EVENT_MAPPER.updateEventFromDto(updateEventUserRequest, event);

        EventFullDto fullEventDto = EVENT_MAPPER.toEventFullDto(event);

        fullEventDto.setConfirmedRequests(
                requestsRepository.findAllByEventIdAndStatus(eventId, RequestStatus.CONFIRMED).size());

        return fullEventDto;
    }

    @Override
    public List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId) {
        return requestsRepository.findByEventIdAndInitiatorId(eventId, userId).stream()
                .map(RequestsMapper.REQUESTS_MAPPER::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventRequestStatusUpdateResult updateEventStatusRequest(
            EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest,
            Long userId,
            Long eventId) {
        Event event = eventsRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));
        if (!event.getInitiator().getId().equals(userId)) {
            throw new NotFoundException("You don't have event with id " + eventId);
        }
        if (Boolean.TRUE.equals(!event.getRequestModeration())
                || event.getParticipantLimit() == 0) {
            throw new ConflictException("Confirmation is not required");
        }

        EventRequestStatusUpdateResult requestUpdateDto = new EventRequestStatusUpdateResult(new ArrayList<>(),
                new ArrayList<>());
        Integer confirmedRequests = requestsRepository.findByEventIdConfirmed(eventId).size();
        List<ParticipationRequest> requests = requestsRepository.findByEventIdAndRequestsIds(eventId,
                eventRequestStatusUpdateRequest.getRequestIds());
        if (eventRequestStatusUpdateRequest.getStatus().name().equalsIgnoreCase(RequestStatus.CONFIRMED.name())
                && confirmedRequests + requests.size() > event.getParticipantLimit()) {
            requests.forEach(request -> request.setStatus(RequestStatus.REJECTED));
            List<ParticipationRequestDto> requestDto = requests.stream()
                    .map(RequestsMapper.REQUESTS_MAPPER::toParticipationRequestDto)
                    .collect(Collectors.toList());
            requestUpdateDto.setRejectedRequests(requestDto);
            requestsRepository.saveAll(requests);
            throw new ConflictException("Requests limit exceeded");
        }
        if (eventRequestStatusUpdateRequest.getStatus().name().equalsIgnoreCase(RequestStatus.REJECTED.name())) {
            requests.forEach(request -> {
                if (request.getStatus().equals(RequestStatus.CONFIRMED)) {
                    throw new ConflictException("You can't reject confirmed request");
                }
                request.setStatus(RequestStatus.REJECTED);
            });
            List<ParticipationRequestDto> requestDto = requests.stream()
                    .map(RequestsMapper.REQUESTS_MAPPER::toParticipationRequestDto)
                    .collect(Collectors.toList());
            requestUpdateDto.setRejectedRequests(requestDto);
            requestsRepository.saveAll(requests);
        } else if (eventRequestStatusUpdateRequest.getStatus().name().equalsIgnoreCase(RequestStatus.CONFIRMED.name())
                && eventRequestStatusUpdateRequest.getRequestIds().size() <= event.getParticipantLimit()
                        - confirmedRequests) {
            requests.forEach(request -> request.setStatus(RequestStatus.CONFIRMED));
            List<ParticipationRequestDto> requestDto = requests.stream()
                    .map(RequestsMapper.REQUESTS_MAPPER::toParticipationRequestDto)
                    .collect(Collectors.toList());
            requestUpdateDto.setConfirmedRequests(requestDto);
            requestsRepository.saveAll(requests);
        }
        return requestUpdateDto;
    }
}
