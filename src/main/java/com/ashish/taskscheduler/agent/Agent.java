package com.ashish.taskscheduler.agent;

import com.ashish.taskscheduler.client.OllamaClient;
import com.ashish.taskscheduler.llm.ChatMessage;
import com.ashish.taskscheduler.llm.ChatResponse;
import com.ashish.taskscheduler.llm.ToolCall;
import com.ashish.taskscheduler.llm.ToolRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * The agent loop: send the conversation + tools to the model, and keep going
 * until the model stops asking for tools and gives a final answer.
 */
@Service
public class Agent {

    private static final Logger log = LoggerFactory.getLogger(Agent.class);

    private static final int MAX_ITERATIONS = 5;

    private final OllamaClient ollamaClient;
    private final ToolRegistry toolRegistry;

    public Agent(OllamaClient ollamaClient, ToolRegistry toolRegistry) {
        this.ollamaClient = ollamaClient;
        this.toolRegistry = toolRegistry;
    }

    public String run(String userPrompt) {
        log.info("Agent run START | prompt='{}'", userPrompt);

        List<ChatMessage> messages = new ArrayList<>();
        messages.add(new ChatMessage("user", userPrompt));

        for (int i = 0; i < MAX_ITERATIONS; i++) {
            log.info("Iteration {} | sending {} message(s) to model", i, messages.size());
            ChatResponse response = ollamaClient.chat(messages, toolRegistry.definitions());
            ChatMessage reply = response.message();

            if (reply.toolCalls() == null || reply.toolCalls().isEmpty()) {
                log.info("Agent run DONE | no tool calls, final answer='{}'", reply.content());
                return reply.content();
            }

            log.info("Model requested {} tool call(s)", reply.toolCalls().size());
            messages.add(reply);

            for (ToolCall tc : reply.toolCalls()) {
                String name = tc.function().name();
                log.info(">> TOOL CALL  | name='{}' args={}", name, tc.function().arguments());
                String result = toolRegistry.run(name, tc.function().arguments());
                log.info("<< TOOL RESULT| name='{}' result='{}'", name, result);
                messages.add(new ChatMessage("tool", result, null, name));
            }
        }
        throw new IllegalStateException("Agent did not finish within " + MAX_ITERATIONS + " iterations");
    }
}
