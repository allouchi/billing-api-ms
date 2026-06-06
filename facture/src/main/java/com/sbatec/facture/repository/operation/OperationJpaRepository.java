package com.sbatec.facture.repository.operation;


import com.sbatec.facture.models.OperationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationJpaRepository extends JpaRepository<OperationEntity, Long> {

    Page<OperationEntity> findBySiret(String siret, Pageable pageableReq);

    Page<OperationEntity> findBySiretAndExercise(String siret, String exercise, Pageable pageableReq);

    Page<OperationEntity> findBySiretAndTypeOperation(String siret, String type, Pageable pageableReq);

    Page<OperationEntity> findBySiretAndExerciseAndTypeOperation(String siret, String exercise, String type, Pageable pageableReq);


    @Query("""
                SELECT o FROM OperationEntity o
                WHERE 
                    o.siret = :siret
                    AND (
                        LOWER(o.typeOperation) LIKE LOWER(CONCAT('%', :search, '%'))
                        OR LOWER(o.dateOperation) LIKE LOWER(CONCAT('%', :search, '%'))
                        OR LOWER(o.exercise) LIKE LOWER(CONCAT('%', :search, '%'))                       
                    )
            """)
    Page<OperationEntity> searchOperations(@Param("siret") String siret,
                                           @Param("search") String search, Pageable pageableReq);

    @Query("""
                SELECT o FROM OperationEntity o
                WHERE 
                    o.siret = :siret
                    AND (
                        o.montantoperation = :search                                          
                    )
            """)
    Page<OperationEntity> searchOperationsByNumeric(
            @Param("siret") String siret,
            @Param("search") Float search,
            Pageable pageable
    );

}
