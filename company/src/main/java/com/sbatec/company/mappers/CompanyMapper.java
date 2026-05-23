package com.sbatec.company.mappers;

import com.sbatec.company.dtos.Company;
import com.sbatec.company.models.CompanyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    @Mapping(target = "adresse", source = "adresse")
    Company toDto(CompanyEntity companyEntity);

    CompanyEntity toEntity(Company company);

    List<Company> toDtoList(List<CompanyEntity> entities);

    List<CompanyEntity> toEntityList(List<Company> dtos);
}
