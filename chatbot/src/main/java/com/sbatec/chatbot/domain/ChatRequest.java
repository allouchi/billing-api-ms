package com.sbatec.chatbot.domain;

import java.util.List;

public record ChatRequest(
        List<Message> messages
) {
}