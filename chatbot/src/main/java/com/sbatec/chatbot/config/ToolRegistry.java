package com.sbatec.chatbot.config;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ToolRegistry {

    private final Map<String, Tool> tools = new HashMap<>();

    public ToolRegistry(List<Tool> toolList) {
        for (Tool tool : toolList) {
            tools.put(tool.name(), tool);
        }
    }

    public Tool get(String name) {
        Tool tool = tools.get(name);

        if (tool == null) {
            throw new IllegalArgumentException("Unknown tool: " + name);
        }

        return tool;
    }
}