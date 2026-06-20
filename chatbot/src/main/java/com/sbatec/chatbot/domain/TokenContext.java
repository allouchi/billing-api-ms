package com.sbatec.chatbot.domain;


public class TokenContext {

    private static final ThreadLocal<String> TOKEN = new ThreadLocal<>();

    public static String getToken() {
        return TOKEN.get();
    }

    public static void setToken(String token) {
        TOKEN.set(token);
    }

    public static void clear() {
        TOKEN.remove();
    }
}