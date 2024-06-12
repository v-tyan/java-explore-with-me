package ru.practicum.ewm.controller.comments;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.practicum.ewm.model.comments.dto.CommentDtoCreate;
import ru.practicum.ewm.model.comments.dto.CommentDtoResponse;
import ru.practicum.ewm.model.comments.dto.CommentDtoUpdate;
import ru.practicum.ewm.service.comments.CommentService;

@Validated
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class CommentPrivateController {

    private final CommentService service;

    @GetMapping("comments/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDtoResponse> getCommentByAuthorId(@PathVariable Long userId,
            @PositiveOrZero @RequestParam(defaultValue = "0", name = "from") Integer from,
            @Positive @RequestParam(defaultValue = "10", name = "size") Integer size) {
        return service.getCommentsByAuthorId(userId, from, size);
    }

    @PostMapping("comments/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDtoResponse addComment(@PathVariable Long userId,
            @Valid @RequestBody CommentDtoCreate commentDtoCreate) {
        return service.addComment(commentDtoCreate, userId);
    }

    @PatchMapping("comments/{userId}/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDtoResponse updateComment(@PathVariable Long userId,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentDtoUpdate commentDtoUpdate) {
        return service.updateComment(commentDtoUpdate, commentId, userId);
    }

    @DeleteMapping("comments/{commentId}/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long commentId,
            @PathVariable Long userId) {
        service.deleteComment(commentId, userId);
    }

}
