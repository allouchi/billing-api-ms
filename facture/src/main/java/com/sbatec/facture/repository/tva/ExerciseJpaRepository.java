package com.sbatec.facture.repository.tva;


import com.sbatec.facture.models.ExerciseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseJpaRepository extends JpaRepository<ExerciseEntity, Long> {

    List<ExerciseEntity> findByExercise(String exercise);

    void deleteByExercise(String exercise);

}
