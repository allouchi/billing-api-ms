package com.sbatec.facture.controllers;

import com.sbatec.facture.dtos.Exercise;
import com.sbatec.facture.services.internals.tva.ExerciseService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/exercises")
@RequiredArgsConstructor
@Slf4j
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExerciseController {
    ExerciseService exerciseService;

    @GetMapping("/exerciseRef")
    public List<Exercise> findAllExercises() {
        log.info("find all exercises ref");
        List<Exercise> response = exerciseService.findExercisesRef();
        return response.stream().sorted(Comparator.comparing(Exercise::getExercise).reversed())
                .collect(Collectors.toList());
    }
}
