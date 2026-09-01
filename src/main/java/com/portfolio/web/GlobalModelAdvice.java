package com.portfolio.web;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.portfolio.domain.SiteSettings;
import com.portfolio.service.SiteSettingsService;

@ControllerAdvice
public class GlobalModelAdvice {

    private final SiteSettingsService siteSettingsService;

    public GlobalModelAdvice(SiteSettingsService siteSettingsService) {
        this.siteSettingsService = siteSettingsService;
    }

    @ModelAttribute("site")
    public SiteSettings site() {
        return siteSettingsService.get();
    }

    @ModelAttribute("loggedIn")
    public boolean loggedIn(Authentication authentication) {
        return authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
    }
}
