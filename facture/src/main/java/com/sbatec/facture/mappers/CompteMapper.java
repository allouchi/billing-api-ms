package com.sbatec.facture.mappers;

import com.sbatec.facture.dtos.Compte;
import com.sbatec.facture.models.CompteEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CompteMapper {

    Compte toDto(CompteEntity compteEntity);

    CompteEntity toEntity(Compte exercise);

    List<Compte> toDtoList(List<CompteEntity> entities);

    List<CompteEntity> toEntityList(List<Compte> dtos);

    void updateEntityFromDto(Compte dto, @MappingTarget CompteEntity entity);
}