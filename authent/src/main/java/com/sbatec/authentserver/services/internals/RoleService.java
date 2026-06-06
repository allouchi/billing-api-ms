package com.sbatec.authentserver.services.internals;


import com.sbatec.authentserver.dtos.Role;

import java.util.List;

public interface RoleService {


    List<Role> findAll();

    Role findByRoleName(String roleName);
}
