package com.sbatec.facture.controllers;


import com.sbatec.facture.config.ApplicationProperties;
import com.sbatec.facture.dtos.Facture;
import com.sbatec.facture.services.internals.facture.FactureService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/factures")
@RequiredArgsConstructor
@Slf4j
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FactureController {

    FactureService factureService;
    ApplicationProperties resources;

    private String getToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String rawToken = null;

        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            // Cas 1 : Le JWT est dans le Principal (Le plus standard)
            if (principal instanceof Jwt jwt) {
                rawToken = jwt.getTokenValue();
            }
        }
        return (rawToken != null) ? "Bearer " + rawToken : null;
    }


    // IMS-billing: fetch a single facture by id (edit deep-link / refresh).
    @GetMapping("/byId/{id}")
    public Facture getById(@PathVariable Long id) {
        log.info("find facture by id {}", id);
        return factureService.findById(id);
    }

    @GetMapping("/noPage/{siret}")
    public List<Facture> findAllBySiret(@PathVariable String siret, Pageable pageable) {
        log.info("findBySiret by siret");
        return factureService.findAllBySiret(siret);
    }

    @GetMapping("/{siret}")
    public Page<Facture> findBySiret(@PathVariable String siret, Pageable pageable) {
        log.info("findBySiret by siret and pageable");
        PageRequest pageableReq = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));
        return factureService.findAllBySiret(siret, pageableReq);
    }


    @GetMapping("/{siret}/{exercise}")
    public Page<Facture> findBySiretAndExercise(@PathVariable String siret, @PathVariable String exercise, Pageable pageable) {
        log.info("findBySiretAndExercise by siret, exercice and pageable");
        PageRequest pageableReq = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));
        return factureService.findBySiretAndExercice(siret, exercise, pageableReq);
    }


    @DeleteMapping("/{factureId}")
    public void deleteFacture(@PathVariable Long factureId) {
        log.info("delete bill");
        factureService.deleteFacture(factureId);
    }

    @PostMapping(value = "/create")
    public Facture createFacture(@RequestBody Facture facture) throws IOException, URISyntaxException {
        log.info("Create facture");

        Path repertoire = Paths.get(resources.getPathFilePdf());
        Files.createDirectories(repertoire);
        return factureService.addFacture(facture,
                resources.getPathFilePdf(),
                resources.getFichierSuiviFactures(), getToken());
    }


    @PutMapping(value = "/update", consumes = "application/json", produces = "application/json")
    public Facture updateFacture(@RequestBody Facture factureRequest) {
        log.info("Update facture : {}", factureRequest.getDateEncaissement());
        return factureService.updateFacture(factureRequest, resources.getPathFilePdf(),
                resources.getFichierSuiviFactures());
    }


    @PostMapping(value = "/search/{siret}")
    public Page<Facture> searchFactures(@RequestBody String searchTerm, @PathVariable String siret, Pageable pageable) {
        log.info("searchFactures by siret = {}, pattern = {}", siret, searchTerm);
        PageRequest pageableReq = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));
        return factureService.searchFactures(siret, searchTerm, pageableReq);
    }

}
