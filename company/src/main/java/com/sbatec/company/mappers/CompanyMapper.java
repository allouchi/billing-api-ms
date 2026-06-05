package com.sbatec.company.mappers;

import com.sbatec.company.dtos.Company;
import com.sbatec.company.models.CompanyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CompanyMapper {

    @Mapping(target = "adresse", source = "adresse")
    Company toDto(CompanyEntity companyEntity);

    void updateEntityFromDto(Company dto, @MappingTarget CompanyEntity entity);

    CompanyEntity toEntity(Company company);

    List<Company> toDtoList(List<CompanyEntity> entities);

    List<CompanyEntity> toEntityList(List<Company> dtos);
}
