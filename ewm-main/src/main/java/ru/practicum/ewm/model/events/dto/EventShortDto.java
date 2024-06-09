package ru.practicum.ewm.model.events.dto;

import lombok.*;
import ru.practicum.ewm.model.categories.dto.CategoryDto;
import ru.practicum.ewm.model.users.dto.UserShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventShortDto {
    private Long id;
    @NotBlank
    private String annotation;
    @NotNull
    private CategoryDto category;
    private Integer confirmedRequests;
    private String eventDate;
    private UserShortDto initiator;
    @NotNull
    private Boolean paid;
    private String title;
    private Long views;
}
