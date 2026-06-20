package com.sbatec.chatbot.domain;

public record Message(
        String role,
        String content
) {
}