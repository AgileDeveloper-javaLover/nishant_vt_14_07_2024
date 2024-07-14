package com.test.auth.repositories;

import com.test.auth.modal.RolePermission;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission,Long> {

    List<RolePermission> findAllByRoleId(Long roleId);

    List<RolePermission> findAllByRoleIdIn(Set<Long> roleId);
}
