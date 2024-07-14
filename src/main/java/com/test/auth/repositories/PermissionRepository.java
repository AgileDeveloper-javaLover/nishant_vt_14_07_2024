package com.test.auth.repositories;

import com.test.auth.modal.Permissions;
import com.test.auth.modal.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permissions,Long> {
}
