package com.sbatec.facture.mappers;

import com.sbatec.facture.dtos.Tva;
import com.sbatec.facture.models.TvaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TvaMapper {

    Tva toDto(TvaEntity tvaEntity);

    TvaEntity toEntity(Tva tva);

    List<Tva> toDtoList(List<TvaEntity> entities);

    List<TvaEntity> toEntityList(List<Tva> dtos);
}