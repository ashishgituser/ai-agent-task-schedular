package com.ashish.taskscheduler.controller;

import com.ashish.taskscheduler.agent.Agent;
import com.ashish.taskscheduler.client.OllamaClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ChatController {

    private final OllamaClient ollamaClient;
    private final Agent agent;

    public ChatController(OllamaClient ollamaClient, Agent agent) {
        this.ollamaClient = ollamaClient;
        this.agent = agent;
    }

    @GetMapping("/chat")
    public String chat(@RequestParam String userMessage) {
        return ollamaClient.chat(userMessage);
    }

    @GetMapping("/agent")
    public String agent(@RequestParam String prompt) {
        return agent.run(prompt);
    }

}
