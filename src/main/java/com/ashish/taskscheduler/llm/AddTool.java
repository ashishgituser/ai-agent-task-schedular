package com.ashish.taskscheduler.llm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class AddTool implements AgentTool {

    private static final Logger log = LoggerFactory.getLogger(AddTool.class);
    @Override
    public String name() {
        return "add";
    }

    @Override
    public Tool definition() {
        // parameters is a JSON Schema object describing the function's arguments.
        Map<String, Object> parameters = Map.of(
                "type", "object",
                "properties", Map.of(
                        "a", Map.of("type", "number", "description", "the first number"),
                        "b", Map.of("type", "number", "description", "the second number")),
                "required", List.of("a", "b"));

        // Tool -> ToolFunction -> parameters.  type is always "function".
        return new Tool("function", new ToolFunction("add", "Adds two numbers together.", parameters));
    }

    @Override
    public String execute(Map<String, Object> arguments) {
        double a = ((Number) arguments.get("a")).doubleValue();
        double b = ((Number) arguments.get("b")).doubleValue();
        double result = a + b;
        log.info("AddTool.execute | {} + {} = {}", a, b, result);
        return String.valueOf(result);
    }
}
