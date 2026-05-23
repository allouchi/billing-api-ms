package com.sbatec.company.services;

import com.sbatec.company.dtos.Company;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CompanyServiceTest {


    @Autowired
    CompanyService companyService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void addCompany() {

        Company company = Company.builder().
                siret("85292702900011")
                .codeApe("Code Ape")
                .socialReason("Sbatec Consulting")
                .build();
        companyService.addCompany(company);
        List<Company> companys = companyService.findAll();
        System.out.println(companys);

    }

    @Test
    void updateCompany() {
    }

    @Test
    void findAll() {
    }

    @Test
    void findById() {
    }

    @Test
    void findBySiret() {
    }

    @Test
    void deleteCompany() {
    }
}