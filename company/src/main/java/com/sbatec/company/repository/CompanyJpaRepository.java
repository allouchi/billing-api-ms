package com.sbatec.company.repository;

import com.sbatec.company.models.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyJpaRepository extends JpaRepository<CompanyEntity, Long> {
    CompanyEntity findBySiret(String siret);
}
