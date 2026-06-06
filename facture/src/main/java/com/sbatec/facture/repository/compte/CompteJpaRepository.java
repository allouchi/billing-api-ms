package com.sbatec.facture.repository.compte;


import com.sbatec.facture.models.CompteEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompteJpaRepository extends JpaRepository<CompteEntity, Long> {

    Page<CompteEntity> findBySiret(String siret, Pageable pageableReq);

    List<CompteEntity> findBySiret(String siret);

    Page<CompteEntity> findBySiretAndExercise(String siret, String exercise, Pageable pageableReq);

    Page<CompteEntity> findBySiretAndTypeOperation(String siret, String type, Pageable pageableReq);

    Page<CompteEntity> findBySiretAndDateOperation(String siret, String dateOperation, Pageable pageableReq);

    Page<CompteEntity> findBySiretAndExerciseAndTypeOperation(String siret, String exercise, String type, Pageable pageableReq);

    Page<CompteEntity> findBySiretAndExerciseAndTypeOperationAndMonthOperation(String siret, String exercise,
                                                                               String type, String monthOperation, Pageable pageableReq);

    Page<CompteEntity> findBySiretAndMonthOperation(String siret, String month, Pageable pageable);

    Page<CompteEntity> findBySiretAndExerciseAndMonthOperation(String siret, String exercice, String monthOperation, Pageable pageableReq);

    Page<CompteEntity> findBySiretAndTypeOperationAndMonthOperation(String siret, String type, String monthOperation, Pageable pageableReq);

    @Query("""
                SELECT o FROM CompteEntity o
                WHERE  o.siret = :siret
                    AND (
                        LOWER(o.typeOperation) LIKE LOWER(CONCAT('%', :search, '%'))
                        OR LOWER(COALESCE(o.descriptionOperation, '')) LIKE LOWER(CONCAT('%', :search, '%'))
                        OR LOWER(o.exercise) LIKE LOWER(CONCAT('%', :search, '%'))
                    )
            """)
    Page<CompteEntity> searchOperations(@Param("siret") String siret,
                                        @Param("search") String search, Pageable pageableReq);


    @Query("""
                SELECT o FROM CompteEntity o
                WHERE  o.siret = :siret
                    AND (
                        o.montantoperation = :search
                    )
            """)
    Page<CompteEntity> searchOperationsByNumeric(
            @Param("siret") String siret,
            @Param("search") Float search,
            Pageable pageable
    );

}
