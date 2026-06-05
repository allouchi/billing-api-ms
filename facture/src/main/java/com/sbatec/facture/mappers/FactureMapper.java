package com.sbatec.facture.mappers;

import com.sbatec.facture.dtos.Facture;
import com.sbatec.facture.models.FactureEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FactureMapper {

    Facture toDto(FactureEntity factureEntity);

    FactureEntity toEntity(Facture facture);

    List<Facture> toDtoList(List<FactureEntity> entities);

    List<FactureEntity> toEntityList(List<Facture> dtos);

}
