package com.sbatec.facture.controllers;


import com.sbatec.facture.dtos.Operation;
import com.sbatec.facture.services.internals.operation.OperationService;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/operations")
public class OperationController {

    OperationService operationService;

    @GetMapping(value = "/{siret}/{exercice}/{type}")
    public Page<Operation> getOperations(@PathVariable @NotNull String siret, @PathVariable @NotNull String exercice, @PathVariable @NotNull String type, Pageable pageable) {
        log.info("get all opérations by siret {}", siret);

        PageRequest pageableReq = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));
        if (!exercice.equalsIgnoreCase("Tous") && !type.equalsIgnoreCase("Tous")) {
            return operationService.findBySiretAndExerciseAndTypeOperation(siret, exercice, type, pageableReq);
        }

        if (exercice.equalsIgnoreCase("Tous") && type.equalsIgnoreCase("Tous")) {
            return operationService.findBySiret(siret, pageableReq);
        }

        if (!exercice.equalsIgnoreCase("Tous") && type.equalsIgnoreCase("Tous")) {
            return operationService.findBySiretAndExercise(siret, exercice, pageableReq);
        }

        if (exercice.equalsIgnoreCase("Tous") && !type.equalsIgnoreCase("Tous")) {
            return operationService.findBySiretAndTypeOperation(siret, type, pageableReq);
        }
        return null;
    }


    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping(value = "/add")
    public Operation addOperation(@RequestBody @NotNull Operation operation) {
        log.info("Add operation : {}", operation.getTypeOperation());
        return operationService.addOperation(operation);
    }


    @ResponseStatus(code = HttpStatus.ACCEPTED)
    @PutMapping(value = "/edit")
    public Operation editOperation(@RequestBody @NotNull Operation operation) {
        log.info("Edit operation : {}", operation.getTypeOperation());
        return operationService.addOperation(operation);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void deleteOperation(@PathVariable @NotNull long id) {
        log.info("delete operation by id : {}", id);
        operationService.deleteOperationById(id);
    }


    @PostMapping(value = "/search/{siret}")
    public Page<Operation> searchOperations(@RequestBody String searchTerm, @PathVariable @NotNull String siret, Pageable pageable) {
        log.info("searchOperations by siret = {}, pattern = {}", siret, searchTerm);
        PageRequest pageableReq = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));
        return operationService.searchOperations(siret, searchTerm, pageableReq);
    }
}
