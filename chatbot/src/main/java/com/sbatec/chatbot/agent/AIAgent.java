package com.sbatec.chatbot.agent;


import com.sbatec.chatbot.config.prompts.CommonResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

@Slf4j
@Service
public class AIAgent {

    private final ChatClient chatClient;

    public AIAgent(ChatClient.Builder chatClient, ToolCallbackProvider toolCallbackProvider) {

        this.chatClient = chatClient
                .defaultToolCallbacks(toolCallbackProvider)
                .defaultSystem(CommonResource.SYSTEM_PROMPT)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(MessageWindowChatMemory.builder().maxMessages(20)
                                .build()
                        ).build())
                .build();
    }

    public String ask(String query) {

        try {

            return chatClient
                    .prompt()
                    .user(query)
                    .call()
                    .content();

        } catch (Exception e) {
            Throwable cause = e;
            while (cause != null) {
                if (cause instanceof ResourceAccessException) {
                    log.error("Error : Service IA indisponible (connexion refusée)");
                    final String format = "Service IA indisponible (connexion refusée)";
                    throw new RuntimeException(format);
                }
                cause = cause.getCause();
            }

            log.error("Erreur inattendue", e);
            throw new RuntimeException("Erreur technique", e);
        }
    }
}
