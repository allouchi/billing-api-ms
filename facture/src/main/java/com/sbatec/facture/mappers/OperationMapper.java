package com.sbatec.facture.mappers;

import com.sbatec.facture.dtos.Operation;
import com.sbatec.facture.models.OperationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OperationMapper {

    Operation toDto(OperationEntity operationEntity);

    OperationEntity toEntity(Operation operation);

    List<Operation> toDtoList(List<OperationEntity> entities);

    List<OperationEntity> toEntityList(List<Operation> dtos);

    void updateEntityFromDto(Operation dto, @MappingTarget OperationEntity entity);
}