package ru.practicum.ewm.controller.requests.impl;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.practicum.ewm.controller.requests.RequestsPrivateController;
import ru.practicum.ewm.model.requests.dto.ParticipationRequestDto;
import ru.practicum.ewm.service.requests.RequestsService;

@RestController
@RequiredArgsConstructor
public class RequestsPrivateControllerImpl implements RequestsPrivateController {
    private final RequestsService requestsService;

    public List<ParticipationRequestDto> getRequest(Long userId) {
        return requestsService.getRequest(userId);
    }

    public ParticipationRequestDto setRequest(Long eventId,
            Long userId) {
        return requestsService.setRequest(eventId, userId);
    }

    public ParticipationRequestDto updateRequest(Long userId,
            Long requestId) {
        return requestsService.updateRequest(userId, requestId);
    }
}
