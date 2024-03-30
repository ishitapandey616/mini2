package com.nagarro.mini.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfiguration {

    @Bean("api1WebClient")
    public WebClient api1WebClient() {
        return WebClient.builder()
                .baseUrl("https://randomuser.me/api/")
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create()
                        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 2000)
                        .responseTimeout(Duration.ofMillis(2000))
                        .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(2000, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(2000, TimeUnit.MILLISECONDS)))
                        // Add read and write timeouts as required
                ))
                .build();
    }

    @Bean("api2WebClient")
    public WebClient api2WebClient() {
        return WebClient.builder()
                .baseUrl("https://api.nationalize.io/")
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create()
                        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)
                        .responseTimeout(Duration.ofMillis(1000))
                        .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(1000, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(1000, TimeUnit.MILLISECONDS)))
                        // Add read and write timeouts as required
                ))
                .build();
    }

    @Bean("api3WebClient")
    public WebClient api3WebClient() {
        return WebClient.builder()
                .baseUrl("https://api.genderize.io/")
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create()
                        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)
                        .responseTimeout(Duration.ofMillis(1000))
                        .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(1000, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(1000, TimeUnit.MILLISECONDS)))
                        // Add read and write timeouts as required
                ))
                .build();
    }
}
