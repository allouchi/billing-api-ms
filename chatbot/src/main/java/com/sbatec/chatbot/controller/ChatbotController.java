package com.sbatec.chatbot.controller;


import com.sbatec.chatbot.agent.AIAgent;
import com.sbatec.chatbot.domain.ChatRequest;
import com.sbatec.chatbot.prompts.CommonResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
@RequestMapping(CommonResource.Resource.BOT)
public class ChatbotController {

    private final AIAgent aiAgent;

    public ChatbotController(AIAgent aiAgent) {
        this.aiAgent = aiAgent;
    }

    // 🟢 CORRECTION : Utilisation de @PostMapping pour s'aligner avec le fetch() du Front
    @PostMapping(value = "/message", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> askBot(@RequestBody ChatRequest chatRequest) {

        log.info("======Request from bot : {}", chatRequest.getRequest());

        return aiAgent.ask(chatRequest.getRequest())
                .doOnNext(chunk -> log.info("CHUNK = [{}]", chunk))
                .doOnError(err -> log.error("error : ", err));
    }
}
