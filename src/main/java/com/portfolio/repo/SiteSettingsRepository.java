package com.portfolio.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.portfolio.domain.SiteSettings;

public interface SiteSettingsRepository extends JpaRepository<SiteSettings, Long> {
}
