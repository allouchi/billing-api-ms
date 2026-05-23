package com.sbatec.prestation.repository;


import com.sbatec.prestation.models.PrestationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrestationJpaRepository extends JpaRepository<PrestationEntity, Long> {
    List<PrestationEntity> findBySiret(String siret);
}
