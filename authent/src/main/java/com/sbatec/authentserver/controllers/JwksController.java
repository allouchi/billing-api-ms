package com.sbatec.authentserver.controllers;

import com.nimbusds.jose.jwk.JWKSet;
import com.sbatec.authentserver.config.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
@Slf4j
public class JwksController {

    private final JwtService jwtService;

    public JwksController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @GetMapping("/public-key/jwks.json")
    public Map<String, Object> keys() {
        log.info("=====jwks.json=====");
        return new JWKSet(jwtService.rsaJwk()).toJSONObject();
    }
}