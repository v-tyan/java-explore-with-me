package ru.practicum.ewm.model.comments.dto;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import ru.practicum.ewm.model.comments.Comment;
import ru.practicum.ewm.model.events.EventMapper;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = {EventMapper.class})
public interface CommentMapper {
    Comment toEntity(CommentDtoCreate commentDtoCreate);

    @Mapping(source = "author.name", target = "authorName")
    CommentDtoResponse toDto(Comment comment);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(CommentDtoUpdate commentDtoUpdate, @MappingTarget Comment comment);
}
