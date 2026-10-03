package com.ashish.taskscheduler.llm;

/**
 * One entry in the request's "tools" array. `type` is always "function".
 */
public record Tool(String type, ToolFunction function) {

}
