package ru.practicum.ewm.service.comments.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.ewm.model.comments.Comment;
import ru.practicum.ewm.model.comments.CommentState;
import ru.practicum.ewm.model.comments.dto.CommentDtoCreate;
import ru.practicum.ewm.model.comments.dto.CommentDtoResponse;
import ru.practicum.ewm.model.comments.dto.CommentDtoUpdate;
import ru.practicum.ewm.model.comments.dto.CommentMapper;
import ru.practicum.ewm.model.errors.BadRequestException;
import ru.practicum.ewm.model.errors.ConflictException;
import ru.practicum.ewm.model.errors.NotFoundException;
import ru.practicum.ewm.model.events.Event;
import ru.practicum.ewm.model.users.User;
import ru.practicum.ewm.repository.comments.CommentRepository;
import ru.practicum.ewm.repository.events.EventsRepository;
import ru.practicum.ewm.repository.users.UsersRepository;
import ru.practicum.ewm.service.comments.CommentService;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final EventsRepository eventsRepository;
    private final UsersRepository usersRepository;

    @Override
    public List<CommentDtoResponse> getCommentsByEventId(Long eventId, int from, int size) {
        log.info("Get comments by event_id =  {}", eventId);
        Pageable pageable = PageRequest.of(from, size);
        return commentRepository.findCommentsByEventId(eventId, pageable).stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteComment(Long commentId, Long userId) {
        log.info("deleting comment with id {}", commentId);
        Comment comment = getCommentIfExists(commentId);
        if (comment.getCommentState() == CommentState.PUBLISHED) {
            throw new ConflictException("You can delete only not published comments");
        }
        if (!Objects.equals(comment.getAuthor().getId(), userId)) {
            throw new BadRequestException("Unable to delete someone else's comment");
        }
        commentRepository.deleteById(commentId);
    }

    @Override
    public List<CommentDtoResponse> getCommentsByAuthorId(Long authorId, int from, int size) {
        log.info("getting comments by author id {}", authorId);
        Pageable pageable = PageRequest.of(from, size);
        return commentRepository.findCommentsByAuthorId(authorId, pageable).stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public CommentDtoResponse addComment(CommentDtoCreate commentDtoCreate, Long userId) {
        log.info("creating new comment by user with id {}", userId);
        User user = getUserIfExists(userId);
        Event event = getEventIfExists(commentDtoCreate.getEventId());
        Comment comment = commentMapper.toEntity(commentDtoCreate);
        comment.setAuthor(user);
        comment.setEvent(event);
        comment.setCreatedOn(LocalDateTime.now());
        comment.setCommentState(CommentState.PENDING);
        CommentDtoResponse dtoResponse = commentMapper.toDto(commentRepository.save(comment));
        log.info("created new comment: {}", comment);
        return dtoResponse;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public CommentDtoResponse updateComment(CommentDtoUpdate commentDtoUpdate, Long commentId, Long userId) {
        log.info("updating comment with id {}", commentId);
        this.getUserIfExists(userId);
        Comment comment = getCommentIfExists(userId, commentId);
        if (comment.getCommentState().equals(CommentState.PUBLISHED)) {
            throw new ConflictException("You can update only not published comments");
        }

        commentMapper.partialUpdate(commentDtoUpdate, comment);

        return commentMapper.toDto(comment);
    }

    private Comment getCommentIfExists(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(String.format("Comment with id=%d not found", commentId)));
    }

    private Comment getCommentIfExists(Long userId, Long commentId) {
        return commentRepository.findCommentByAuthorIdAndId(userId, commentId).orElseThrow(() -> new NotFoundException(
                String.format("Comment with id=%d from user with id=%d not found", userId, commentId)));
    }

    private Event getEventIfExists(Long eventId) {
        return eventsRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id %d not found", eventId)));
    }

    private User getUserIfExists(Long userId) {
        return usersRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id=%d not found", userId)));
    }
}
