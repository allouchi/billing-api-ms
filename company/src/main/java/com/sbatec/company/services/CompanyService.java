package com.sbatec.company.services;

import com.sbatec.company.dtos.Company;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;


public interface CompanyService {

    Company addCompany(Company company);

    Company updateCompany(Company company);

    @EntityGraph(attributePaths = {"adresse"})
        // Remplace "adresse" par le nom exact du champ dans ton entité
    List<Company> findAll();

    Company findById(Long id);

    Company findBySiret(String siret);

    void deleteCompany(Long id);

}
