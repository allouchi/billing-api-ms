package com.sbatec.facture.services.internals.tva;


import com.sbatec.facture.dtos.Exercise;

import java.util.List;

/**
 *
 * @author MALIANE
 *
 */
public interface ExerciseService {

    void delteByExercise(String exercice);

    void delteById(Long id);

    Exercise findByExercise(String exercice);

    List<Exercise> findExercisesRef();

    void updateExercise(Exercise tva);

    Exercise addExercise(Exercise tva);

}
