package com.sbatec.company.services;

import com.sbatec.company.dtos.Company;
import com.sbatec.company.exceptions.CompanyNotFoundException;
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
    public Company updateCompany(Company companyDto) {
        // 1. Récupérer l'entité existante depuis la base de données
        CompanyEntity existingEntity = companyJpaRepository.findById(companyDto.getId())
                .orElseThrow(() -> new CompanyNotFoundException("Company not found with id: " + companyDto.getId()));

        // 2. Fusionner uniquement les modifications du DTO dans l'entité managée
        companyMapper.updateEntityFromDto(companyDto, existingEntity);

        // 3. Sauvegarder l'entité mise à jour
        CompanyEntity updatedEntity = companyJpaRepository.save(existingEntity);

        // 4. Retourner le DTO tout neuf
        return companyMapper.toDto(updatedEntity);
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