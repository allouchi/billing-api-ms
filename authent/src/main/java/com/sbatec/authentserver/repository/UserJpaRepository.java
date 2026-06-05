package com.sbatec.authentserver.repository;

import com.sbatec.authentserver.models.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String userName);

    Optional<UserEntity> findByEmailAndPassword(String userName, String password);

}
