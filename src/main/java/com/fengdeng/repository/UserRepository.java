package com.fengdeng.repository;

import com.fengdeng.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);
    long countByRole(String role);
}

