package com.sbatec.facture.services.internals.tva;

import com.sbatec.facture.dtos.Exercise;
import com.sbatec.facture.mappers.ExerciseMapper;
import com.sbatec.facture.models.ExerciseEntity;
import com.sbatec.facture.repository.tva.ExerciseJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class ExerciseServiceImpl implements ExerciseService {
    ExerciseJpaRepository exerciseJpaRepository;
    ExerciseMapper exerciseMapper;

    @Override
    public void delteByExercise(String exercice) {

    }

    @Override
    public void delteById(Long id) {

    }

    @Override
    public Exercise findByExercise(String exercice) {
        return null;
    }

    @Override
    public List<Exercise> findExercisesRef() {
        List<ExerciseEntity> entities = exerciseJpaRepository.findAll();
        entities.sort(Comparator.comparing(ExerciseEntity::getId));
        return exerciseMapper.toDtoList(entities);
    }

    @Override
    public void updateExercise(Exercise tva) {

    }

    @Override
    public Exercise addExercise(Exercise tva) {
        return null;
    }
}
