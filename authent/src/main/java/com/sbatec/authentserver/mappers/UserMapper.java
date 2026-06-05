package com.sbatec.authentserver.mappers;

import com.sbatec.authentserver.dtos.User;
import com.sbatec.authentserver.models.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = RoleMapper.class)
public interface UserMapper {

    @Mapping(source = "roles", target = "roles")
    User toDto(UserEntity userEntity);

    @Mapping(source = "roles", target = "roles")
    UserEntity toEntity(User user);

    List<User> toDtoList(List<UserEntity> entities);

    List<UserEntity> toEntityList(List<User> dtos);


}
