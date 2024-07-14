package com.test.auth.repositories;

import com.test.auth.modal.UserRole;
import com.test.auth.modal.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole,Long> {
    List<UserRole> findByUserId(Long userId);
}
