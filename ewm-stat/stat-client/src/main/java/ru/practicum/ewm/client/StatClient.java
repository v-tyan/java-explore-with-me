package ru.practicum.ewm.client;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import dto.EndpointHitDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class StatClient {
    private final WebClient webClient;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void save(EndpointHitDTO endpointHitDTO) {
        log.info("Save request to - {}", endpointHitDTO);
        webClient.post()
                .uri("/hit")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(endpointHitDTO))
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.CREATED)) {
                        return response.bodyToMono(Object.class)
                                .map(body -> ResponseEntity.status(HttpStatus.CREATED).body(body));
                    } else {
                        return response.createException()
                                .flatMap(Mono::error);
                    }
                })
                .block();
    }

    public ResponseEntity<Object> getStat(LocalDateTime start,
            LocalDateTime end,
            List<String> uris,
            boolean unique) {
        log.info("Get statistics from - {} ", uris);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/stats")
                        .queryParam("start", start.format(FORMATTER))
                        .queryParam("end", end.format(FORMATTER))
                        .queryParam(uris.toString())
                        .queryParam("unique", unique)
                        .build())
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        return response.bodyToMono(Object.class)
                                .map(body -> ResponseEntity.ok().body(body));
                    } else {
                        return response.createException()
                                .flatMap(Mono::error);
                    }
                })
                .block();
    }
}
