package com.sbatec.prestation.services.externals;

import com.sbatec.prestation.dtos.Client;
import com.sbatec.prestation.dtos.EmailClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@FeignClient(name = "client")
public interface ClientRestClient {

    @GetMapping("/api/clients/{id}")
    @CircuitBreaker(name = "client", fallbackMethod = "getDefaultClient")
    Client findById(@RequestHeader("Authorization") String token, @PathVariable Long id);

    @GetMapping("/api/clients/batch")
    @CircuitBreaker(name = "client", fallbackMethod = "getDefaultClients")
    List<Client> findAllByIds(@RequestHeader("Authorization") String token, @RequestParam("ids") List<Long> ids);

    // Correction ici : ajout de "String token"
    default Client getDefaultClient(String token, Long id, Exception exception) {
        EmailClient emailClient = new EmailClient();
        emailClient.setEmail("default@email.com");
        return Client.builder()
                .id(id)
                .emails(List.of(emailClient))
                .remoteError(exception.getMessage())
                .socialReason("Social Reason")
                .build();
    }

    // Correction ici : ajout de "String token"
    default List<Client> getDefaultClients(String token, List<Long> ids, Exception exception) {
        return ids.stream()
                .map(id -> Client.builder()
                        .id(id)
                        .socialReason("Social Reason")
                        .remoteError(exception.getMessage())
                        .build())
                .collect(Collectors.toList());
    }
}