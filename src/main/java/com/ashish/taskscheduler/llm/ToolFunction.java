package com.ashish.taskscheduler.llm;

import java.util.Map;

/**
 * Describes a function the model is allowed to call.
 * `parameters` is a JSON Schema object (kept flexible as a Map for now).
 */
public record ToolFunction(String name, String description, Map<String, Object> parameters) {

}
