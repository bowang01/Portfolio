package com.portfolio.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.portfolio.domain.SiteSettings;
import com.portfolio.repo.SiteSettingsRepository;
import com.portfolio.service.AdminUserService;

@Component
public class DataInitializer implements ApplicationRunner {

    private final SiteSettingsRepository siteSettingsRepository;
    private final AdminUserService adminUserService;
    private final AppProperties appProperties;

    public DataInitializer(
            SiteSettingsRepository siteSettingsRepository,
            AdminUserService adminUserService,
            AppProperties appProperties
    ) {
        this.siteSettingsRepository = siteSettingsRepository;
        this.adminUserService = adminUserService;
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

        adminUserService.createInitialAdminIfMissing(
                appProperties.getAdmin().getUsername(),
                appProperties.getAdmin().getPassword()
        );
    }
}
