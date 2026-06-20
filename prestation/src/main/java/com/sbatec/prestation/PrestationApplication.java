package com.sbatec.prestation;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.context.request.RequestContextHolder;

@SpringBootApplication
@EnableFeignClients
@DependsOn("flyway")
public class PrestationApplication {
    public static void main(String[] args) {
        SpringApplication.run(PrestationApplication.class, args);
    }

    @PostConstruct
    public void init() {
        // Permet à RequestContextHolder d'être hérité par les threads enfants
        RequestContextHolder.setRequestAttributes(
                RequestContextHolder.getRequestAttributes(),
                true
        );
    }
}
