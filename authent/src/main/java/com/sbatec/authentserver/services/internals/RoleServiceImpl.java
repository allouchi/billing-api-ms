package com.sbatec.authentserver.services.internals;

import com.sbatec.authentserver.dtos.Role;
import com.sbatec.authentserver.mappers.RoleMapper;
import com.sbatec.authentserver.repository.RoleJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {
    RoleJpaRepository roleJpaRepository;
    RoleMapper roleMapper;

    public RoleServiceImpl(RoleJpaRepository roleJpaRepository) {
    }

    @Override
    public List<Role> findAll() {
        return roleMapper.toDtoList(roleJpaRepository.findAll());
    }

    @Override
    public Role findByRoleName(String roleName) {
        return roleMapper.toDto(roleJpaRepository.findByRoleName(roleName));
    }
}
