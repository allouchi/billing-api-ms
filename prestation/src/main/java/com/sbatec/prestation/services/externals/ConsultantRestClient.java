package com.sbatec.prestation.services.externals;

import com.sbatec.prestation.dtos.Consultant;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;


@FeignClient(name = "consultant")
public interface ConsultantRestClient {
    @GetMapping("/api/consultants/{id}")
    @CircuitBreaker(name = "consultant", fallbackMethod = "getDefaultConsultant")
    Consultant findById(@PathVariable Long id);

    // 2. La NOUVELLE méthode par lot (Batch)
    @GetMapping("/api/consultants/batch")
    @CircuitBreaker(name = "consultant", fallbackMethod = "getDefaultConsultants")
    List<Consultant> findAllByIds(@RequestParam("ids") List<Long> ids);


    default Consultant getDefaultConsultant(Long id, Exception exception) {
        return Consultant.builder()
                .fonction("Default function")
                .email("Email")
                .firstName("FirstName")
                .lastName("LastName")
                .remoteError(exception.getMessage())
                .build();
    }

    // Fallback pour la méthode par lot
    default List<Consultant> getDefaultConsultants(List<Long> ids, Exception exception) {
        // En cas de panne du microservice, on génère une liste de clients par défaut
        // basés sur les IDs demandés pour ne pas casser la suite de votre code en mémoire.
        return ids.stream()
                .map(id -> Consultant.builder()
                        .id(id)
                        .fonction("Default function")
                        .email("Email")
                        .firstName("FirstName")
                        .lastName("LastName")
                        .remoteError(exception.getMessage())
                        .build())
                .collect(Collectors.toList());
    }
}