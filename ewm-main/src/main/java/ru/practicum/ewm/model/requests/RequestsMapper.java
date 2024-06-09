package ru.practicum.ewm.model.requests;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import ru.practicum.ewm.model.requests.dto.ParticipationRequestDto;

@Mapper
public interface RequestsMapper {
    RequestsMapper REQUESTS_MAPPER = Mappers.getMapper(RequestsMapper.class);

    @Mapping(target = "event", source = "event.id")
    @Mapping(target = "requester", source = "requester.id")
    ParticipationRequestDto toParticipationRequestDto(ParticipationRequest participationRequest);
}
