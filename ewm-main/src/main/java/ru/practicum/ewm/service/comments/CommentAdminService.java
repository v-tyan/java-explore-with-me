package ru.practicum.ewm.service.comments;

import ru.practicum.ewm.model.comments.dto.CommentDtoAdminStateUpdate;
import ru.practicum.ewm.model.comments.dto.CommentDtoResponse;

public interface CommentAdminService {

    CommentDtoResponse updateCommentByAdmin(Long commentId,
            CommentDtoAdminStateUpdate updateCommentAdminRequest);

    CommentDtoResponse getCommentById(Long commentId);
}