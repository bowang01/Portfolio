package com.portfolio.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.portfolio.domain.AdminUser;
import com.portfolio.repo.AdminUserRepository;

@Service
public class DbUserDetailsService implements UserDetailsService {

    private final AdminUserRepository adminUserRepository;

    public DbUserDetailsService(AdminUserRepository adminUserRepository) {
        this.adminUserRepository = adminUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AdminUser admin = adminUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Admin user not found"));
        return User.withUsername(admin.getUsername())
                .password(admin.getPasswordHash())
                .roles(admin.getRole())
                .build();
    }
}
