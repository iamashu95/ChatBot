package com.example.chatbot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OpenAIService {

    @Autowired
    private HttpClient httpClient;

    @Value("${openai.api.key}")
    private String apiKey;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String generateResponse(String prompt) throws IOException, InterruptedException {
        String requestBody = objectMapper.writeValueAsString(new ChatGPTRequest(prompt));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return parseResponse(response.body());
    }

    public String testOpenAIKey() throws IOException, InterruptedException {
        String requestBody = objectMapper.writeValueAsString(new ChatGPTRequest("Say hello!"));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    private String parseResponse(String responseBody) throws IOException {
        JsonNode responseJson = objectMapper.readTree(responseBody);
        return responseJson.get("choices").get(0).get("message").get("content").asText();
    }

    private static class ChatGPTRequest {
        public String model = "gpt-3.5-turbo";
        public Message[] messages;

        public ChatGPTRequest(String prompt) {
            this.messages = new Message[]{ new Message("user", prompt) };
        }
    }

    private static class Message {
        public String role;
        public String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }
}