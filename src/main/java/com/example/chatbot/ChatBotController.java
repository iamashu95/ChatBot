package com.example.chatbot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chatbot")
public class ChatBotController {

    @Autowired
    private OpenAIService openAIService;

    @GetMapping("/test")
    public ResponseEntity<String> testOpenAIKey() {
        try {
            String response = openAIService.testOpenAIKey();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/ask")
    public ResponseEntity<String> askQuestion(@RequestBody String question) {
        try {
            String response = openAIService.generateResponse(question);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}