package com.sbatec.facture;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FactureApplication {

    public static void main(String[] args) {
        SpringApplication.run(FactureApplication.class, args);
    }

}
