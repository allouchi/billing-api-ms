package com.sbatec.facture.services.externals;

import com.sbatec.facture.dtos.Prestation;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;


@FeignClient(name = "prestation")
public interface PrestationRestClient {
    @GetMapping("/api/prestations/byId/{id}")
    @CircuitBreaker(name = "prestation", fallbackMethod = "getDefaultPrestation")
    Prestation findById(@RequestHeader("Authorization") String token, @PathVariable Long id);

    default Prestation getDefaultPrestation(String token, Long id, Exception exception) {
        return Prestation.builder()
                .id(id)
                .clientPrestation("Default client prestation")
                .numeroCommande("Default numero commande")
                .remoteError(exception.getMessage())
                .build();
    }
}