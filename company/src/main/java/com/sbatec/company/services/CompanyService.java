package com.sbatec.company.services;

import com.sbatec.company.dtos.Company;

import java.util.List;


public interface CompanyService {

    Company addCompany(Company company);

    Company updateCompany(Company company);

    List<Company> findAll();

    Company findById(Long id);

    Company findBySiret(String siret);

    void deleteCompany(Long id);

}
