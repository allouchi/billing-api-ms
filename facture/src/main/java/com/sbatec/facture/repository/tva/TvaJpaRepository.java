package com.sbatec.facture.repository.tva;


import com.sbatec.facture.models.TvaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TvaJpaRepository extends JpaRepository<TvaEntity, Long> {

    Page<TvaEntity> findByExercise(String exercise, Pageable pageable);

    Page<TvaEntity> findByExerciseAndSiret(String exercise, String siret, Pageable pageable);

    List<TvaEntity> findByExerciseAndSiret(String exercise, String siret);

    Page<TvaEntity> findBySiret(String siret, Pageable pageable);

    List<TvaEntity> findBySiret(String siret);

    List<TvaEntity> findByNumeroFacture(String numeroFacture);


    @Query("""
                SELECT o FROM TvaEntity o
                WHERE 
                    o.siret = :siret
                    AND (
                        LOWER(o.datePayment) LIKE LOWER(CONCAT('%', :search, '%'))                        
                        OR LOWER(o.exercise) LIKE LOWER(CONCAT('%', :search, '%'))
                        OR LOWER(o.numeroFacture) LIKE LOWER(CONCAT('%', :search, '%'))
                    )
            """)
    Page<TvaEntity> searchTvas(@Param("siret") String siret,
                               @Param("search") String search, Pageable pageableReq);


    @Query("""
                SELECT o FROM TvaEntity o
                WHERE 
                    o.siret = :siret
                    AND (
                        o.montantPayment = :search     
                        OR  o.montantTvaFacture = :search                                    
                    )
            """)
    Page<TvaEntity> searchTvasByNumeric(
            @Param("siret") String siret,
            @Param("search") Float search,
            Pageable pageable
    );
}
