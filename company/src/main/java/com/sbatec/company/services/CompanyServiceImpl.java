package com.sbatec.company.services;

import com.sbatec.company.dtos.Company;
import com.sbatec.company.mappers.CompanyMapper;
import com.sbatec.company.models.CompanyEntity;
import com.sbatec.company.repository.CompanyJpaRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompanyServiceImpl implements CompanyService {
    CompanyJpaRepository companyJpaRepository;
    CompanyMapper companyMapper;


    @Override
    public Company addCompany(Company company) {
        CompanyEntity companyMapped = companyMapper.toEntity(company);
        CompanyEntity companyEntity = companyJpaRepository.save(companyMapped);
        return companyMapper.toDto(companyEntity);
    }

    @Override
    public Company updateCompany(Company company) {
        CompanyEntity companyEntity = companyJpaRepository.save(companyMapper.toEntity(company));
        return companyMapper.toDto(companyEntity);
    }


    @Override
    public List<Company> findAll() {
        List<CompanyEntity> companyEntity = companyJpaRepository.findAll();
        return companyMapper.toDtoList(companyEntity);
    }

    @Override
    public Company findById(Long id) {
        Optional<CompanyEntity> companyEntity = companyJpaRepository.findById(id);
        return companyEntity.map(companyMapper::toDto).orElse(null);
    }

    @Override
    public Company findBySiret(String siret) {
        CompanyEntity companyEntity = companyJpaRepository.findBySiret(siret);
        return companyMapper.toDto(companyEntity);
    }

    @Override
    public void deleteCompany(Long id) {
        companyJpaRepository.findById(id).ifPresent(companyJpaRepository::delete);
    }
}