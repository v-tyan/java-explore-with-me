package ru.practicum.ewm.model.comments.dto;

import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.model.comments.CommentAction;

@Data
@Builder
public class CommentDtoAdminStateUpdate {
    @NotNull
    private CommentAction commentState;
}
