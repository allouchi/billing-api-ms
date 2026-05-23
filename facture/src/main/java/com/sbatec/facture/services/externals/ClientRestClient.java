package com.sbatec.facture.services.externals;

import com.sbatec.facture.dtos.Client;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "client")
public interface ClientRestClient {
    @GetMapping("/api/client/{id}")
    @CircuitBreaker(name = "client", fallbackMethod = "getDefaultClient")
    Client findById(@PathVariable Long id);

    default Client getDefaultClient(Long id, Exception exception) {
        return Client.builder()
                .id(id)
                //.emails(Arrays.asList("default@email.com"))
                .socialReason("Social Reason")
                .build();
    }
}