package com.sbatec.facture.services.externals;

import com.sbatec.facture.dtos.Consultant;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;


@FeignClient(name = "consultant")
public interface ConsultantRestClient {
    @GetMapping("/api/consultants/{id}")
    @CircuitBreaker(name = "consultant", fallbackMethod = "getDefaultConsultant")
    Consultant findById(@RequestHeader("Authorization") String token, @PathVariable Long id);

    default Consultant getDefaultConsultant(String token, Long id, Exception exception) {
        return Consultant.builder()
                .fonction("Default function")
                .firstName("firstName")
                .lastName("lastName")
                .email("Email")
                .remoteError(exception.getMessage())
                .build();
    }
}