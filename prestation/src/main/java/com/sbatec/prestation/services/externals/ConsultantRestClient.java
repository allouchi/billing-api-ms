package com.sbatec.prestation.services.externals;

import com.sbatec.prestation.dtos.Consultant;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@FeignClient(name = "consultant")
public interface ConsultantRestClient {

    @GetMapping("/api/consultants/{id}")
    @CircuitBreaker(name = "consultant", fallbackMethod = "getDefaultConsultant")
    Consultant findById(@RequestHeader("Authorization") String token, @PathVariable Long id);

    @GetMapping("/api/consultants/batch")
    @CircuitBreaker(name = "consultant", fallbackMethod = "getDefaultConsultants")
    List<Consultant> findAllByIds(@RequestHeader("Authorization") String token, @RequestParam("ids") List<Long> ids);

    // Correction ici : ajout de "String token" au début
    default Consultant getDefaultConsultant(String token, Long id, Exception exception) {
        return Consultant.builder()
                .fonction("Default function")
                .email("Email")
                .firstName("FirstName")
                .lastName("LastName")
                .remoteError(exception.getMessage())
                .build();
    }

    // Correction ici : ajout de "String token" au début
    default List<Consultant> getDefaultConsultants(String token, List<Long> ids, Exception exception) {
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