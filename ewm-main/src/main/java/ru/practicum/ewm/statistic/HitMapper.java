package ru.practicum.ewm.statistic;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import dto.EndpointHitDTO;
import lombok.experimental.UtilityClass;

@UtilityClass
public class HitMapper {
    public static EndpointHitDTO toEndpointHit(String app, HttpServletRequest request) {
        return new EndpointHitDTO(null,
                app,
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now());
    }
}
