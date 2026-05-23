package com.sbatec.facture.services.externals;

import com.sbatec.facture.dtos.Consultant;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "consultant")
public interface ConsultantRestClient {
    @GetMapping("/api/consultant/{id}")
    @CircuitBreaker(name = "consultant", fallbackMethod = "getDefaultConsultant")
    Consultant findBySiret(@PathVariable Long id);

    default Consultant getDefaultConsultant(Long id, Exception exception) {
        return Consultant.builder()
                .fonction("Default function")
                .email("Email")
                .build();
    }
}