package com.portfolio.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portfolio.domain.AdminUser;
import com.portfolio.repo.AdminUserRepository;

@Service
public class AdminUserService {

    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminUserService(AdminUserRepository adminUserRepository, PasswordEncoder passwordEncoder) {
        this.adminUserRepository = adminUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public AdminUser requireByUsername(String username) {
        return adminUserRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Admin user not found"));
    }

    @Transactional
    public AdminUser update(String currentUsername, String newUsername, String currentPassword, String newPassword) {
        AdminUser admin = requireByUsername(currentUsername);
        if (!passwordEncoder.matches(currentPassword, admin.getPasswordHash())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        String username = newUsername == null ? "" : newUsername.trim();
        if (username.isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (!username.equalsIgnoreCase(admin.getUsername()) && adminUserRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("That username is already taken");
        }
        admin.setUsername(username);

        if (newPassword != null && !newPassword.isBlank()) {
            if (newPassword.length() < 6) {
                throw new IllegalArgumentException("New password must be at least 6 characters");
            }
            admin.setPasswordHash(passwordEncoder.encode(newPassword));
        }
        return adminUserRepository.save(admin);
    }
}
