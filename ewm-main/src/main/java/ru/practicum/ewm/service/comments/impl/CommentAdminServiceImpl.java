package ru.practicum.ewm.service.comments.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.ewm.model.comments.Comment;
import ru.practicum.ewm.model.comments.CommentAction;
import ru.practicum.ewm.model.comments.CommentState;
import ru.practicum.ewm.model.comments.dto.CommentDtoAdminStateUpdate;
import ru.practicum.ewm.model.comments.dto.CommentDtoResponse;
import ru.practicum.ewm.model.comments.dto.CommentMapper;
import ru.practicum.ewm.model.errors.NotFoundException;
import ru.practicum.ewm.repository.comments.CommentRepository;
import ru.practicum.ewm.service.comments.CommentAdminService;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentAdminServiceImpl implements CommentAdminService {
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public CommentDtoResponse updateCommentByAdmin(Long commentId,
            CommentDtoAdminStateUpdate updateRequest) {
        log.info("updating comment with id {}", commentId);
        Comment comment = findCommentById(commentId);

        if (updateRequest.getCommentState().equals(CommentAction.PUBLISH)) {
            comment.setCommentState(CommentState.PUBLISHED);
            comment.setPublishedOn(LocalDateTime.now());
        } else {
            comment.setCommentState(CommentState.REJECTED);
        }
        log.info("comment updated: {}", comment);
        return commentMapper.toDto(comment);
    }

    @Override
    public CommentDtoResponse getCommentById(Long commentId) {
        log.info("getting comment by id {}", commentId);
        return commentMapper.toDto(findCommentById(commentId));
    }

    private Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(String.format("comment with id %d not found", commentId)));
    }
}
