package com.sbatec.facture.services.externals;

import com.sbatec.facture.dtos.Client;
import com.sbatec.facture.dtos.EmailClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "client")
public interface ClientRestClient {
    @GetMapping("/api/clients/{id}")
    @CircuitBreaker(name = "client", fallbackMethod = "getDefaultClient")
    Client findById(@RequestHeader("Authorization") String token, @PathVariable Long id);

    default Client getDefaultClient(String token, Long id, Exception exception) {

        EmailClient emailClient = new EmailClient();
        emailClient.setEmail("default@email.com");
        return Client.builder()
                .id(id)
                .remoteError(exception.getMessage())
                .emails(List.of(emailClient))
                .socialReason("Default Social Reason")
                .build();
    }
}