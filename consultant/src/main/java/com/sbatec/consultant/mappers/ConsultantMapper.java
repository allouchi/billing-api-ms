package com.sbatec.consultant.mappers;

import com.sbatec.consultant.dtos.Consultant;
import com.sbatec.consultant.models.ConsultantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ConsultantMapper {

    Consultant toDto(ConsultantEntity consultantEntity);

    ConsultantEntity toEntity(Consultant consultant);

    List<Consultant> toDtoList(List<ConsultantEntity> entities);

    List<ConsultantEntity> toEntityList(List<Consultant> dtos);
}
