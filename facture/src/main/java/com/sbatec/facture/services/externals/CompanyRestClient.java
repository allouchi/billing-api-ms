package com.sbatec.facture.services.externals;

import com.sbatec.facture.dtos.Company;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;


@FeignClient(name = "company")
public interface CompanyRestClient {
    @GetMapping("/api/companies/{siret}")
    @CircuitBreaker(name = "company", fallbackMethod = "getDefaultCompany")
    Company findBySiret(@RequestHeader("Authorization") String token, @PathVariable String siret);

    default Company getDefaultCompany(String token, String siret, Exception exception) {
        return Company.builder()
                .siret(siret)
                .codeApe("Default Code Ape")
                .socialReason("Default Social Reason")
                .status("Default status")
                .remoteError(exception.getMessage())
                .build();
    }
}