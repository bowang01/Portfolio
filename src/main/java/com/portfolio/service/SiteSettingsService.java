package com.portfolio.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portfolio.domain.SiteSettings;
import com.portfolio.repo.SiteSettingsRepository;

@Service
public class SiteSettingsService {

    private final SiteSettingsRepository repository;

    public SiteSettingsService(SiteSettingsRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public SiteSettings get() {
        return repository.findById(1L).orElseGet(this::defaults);
    }

    @Transactional
    public SiteSettings save(SiteSettings incoming) {
        SiteSettings current = repository.findById(1L).orElseGet(this::defaults);
        current.setDisplayName(incoming.getDisplayName().trim());
        current.setTagline(incoming.getTagline().trim());
        current.setBio(incoming.getBio().trim());
        current.setEmail(blankToNull(incoming.getEmail()));
        current.setGithubUrl(blankToNull(incoming.getGithubUrl()));
        current.setFooterText(blankToNull(incoming.getFooterText()));
        current.setId(1L);
        return repository.save(current);
    }

    private SiteSettings defaults() {
        SiteSettings settings = new SiteSettings();
        settings.setId(1L);
        settings.setDisplayName("Portfolio");
        settings.setTagline("Turning ideas into products you can open");
        settings.setBio("A collection of websites and apps I have built. Each project has an intro and test data you can try.");
        settings.setFooterText("Open a project for details.");
        return settings;
    }

    private static String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
