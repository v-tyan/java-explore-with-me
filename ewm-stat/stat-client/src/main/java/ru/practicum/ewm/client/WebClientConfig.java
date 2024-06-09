package ru.practicum.ewm.client;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {
        private static final int TIMEOUT = 5000;

        @Bean
        public WebClient webClientWithTimeout(@Value("${ewm-server.url}") String serverUrl) {
                final HttpClient httpClient = HttpClient.create()
                                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TIMEOUT)
                                .responseTimeout(Duration.ofMillis(TIMEOUT))
                                .doOnConnected(conn -> conn
                                                .addHandlerLast(new ReadTimeoutHandler(TIMEOUT, TimeUnit.MILLISECONDS))
                                                .addHandlerLast(new WriteTimeoutHandler(TIMEOUT,
                                                                TimeUnit.MILLISECONDS)));
                return WebClient.builder()
                                .baseUrl(serverUrl)
                                .clientConnector(new ReactorClientHttpConnector(httpClient))
                                .build();
        }
}