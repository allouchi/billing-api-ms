package com.sbatec.company.controllers;

import com.sbatec.company.dtos.Company;
import com.sbatec.company.services.CompanyService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompanyController {

    CompanyService companyService;

    @GetMapping("/{siret}")
    public Company findBySiret(@PathVariable String siret) {
        log.info("Find company by siret : {}", siret);
        return companyService.findBySiret(siret);
    }

    @GetMapping
    public ResponseEntity<List<Company>> findAll() {
        log.info("Find all companies");
        List<Company> companies = companyService.findAll();
        return ResponseEntity.ok(companies);
    }

    @PostMapping
    public Company addCompany(@RequestBody Company companyRequest) {
        log.info("Create new company");
        return companyService.addCompany(companyRequest);
    }


    @PutMapping
    public Company updateCompany(@RequestBody Company companyRequest) {
        log.info("Update company");
        return companyService.updateCompany(companyRequest);
    }


    @DeleteMapping(value = "/{id}")
    public void deleteCompany(@PathVariable long id) {
        log.info("delete company by id : {}", id);
        companyService.deleteCompany(id);
    }

}
