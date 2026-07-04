package com.sbatec.chatbot.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor // 👈 Très important pour Jackson
@AllArgsConstructor
public class ChatRequest {
    private String request;
}