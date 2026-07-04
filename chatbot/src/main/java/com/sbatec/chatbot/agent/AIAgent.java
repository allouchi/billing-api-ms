package com.sbatec.chatbot.agent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import reactor.core.publisher.Flux;

@Slf4j
@Service
public class AIAgent {

    private final ChatClient chatClient;

    public AIAgent(ChatClient.Builder chatClient, ToolCallbackProvider toolCallbackProvider) {
        this.chatClient = chatClient
                .defaultOptions(ChatOptions.builder()
                        .temperature(0.3)
                        .maxTokens(400) // 200 peut être un peu court si l'agent génère du JSON d'appel d'outil

                        .build()
                )
                .defaultToolCallbacks(toolCallbackProvider)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(MessageWindowChatMemory.builder().maxMessages(5).build()).build()
                )
                .build();
    }

    public Flux<String> ask(String query) {
        // Suppression du try-catch classique inutile ici
        return chatClient
                .prompt()
                .user(query)
                .stream()
                .content()
                // Gérer les erreurs de manière réactive
                .onErrorResume(e -> {
                    Throwable cause = e;
                    while (cause != null) {
                        if (cause instanceof ResourceAccessException) {
                            log.error("Error : Service IA indisponible (connexion refusée)");
                            return Flux.error(new RuntimeException("Service IA indisponible (connexion refusée)"));
                        }
                        cause = cause.getCause();
                    }
                    log.error("Erreur inattendue lors du stream de l'IA", e);
                    return Flux.error(new RuntimeException("Erreur technique", e));
                });
    }
}