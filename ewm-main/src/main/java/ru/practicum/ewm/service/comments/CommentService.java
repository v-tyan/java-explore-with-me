package ru.practicum.ewm.service.comments;

import ru.practicum.ewm.model.comments.dto.CommentDtoCreate;
import ru.practicum.ewm.model.comments.dto.CommentDtoResponse;
import ru.practicum.ewm.model.comments.dto.CommentDtoUpdate;

import java.util.List;

public interface CommentService {
    List<CommentDtoResponse> getCommentsByEventId(Long eventId, int from, int size);

    List<CommentDtoResponse> getCommentsByAuthorId(Long authorId, int from, int size);

    CommentDtoResponse addComment(CommentDtoCreate commentDtoCreate, Long userId);

    CommentDtoResponse updateComment(CommentDtoUpdate commentDtoUpdate, Long commentId, Long userId);

    void deleteComment(Long commentId, Long userId);

}