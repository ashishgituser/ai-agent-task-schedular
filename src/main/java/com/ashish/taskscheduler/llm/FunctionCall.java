package com.ashish.taskscheduler.llm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

/**
 * The function name + arguments inside a tool call.
 * `ignoreUnknown` skips extra fields like "index" that the model may include.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record FunctionCall(String name, Map<String, Object> arguments) {

}
