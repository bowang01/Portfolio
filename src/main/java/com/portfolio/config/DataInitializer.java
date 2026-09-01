package com.portfolio.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.portfolio.domain.AdminUser;
import com.portfolio.domain.SiteSettings;
import com.portfolio.repo.AdminUserRepository;
import com.portfolio.repo.SiteSettingsRepository;

@Component
public class DataInitializer implements ApplicationRunner {

    private final SiteSettingsRepository siteSettingsRepository;
    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AppProperties appProperties;

    public DataInitializer(
            SiteSettingsRepository siteSettingsRepository,
            AdminUserRepository adminUserRepository,
            PasswordEncoder passwordEncoder,
            AppProperties appProperties
    ) {
        this.siteSettingsRepository = siteSettingsRepository;
        this.adminUserRepository = adminUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.appProperties = appProperties;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (siteSettingsRepository.count() == 0) {
            SiteSettings settings = new SiteSettings();
            settings.setId(1L);
            settings.setDisplayName("Portfolio");
            settings.setTagline("Turning ideas into products you can open");
            settings.setBio("A collection of websites and apps I have built. Each project has a cover, a short intro, and test data you can try. Sign in to add, edit, or remove work.");
            settings.setEmail("hello@example.com");
            settings.setGithubUrl("https://github.com");
            settings.setFooterText("Open a project for details, or sign in to manage the work.");
            siteSettingsRepository.save(settings);
        }

        if (adminUserRepository.count() == 0) {
            AdminUser admin = new AdminUser();
            admin.setUsername(appProperties.getAdmin().getUsername());
            admin.setPasswordHash(passwordEncoder.encode(appProperties.getAdmin().getPassword()));
            admin.setRole("ADMIN");
            adminUserRepository.save(admin);
        }
    }
}
