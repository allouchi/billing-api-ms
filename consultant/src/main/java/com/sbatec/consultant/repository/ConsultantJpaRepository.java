package com.sbatec.consultant.repository;

import com.sbatec.consultant.models.ConsultantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConsultantJpaRepository extends JpaRepository<ConsultantEntity, Long> {
    Optional<ConsultantEntity> findByEmail(String mail);

    Optional<ConsultantEntity> findByFirstName(String firstName);

    Optional<ConsultantEntity> findByLastName(String lastName);
}
