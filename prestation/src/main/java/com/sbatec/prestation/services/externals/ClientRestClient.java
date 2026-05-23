package com.sbatec.prestation.services.externals;


import com.sbatec.prestation.dtos.Client;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;


@FeignClient(name = "client")
public interface ClientRestClient {
    @GetMapping("/api/clients/{id}")
    @CircuitBreaker(name = "client", fallbackMethod = "getDefaultClient")
    Client findById(@PathVariable Long id);

    // 2. La NOUVELLE méthode par lot (Batch)
    @GetMapping("/api/clients/batch")
    @CircuitBreaker(name = "client", fallbackMethod = "getDefaultClients")
    List<Client> findAllByIds(@RequestParam("ids") List<Long> ids);

    default Client getDefaultClient(Long id, Exception exception) {
        return Client.builder()
                .id(id)
                //.emails(Arrays.asList("default@email.com"))
                .socialReason("Social Reason")
                .build();
    }

    // Fallback pour la méthode par lot
    default List<Client> getDefaultClients(List<Long> ids, Exception exception) {
        // En cas de panne du microservice, on génère une liste de clients par défaut
        // basés sur les IDs demandés pour ne pas casser la suite de votre code en mémoire.
        return ids.stream()
                .map(id -> Client.builder()
                        .id(id)
                        .socialReason("Social Reason (Fallback Batch)")
                        .build())
                .collect(Collectors.toList());
    }
}