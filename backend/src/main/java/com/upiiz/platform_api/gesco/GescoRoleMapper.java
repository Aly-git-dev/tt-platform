package com.upiiz.platform_api.gesco;

import com.upiiz.platform_api.entities.Role;
import com.upiiz.platform_api.repositories.RoleRepository;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class GescoRoleMapper {
    private final RoleRepository roles;
    public GescoRoleMapper(RoleRepository roles){
        this.roles=roles;
    }
    public Set<Role> rolesForGescoUser(){
        return Set.of(roles.findByName("ALUMNO"));
    }
}

