package com.sbatec.facture.controllers;

import com.sbatec.facture.dtos.Exercise;
import com.sbatec.facture.dtos.Tva;
import com.sbatec.facture.dtos.TvaInfo;
import com.sbatec.facture.services.internals.tva.ExerciseService;
import com.sbatec.facture.services.internals.tva.TvaService;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tvas")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TvaController {

    TvaService tvaService;
    ExerciseService exerciseService;

    @GetMapping("/exerciceRef")
    public List<Exercise> findAllExercises() {
        log.info("find all exercises ref");
        List<Exercise> response = exerciseService.findExercisesRef();
        return response.stream().sorted(Comparator.comparing(Exercise::getExercise).reversed())
                .collect(Collectors.toList());
    }

    @GetMapping("/tvasInfo/{siret}/{exercise}")
    public TvaInfo getTvaInfoByExercise(@PathVariable String exercise, @PathVariable String siret) {
        log.info("Get All tvas info by exercise : {} ", exercise);
        return tvaService.findTvaInfoByExercice(exercise, siret);
    }

    @GetMapping("/{siret}/{exercise}")
    public Page<Tva> getTvaByExercise(@PathVariable String siret, @PathVariable String exercise, Pageable pageable) {
        PageRequest pageableReq = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "datePaymentSorted"));
        log.info("Get All tvas by exercise : {} ", exercise);
        return tvaService.findByExerciseAndSiret(exercise, siret, pageableReq);
    }

    @PutMapping(consumes = "application/json", produces = "application/json")
    public void updateTva(@RequestBody Tva tvaRequest) {
        log.info("add or update tva : {}", tvaRequest);
        tvaService.updateTva(tvaRequest);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public void addTva(@RequestBody Tva tvaRequest) {
        log.info("add or update tva : {}", tvaRequest);
        tvaService.addTva(tvaRequest);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteTva(@PathVariable Long id) {
        log.info("delete by id : {}", id);
        tvaService.deleteById(id);
    }

    @PostMapping(value = "/search/{siret}")
    public Page<Tva> searchTvas(@RequestBody String searchTerm, @PathVariable @NotNull String siret, Pageable pageable) {
        log.info("searchTvas by siret = {}, pattern = {}", siret, searchTerm);
        PageRequest pageableReq = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));
        return tvaService.searchTvas(siret, searchTerm, pageableReq);
    }
}
