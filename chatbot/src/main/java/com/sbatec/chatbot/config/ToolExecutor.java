package com.sbatec.chatbot.config;


import java.util.Map;


public class ToolExecutor {

    private final ToolRegistry registry;
    private final ToolCallValidator validator;

    public ToolExecutor(ToolRegistry registry, ToolCallValidator validator) {
        this.registry = registry;
        this.validator = validator;
    }

    public Object execute(String toolName, Map<String, Object> params) {

        validator.validate(toolName, params);

        Tool tool = registry.get(toolName);

        if (tool == null) {
            throw new IllegalArgumentException("Unknown tool: " + toolName);
        }

        return tool.execute(params);
    }
}