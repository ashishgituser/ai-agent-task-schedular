package com.ashish.taskscheduler.llm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Plays three roles: a user message, an assistant message (may carry tool_calls),
 * and a tool-result message (carries tool_name + content).
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ChatMessage(
        String role,
        String content,
        @JsonProperty("tool_calls") List<ToolCall> toolCalls,
        @JsonProperty("tool_name") String toolName) {

    // Convenience constructor so existing `new ChatMessage("user", text)` still works.
    public ChatMessage(String role, String content) {
        this(role, content, null, null);
    }
}
