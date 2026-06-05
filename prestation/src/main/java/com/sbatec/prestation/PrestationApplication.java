package com.sbatec.prestation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.DependsOn;

@SpringBootApplication
@EnableFeignClients
@DependsOn("flyway")
public class PrestationApplication {
    public static void main(String[] args) {
        SpringApplication.run(PrestationApplication.class, args);
    }
}
