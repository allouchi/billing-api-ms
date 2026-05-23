package com.sbatec.authentserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AuthentServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthentServerApplication.class, args);
    }

}
