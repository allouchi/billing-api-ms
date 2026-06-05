package com.sbatec.facture.mappers;

import com.sbatec.facture.dtos.Exercise;
import com.sbatec.facture.models.ExerciseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ExerciseMapper {

    Exercise toDto(ExerciseEntity exerciseEntity);

    ExerciseEntity toEntity(Exercise exercise);

    List<Exercise> toDtoList(List<ExerciseEntity> entities);

    List<ExerciseEntity> toEntityList(List<Exercise> dtos);
}