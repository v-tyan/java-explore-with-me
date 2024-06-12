package ru.practicum.ewm.model.comments.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class CommentDtoUpdate {
    @NotNull(message = "text can't be null")
    @Size(min = 3, max = 255)
    private String text;
}
