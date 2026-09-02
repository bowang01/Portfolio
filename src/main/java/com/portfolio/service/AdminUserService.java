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

    @Transactional
    public void createInitialAdminIfMissing(String username, String rawPassword) {
        if (adminUserRepository.count() > 0) {
            return;
        }
        String name = username == null ? "" : username.trim();
        if (name.isBlank() || rawPassword == null || rawPassword.isBlank()) {
            return;
        }
        AdminUser admin = new AdminUser();
        admin.setUsername(name);
        admin.setPasswordHash(passwordEncoder.encode(rawPassword));
        admin.setRole("ADMIN");
        adminUserRepository.save(admin);
    }

    @Transactional
    public void changePassword(String username, String currentPassword, String newPassword) {
        AdminUser admin = adminUserRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Admin user not found"));
        if (!passwordEncoder.matches(currentPassword, admin.getPasswordHash())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        if (newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("New password is required");
        }
        if (newPassword.length() < 6) {
            throw new IllegalArgumentException("New password must be at least 6 characters");
        }
        admin.setPasswordHash(passwordEncoder.encode(newPassword));
        adminUserRepository.save(admin);
    }
}
