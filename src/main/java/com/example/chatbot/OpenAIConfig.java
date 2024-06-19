package com.example.chatbot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;

@Configuration
public class OpenAIConfig {

    @Value("${openai.api.key}")
    private String apiKey;

    @Bean
    public HttpClient httpClient() {
        return HttpClient.newBuilder().build();
    }

    @Bean
    public HttpRequest.Builder httpRequestBuilder() {
        return HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json");
    }
}
