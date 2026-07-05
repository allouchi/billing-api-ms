package com.sbatec.facture.services.internals.operation;


import com.sbatec.facture.dtos.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OperationService {

    Page<Operation> findBySiret(String siret, Pageable pageable);

    Page<Operation> findBySiretAndExerciseAndTypeOperation(String siret, String exercice, String type, Pageable pageable);

    Page<Operation> findBySiretAndExercise(String siret, String exercice, Pageable pageable);

    Page<Operation> findBySiretAndTypeOperation(String siret, String type, Pageable pageable);

    Operation addOperation(Operation operation);

    void deleteOperationById(Long id);
    Operation findById(Long id);

    List<Operation> findAll();

    Page<Operation> searchOperations(String siret, String searchTerm, Pageable pageable);
}
