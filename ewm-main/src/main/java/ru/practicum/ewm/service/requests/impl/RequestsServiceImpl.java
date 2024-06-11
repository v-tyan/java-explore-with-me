package ru.practicum.ewm.service.requests.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.practicum.ewm.model.errors.ConflictException;
import ru.practicum.ewm.model.errors.NotFoundException;
import ru.practicum.ewm.model.events.Event;
import ru.practicum.ewm.model.events.State;
import ru.practicum.ewm.model.requests.ParticipationRequest;
import ru.practicum.ewm.model.requests.RequestStatus;
import ru.practicum.ewm.model.requests.RequestsMapper;
import ru.practicum.ewm.model.requests.dto.ParticipationRequestDto;
import ru.practicum.ewm.model.users.User;
import ru.practicum.ewm.repository.events.EventsRepository;
import ru.practicum.ewm.repository.requests.RequestsRepository;
import ru.practicum.ewm.repository.users.UsersRepository;
import ru.practicum.ewm.service.requests.RequestsService;

@Service
@RequiredArgsConstructor
public class RequestsServiceImpl implements RequestsService {

    private final RequestsRepository requestsRepository;
    private final EventsRepository eventsRepository;
    private final UsersRepository usersRepository;

    @Override
    public List<ParticipationRequestDto> getRequest(Long userId) {
        return requestsRepository.findByRequesterId(userId).stream()
                .map(RequestsMapper.REQUESTS_MAPPER::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto setRequest(Long eventId, Long userId) {

        Event event = eventsRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));
        if (Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ConflictException("You can't send request to your own event");
        }
        if (event.getState() != State.PUBLISHED) {
            throw new ConflictException("Event is not published");
        }
        int confirmedRequests = requestsRepository.findByEventIdConfirmed(eventId).size();
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() <= confirmedRequests) {
            throw new ConflictException("Participant limit reached");
        }
        User user = usersRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        RequestStatus status = RequestStatus.PENDING;
        if (Boolean.TRUE.equals(!event.getRequestModeration()) || event.getParticipantLimit() == 0) {
            status = RequestStatus.CONFIRMED;
        }
        ParticipationRequest participationRequest = ParticipationRequest.builder()
                .created(LocalDateTime.now())
                .status(status)
                .event(event)
                .requester(user)
                .build();

        Optional<ParticipationRequest> check = requestsRepository.findByEventIdAndRequesterId(eventId, userId);
        if (check.isPresent())
            throw new ConflictException("You already have request to event");

        participationRequest = requestsRepository.save(participationRequest);
        return RequestsMapper.REQUESTS_MAPPER.toParticipationRequestDto(participationRequest);
    }

    @Override
    public ParticipationRequestDto updateRequest(Long userId, Long requestId) {
        ParticipationRequest request = requestsRepository.findByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> new NotFoundException("Request not found"));
        request.setStatus(RequestStatus.CANCELED);
        return RequestsMapper.REQUESTS_MAPPER.toParticipationRequestDto(requestsRepository.save(request));
    }
}
