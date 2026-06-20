package com.sbatec.chatbot.config;

import java.util.Map;

public interface Tool {

    String name();

    Object execute(Map<String, Object> params);
}