package ru.practicum.ewm.model.events;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import ru.practicum.ewm.model.categories.Category;
import ru.practicum.ewm.model.events.dto.EventFullDto;
import ru.practicum.ewm.model.events.dto.EventShortDto;
import ru.practicum.ewm.model.events.dto.NewEventDto;
import ru.practicum.ewm.model.events.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.model.events.dto.UpdateEventUserRequest;
import ru.practicum.ewm.model.users.User;

@Mapper
public interface EventMapper {
    EventMapper EVENT_MAPPER = Mappers.getMapper(EventMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", expression = "java(category)")
    @Mapping(target = "state", constant = "PENDING")
    @Mapping(target = "createdOn", expression = "java(java.time.LocalDateTime.now())", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "publishedOn", expression = "java(java.time.LocalDateTime.now())", dateFormat = "yyyy-MM-dd HH:mm:ss")
    Event toEvent(User initiator, Category category, NewEventDto newEventDto);

    @Mapping(target = "eventDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEventFromDto(UpdateEventUserRequest updateEventUserRequest, @MappingTarget Event event);

    @Mapping(target = "eventDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "state", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEventFromDto(UpdateEventAdminRequest updateEventAdminRequest, @MappingTarget Event event);

    @Mapping(target = "views", expression = "java(0L)")
    @Mapping(target = "state", expression = "java(event.getState().name())")
    @Mapping(target = "createdOn", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "eventDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "publishedOn", dateFormat = "yyyy-MM-dd HH:mm:ss")
    EventFullDto toEventFullDto(Event event);

    @Mapping(target = "eventDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    EventShortDto toEventShortDto(Event event);

    @Mapping(target = "eventDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    EventShortDto toEventShortDto(EventFullDto eventFullDto);
}