package ru.practicum.ewm.controller.comments;

import java.util.List;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.practicum.ewm.model.comments.dto.CommentDtoResponse;
import ru.practicum.ewm.service.comments.CommentService;

@Validated
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class CommentPublicController {

    private final CommentService service;

    @GetMapping("{eventId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDtoResponse> getCommentsByEventId(@PathVariable Long eventId,
            @PositiveOrZero @RequestParam(defaultValue = "0", name = "from") Integer from,
            @Positive @RequestParam(defaultValue = "10", name = "size") Integer size) {
        return service.getCommentsByEventId(eventId, from, size);
    }
}
