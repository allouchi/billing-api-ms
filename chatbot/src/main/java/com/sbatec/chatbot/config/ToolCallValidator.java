package com.sbatec.chatbot.config;

import java.util.Map;


public class ToolCallValidator {

    public void validate(String toolName, Map<String, Object> params) {

        if (toolName == null || toolName.isBlank()) {
            throw new IllegalArgumentException("Tool name missing");
        }

        if (params == null) {
            throw new IllegalArgumentException("Params missing");
        }

        // 🚫 Interdiction des objets non primitifs
        for (Map.Entry<String, Object> entry : params.entrySet()) {

            Object value = entry.getValue();

            if (value == null) {
                throw new IllegalArgumentException("Null param not allowed");
            }

            // 🚫 BLOQUE LES OBJETS COMME {"type": "..."}
            if (!(value instanceof String ||
                    value instanceof Number ||
                    value instanceof Boolean)) {

                throw new IllegalArgumentException(
                        "Invalid tool parameter structure: " + value
                );
            }
        }
    }
}