package com.portfolio.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.portfolio.domain.AdminUser;

public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {

    Optional<AdminUser> findByUsername(String username);
}
