package com.ashish.taskscheduler.llm;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ToolRegistry {
    private final Map<String, AgentTool> tools = new HashMap<>();

    // Spring injects every AgentTool bean into this list; we index them by name.
    public ToolRegistry(List<AgentTool> agentTools) {
        for (AgentTool tool : agentTools) {
            tools.put(tool.name(), tool);
        }
    }

    public List<Tool> definitions() {
        return tools.values().stream()
                .map(AgentTool::definition)
                .collect(Collectors.toList());
    }

    public String run(String toolName, Map<String, Object> arguments) {
        AgentTool tool = tools.get(toolName);
        if (tool == null) {
            throw new IllegalArgumentException("Tool not found: " + toolName);
        }
        return tool.execute(arguments);
    }
}
