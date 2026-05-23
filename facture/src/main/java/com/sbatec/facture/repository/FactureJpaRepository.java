package com.sbatec.facture.repository;

import com.sbatec.facture.models.FactureEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FactureJpaRepository extends JpaRepository<FactureEntity, Long> {

    FactureEntity findByNumeroFacture(String numeroFacture);

    Page<FactureEntity> findBySiret(String siret, Pageable pageable);

    List<FactureEntity> findBySiretAndDateEncaissementNotNull(String siret);

    @Query("SELECT f FROM FactureEntity f WHERE f.exercice = :exercice AND f.siret = :siret")
    Page<FactureEntity> findBySiretAndExercice(
            @Param("siret") String siret,
            @Param("exercice") String exercice,
            Pageable pageable
    );

    List<FactureEntity> findBySiret(String siret);

    @Query("SELECT f FROM FactureEntity f WHERE f.exercice = :exercice AND f.siret = :siret")
    List<FactureEntity> findBySiretAndExerciceTvaInfo(
            @Param("siret") String siret,
            @Param("exercice") String exercice
    );

    @Query("""
                SELECT f FROM FactureEntity f
                WHERE 
                    (SUBSTRING(f.dateFacturation, 7, 4) = :exercice
                     OR SUBSTRING(f.dateFacturation, 7, 4) = :exercicePrec)
                    AND f.dateEncaissement IS NOT NULL
                    AND f.siret = :siret
            """)
    List<FactureEntity> findBySiretAndExerciceAndPrec(
            @Param("siret") String siret,
            @Param("exercice") String exercice,
            @Param("exercicePrec") String exercicePrec
    );

    @Query("""
                SELECT f FROM FactureEntity f
                WHERE 
                    f.siret = :siret
                    AND (
                        LOWER(f.numeroFacture) LIKE LOWER(CONCAT('%', :search, '%'))
                        OR LOWER(f.dateFacturation) LIKE LOWER(CONCAT('%', :search, '%'))
                        OR LOWER(f.dateEncaissement) LIKE LOWER(CONCAT('%', :search, '%'))
                        OR LOWER(f.dateEcheance) LIKE LOWER(CONCAT('%', :search, '%'))
                        OR LOWER(f.factureStatus) LIKE LOWER(CONCAT('%', :search, '%'))
                        OR LOWER(f.clientPrestation) LIKE LOWER(CONCAT('%', :search, '%'))
                        OR LOWER(f.numeroCommande) LIKE LOWER(CONCAT('%', :search, '%'))
                        OR LOWER(f.moisFacture) LIKE LOWER(CONCAT('%', :search, '%'))
                        OR LOWER(f.taxType) LIKE LOWER(CONCAT('%', :search, '%'))
                    )
            """)
    Page<FactureEntity> searchFactures(
            @Param("siret") String siret,
            @Param("search") String search,
            Pageable pageable
    );

    @Query("""
                SELECT f FROM FactureEntity f
                WHERE 
                    f.siret = :siret
                    AND (
                        f.tarifHT = :search
                        OR f.montantTVA = :search
                        OR f.montantNetTVA = :search
                        OR f.prixTotalHT = :search
                        OR f.prixTotalTTC = :search
                        OR f.quantite = :search
                    )
            """)
    Page<FactureEntity> searchFacturesByNumeric(
            @Param("siret") String siret,
            @Param("search") Float search,
            Pageable pageable
    );

    List<FactureEntity> findByprestationId(Long id);
}
