package com.ashish.taskscheduler.llm;

import java.util.Map;

public interface AgentTool {
    String name();
    Tool definition();
    String execute(Map<String, Object> arguments);
}
