package ru.practicum.ewm.service.requests;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ru.practicum.ewm.model.requests.dto.ParticipationRequestDto;

@Transactional(readOnly = true)
public interface RequestsService {
    List<ParticipationRequestDto> getRequest(Long userId);

    @Transactional
    ParticipationRequestDto setRequest(Long eventId, Long userId);

    @Transactional
    ParticipationRequestDto updateRequest(Long userId, Long requestId);
}
