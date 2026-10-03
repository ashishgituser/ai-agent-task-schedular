package com.ashish.taskscheduler.llm;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ChatRequest(String model, boolean stream, boolean think,
                          List<ChatMessage> messages, List<Tool> tools) {

    // Convenience constructor for plain chat (no tools) so existing code still compiles.
    public ChatRequest(String model, boolean stream, boolean think, List<ChatMessage> messages) {
        this(model, stream, think, messages, null);
    }
}
