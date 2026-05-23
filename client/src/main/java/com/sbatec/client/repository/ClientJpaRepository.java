package com.sbatec.client.repository;


import com.sbatec.client.models.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientJpaRepository extends JpaRepository<ClientEntity, Long> {
    Optional<ClientEntity> findDistinctByEmailsEmail(String email);

    Optional<ClientEntity> findBySocialReasonContainingIgnoreCase(String socialReason);

    void deleteBySocialReasonContainingIgnoreCase(String socialReason);
}
