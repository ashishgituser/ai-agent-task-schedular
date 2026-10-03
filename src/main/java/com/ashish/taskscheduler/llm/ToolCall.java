package com.ashish.taskscheduler.llm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * What the model sends back when it wants a tool run.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ToolCall(String id, FunctionCall function) {

}
