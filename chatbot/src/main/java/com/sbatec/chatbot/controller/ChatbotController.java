package com.sbatec.chatbot.controller;


import com.sbatec.chatbot.agent.AIAgent;
import com.sbatec.chatbot.config.prompts.CommonResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(CommonResource.Resource.BOT)
@CrossOrigin(origins = "http://localhost:4200")
public class ChatbotController {

    private final AIAgent aiAgent;

    public ChatbotController(AIAgent aiAgent) {
        this.aiAgent = aiAgent;
    }

    @PostMapping(value = "/message")
    public String askBot(@RequestBody String request) {
        log.info("Request from bot : {}", request);
        String response = aiAgent.ask(request);
        log.info("LLM Response : {}", response);
        return response;
    }

}
