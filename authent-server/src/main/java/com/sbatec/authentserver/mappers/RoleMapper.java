package com.sbatec.authentserver.mappers;

import com.sbatec.authentserver.dtos.Role;
import com.sbatec.authentserver.models.RoleEntity;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper(componentModel = "spring")
public interface RoleMapper {

    Role toDto(RoleEntity roleEntity);

    RoleEntity toEntity(Role role);

    List<Role> toDtoList(List<RoleEntity> entities);

    List<RoleEntity> toEntityList(List<Role> dtos);
}

