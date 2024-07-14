package com.test.auth.services;

import com.test.auth.modal.RolePermission;
import com.test.auth.repositories.RolePermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class RolePermissionService {

    @Autowired
    private RolePermissionRepository rolePermissionRepository;
    public List<RolePermission> getAllByRoleId(Long id){
        return rolePermissionRepository.findAllByRoleId(id);
    }

    public List<RolePermission> getAllByRoleIdIn(Set<Long> id){
        return rolePermissionRepository.findAllByRoleIdIn(id);
    }
    
}
