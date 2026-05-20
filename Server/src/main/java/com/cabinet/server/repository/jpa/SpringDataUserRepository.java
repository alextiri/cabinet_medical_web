package com.cabinet.server.repository.jpa;

import com.cabinet.server.repository.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataUserRepository extends JpaRepository<UserEntity, Integer> {
    UserEntity findByUsername(String username);
}