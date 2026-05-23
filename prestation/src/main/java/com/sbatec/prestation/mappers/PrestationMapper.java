package com.sbatec.prestation.mappers;

import com.sbatec.prestation.dtos.Client;
import com.sbatec.prestation.dtos.Consultant;
import com.sbatec.prestation.dtos.Prestation;
import com.sbatec.prestation.models.PrestationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {Client.class, Consultant.class})
public interface PrestationMapper {

    @Mapping(target = "clientId", source = "clientId")
    @Mapping(target = "consultantId", source = "consultantId")
    Prestation toDto(PrestationEntity prestationEntity);

    PrestationEntity toEntity(Prestation prestation);

    List<Prestation> toDtoList(List<PrestationEntity> entities);

    List<PrestationEntity> toEntityList(List<Prestation> dtos);
}
