package com.sbatec.facture.services.internals.compte;


import com.sbatec.facture.dtos.Compte;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface CompteService {

    Page<Compte> findBySiret(String siret, Pageable pageable);

    Page<Compte> findBySiretAndExerciseAndTypeOperation(String siret, String exercice, String type, Pageable pageable);

    Page<Compte> findBySiretAndExerciseAndTypeOperationAndMonthOperation(String siret, String exercice, String type, String monthOperation, Pageable pageable);

    Page<Compte> findBySiretAndExercise(String siret, String exercice, Pageable pageable);

    Page<Compte> findBySiretAndTypeOperation(String siret, String type, Pageable pageable);

    Compte addOperation(Compte compte);

    Page<Compte> findBySiretAndDateOperation(String siret, String dateOperation, Pageable pageable);

    List<Compte> importOperations(String siret, String file) throws IOException;

    Page<Compte> findBySiretAndMonthOperation(String siret, String month, Pageable pageable);

    Page<Compte> findBySiretAndExerciseAndMonthOperation(String siret, String exercice, String monthOperation, Pageable pageableReq);

    Page<Compte> findBySiretAndTypeOperationAndMonthOperation(String siret, String type, String monthOperation, Pageable pageableReq);

    Page<Compte> searchOperations(String siret, String searchTerm, Pageable pageableReq);

    List<Compte> findAll() throws IOException;

    Compte updateOperation(Compte compte);

}
