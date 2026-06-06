package com.sbatec.facture.controllers;


import com.sbatec.facture.config.ApplicationProperties;
import com.sbatec.facture.dtos.Compte;
import com.sbatec.facture.services.internals.compte.CompteService;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/comptes")
public class CompteController {

    CompteService compteService;
    ApplicationProperties applicationProperties;


    @GetMapping(value = "/import/{siret}")
    public List<Compte> importOperations(@PathVariable @NotNull String siret) throws IOException {
        log.info("import csv file data");
        List<Compte> newComptes = new ArrayList<>();
        List<Compte> comptes = compteService.importOperations(siret, applicationProperties.getFichierImportCsv());
        if (!comptes.isEmpty()) {
            comptes.forEach(c -> {
                Compte compte = compteService.addOperation(c);
                newComptes.add(compte);
            });
        }
        return newComptes;
    }

    @GetMapping(value = "/{siret}/{exercice}/{type}/{monthOperation}")
    public Page<Compte> getCompteOperations(@PathVariable @NotNull String siret,
                                            @PathVariable @NotNull String exercice,
                                            @PathVariable @NotNull String type,
                                            @PathVariable @NotNull String monthOperation, Pageable pageable) {
        log.info("get all opérations by siret {}", siret);
        PageRequest pageableReq = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));

        // Treat all "Tous" cases
        if (exercice.equalsIgnoreCase("Tous")
                && type.equalsIgnoreCase("Tous")
                && monthOperation.equalsIgnoreCase("Tous")) {
            return compteService.findBySiret(siret, pageableReq);
        }

        // exercice
        if (!exercice.equalsIgnoreCase("Tous")
                && type.equalsIgnoreCase("Tous")
                && monthOperation.equalsIgnoreCase("Tous")) {
            return compteService.findBySiretAndExercise(siret, exercice, pageableReq);

        }

        // monthOperation
        if (exercice.equalsIgnoreCase("Tous")
                && type.equalsIgnoreCase("Tous")
                && !monthOperation.equalsIgnoreCase("Tous")) {
            return compteService.findBySiretAndMonthOperation(siret, monthOperation, pageableReq);
        }

        // type
        if (exercice.equalsIgnoreCase("Tous")
                && !type.equalsIgnoreCase("Tous")
                && monthOperation.equalsIgnoreCase("Tous")) {
            return compteService.findBySiretAndTypeOperation(siret, type, pageableReq);
        }

        // exercice + type + monthOperation
        if (!exercice.equalsIgnoreCase("Tous")
                && !type.equalsIgnoreCase("Tous")
                && !monthOperation.equalsIgnoreCase("Tous")) {
            return compteService.findBySiretAndExerciseAndTypeOperationAndMonthOperation(siret, exercice, type, monthOperation, pageableReq);
        }

        // exercice + type
        if (!exercice.equalsIgnoreCase("Tous")
                && !type.equalsIgnoreCase("Tous")
                && monthOperation.equalsIgnoreCase("Tous")) {
            return compteService.findBySiretAndExerciseAndTypeOperation(siret, exercice, type, pageableReq);
        }

        // exercice + monthOperation
        if (!exercice.equalsIgnoreCase("Tous")
                && type.equalsIgnoreCase("Tous")
                && !monthOperation.equalsIgnoreCase("Tous")) {
            return compteService.findBySiretAndExerciseAndMonthOperation(siret, exercice, monthOperation, pageableReq);
        }

        // type + monthOperation
        if (exercice.equalsIgnoreCase("Tous")
                && !type.equalsIgnoreCase("Tous")
                && !monthOperation.equalsIgnoreCase("Tous")) {
            return compteService.findBySiretAndTypeOperationAndMonthOperation(siret, type, monthOperation, pageableReq);
        }
        return null;
    }


    @PostMapping(value = "/search/{siret}")
    public Page<Compte> searchOperations(@RequestBody String searchTerm, @PathVariable @NotNull String siret, Pageable pageable) {
        log.info("searchOperations by siret = {}, pattern = {}", siret, searchTerm);
        PageRequest pageableReq = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));
        return compteService.searchOperations(siret, searchTerm, pageableReq);
    }
}

