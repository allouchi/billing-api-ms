package com.sbatec.facture.services.internals.operation;

import com.sbatec.facture.dtos.Operation;
import com.sbatec.facture.mappers.OperationMapper;
import com.sbatec.facture.models.OperationEntity;
import com.sbatec.facture.repository.operation.OperationJpaRepository;
import com.sbatec.facture.util.Utils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class OperationServiceImpl implements OperationService {

    OperationJpaRepository operationJpaRepository;
    OperationMapper operationMapper;

    @Override
    public Page<Operation> findBySiret(String siret, Pageable pageable) {
        Page<OperationEntity> operationEntities = operationJpaRepository.findBySiret(siret, pageable);
        return operationEntities.map(operationMapper::toDto);
    }

    @Override
    public Page<Operation> findBySiretAndExercise(String siret, String exercice, Pageable pageable) {
        Page<OperationEntity> operationEntities = operationJpaRepository.findBySiretAndExercise(siret, exercice, pageable);
        return operationEntities.map(operationMapper::toDto);
    }

    @Override
    public Page<Operation> findBySiretAndTypeOperation(String siret, String type, Pageable pageable) {
        Page<OperationEntity> operationEntities = operationJpaRepository.findBySiretAndTypeOperation(siret, type, pageable);
        return operationEntities.map(operationMapper::toDto);

    }

    @Override
    public Page<Operation> findBySiretAndExerciseAndTypeOperation(String siret, String exercice, String type, Pageable pageable) {
        Page<OperationEntity> operationEntities = operationJpaRepository.findBySiretAndExerciseAndTypeOperation(siret, exercice, type, pageable);
        return operationEntities.map(operationMapper::toDto);
    }


    @Override
    public Operation addOperation(Operation operation) {
        OperationEntity operationEntity = operationMapper.toEntity(operation);
        OperationEntity operationSaved = operationJpaRepository.save(operationEntity);
        return operationMapper.toDto(operationSaved);
    }

    @Override
    public void deleteOperationById(Long id) {
        operationJpaRepository.deleteById(id);
    }

    @Override
    public List<Operation> findAll() {
        List<OperationEntity> operations = operationJpaRepository.findAll();
        return operations.stream().map(operationMapper::toDto).toList();
    }

    @Override
    public Page<Operation> searchOperations(String siret, String search, Pageable pageable) {

        Page<OperationEntity> operationEntities = null;
        boolean isNumeric = Utils.isNumeric(search);
        if (isNumeric) {
            String cleanedValue = search.replaceAll("\\s+", "").replace(",", ".");
            operationEntities = operationJpaRepository.searchOperationsByNumeric(siret, Float.valueOf(cleanedValue), pageable);
        } else {
            operationEntities = operationJpaRepository.searchOperations(siret, search, pageable);
        }
        return operationEntities.map(operationMapper::toDto);
    }
}
