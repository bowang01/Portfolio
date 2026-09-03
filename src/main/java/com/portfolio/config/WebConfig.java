package com.portfolio.config;

import java.nio.file.Path;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.portfolio.web.VisitorInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AppProperties properties;
    private final VisitorInterceptor visitorInterceptor;

    public WebConfig(AppProperties properties, VisitorInterceptor visitorInterceptor) {
        this.properties = properties;
        this.visitorInterceptor = visitorInterceptor;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadPath = Path.of(properties.getUploadDir()).toAbsolutePath().normalize();
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadPath.toUri().toString());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(visitorInterceptor)
                .addPathPatterns("/", "/projects/**")
                .excludePathPatterns(
                        "/admin/**",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/uploads/**",
                        "/favicon.svg",
                        "/favicon.ico"
                );
    }
}
