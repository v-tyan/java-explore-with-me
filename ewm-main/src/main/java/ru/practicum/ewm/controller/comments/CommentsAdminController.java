package ru.practicum.ewm.controller.comments;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.practicum.ewm.model.comments.dto.CommentDtoAdminStateUpdate;
import ru.practicum.ewm.model.comments.dto.CommentDtoResponse;
import ru.practicum.ewm.service.comments.CommentAdminService;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class CommentsAdminController {

    private final CommentAdminService commentAdminService;

    @GetMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDtoResponse getCommentById(@PathVariable Long commentId) {
        return commentAdminService.getCommentById(commentId);
    }

    @PatchMapping("comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDtoResponse updateCommentStatus(@PathVariable Long commentId,
            @Valid @RequestBody CommentDtoAdminStateUpdate moderateRequest) {
        return commentAdminService.updateCommentByAdmin(commentId, moderateRequest);
    }
}