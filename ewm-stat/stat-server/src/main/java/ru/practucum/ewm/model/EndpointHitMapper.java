package ru.practucum.ewm.model;

import dto.EndpointHitDTO;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EndpointHitMapper {
    public static EndpointHit toEndpointHit(EndpointHitDTO endpointHitDTO) {
        return EndpointHit.builder()
                .id(endpointHitDTO.getId())
                .app(endpointHitDTO.getApp())
                .uri(endpointHitDTO.getUri())
                .ip(endpointHitDTO.getIp())
                .timestamp(endpointHitDTO.getTimestamp())
                .build();
    }

    public static EndpointHitDTO toEndpointHitDTO(EndpointHit endpointHit) {
        return EndpointHitDTO.builder()
                .id(endpointHit.getId())
                .app(endpointHit.getApp())
                .uri(endpointHit.getUri())
                .ip(endpointHit.getIp())
                .timestamp(endpointHit.getTimestamp())
                .build();
    }
}
