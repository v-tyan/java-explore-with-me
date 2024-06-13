package ru.practicum.ewm.model.comments.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentDtoCreate {
    @NotNull(message = "eventId can't be null'")
    private Long eventId;

    @NotNull(message = "text can't be null")
    @Size(min = 3, max = 255)
    private String text;
}