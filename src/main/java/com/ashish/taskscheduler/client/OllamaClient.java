package com.ashish.taskscheduler.client;

import com.ashish.taskscheduler.llm.ChatMessage;
import com.ashish.taskscheduler.llm.ChatRequest;
import com.ashish.taskscheduler.llm.ChatResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import com.ashish.taskscheduler.llm.Tool;

import java.util.List;

/**
 * LLM client layer: the only class that knows how to talk to Ollama over HTTP.
 * Everything else in the app will call chat(...) and never touch HTTP/JSON directly.
 */
@Service
public class OllamaClient {

    private final RestClient restClient;
    private final String model;
    private final boolean think;

    // Constructor injection: Spring calls this once at startup, passing in the
    // values from application.properties (resolved by the @Value expressions).
    public OllamaClient(
            @Value("${ollama.base-url}") String baseUrl,
            @Value("${ollama.model}") String model,
            @Value("${ollama.think}") boolean think) {
        // A RestClient pre-pointed at Ollama; every call below is relative to this base URL.
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
        this.model = model;
        this.think = think;
    }

    /**
     * Send one user message to the model and return just the reply text.
     */
    public String chat(String userMessage) {
        // Build the request body (maps to the JSON we tested by hand).
        ChatRequest request = new ChatRequest(
                model,
                false,  // stream = false -> one complete response, not chunks
                think,
                List.of(new ChatMessage("user", userMessage)));

        // POST it. .body(request) serializes record -> JSON (Jackson).
        // .body(ChatResponse.class) parses the JSON reply -> our record (Jackson).
        ChatResponse response = restClient.post()
                .uri("/api/chat")
                .body(request)
                .retrieve()
                .body(ChatResponse.class);

        // Dig out the one field we actually want.
        return response.message().content();
    }

    public ChatResponse chat(List<ChatMessage> messages, List<Tool> tools) {
        ChatRequest request = new ChatRequest(model, false, think, messages, tools);
        return restClient.post().uri("/api/chat").body(request).retrieve().body(ChatResponse.class);
    }

}
