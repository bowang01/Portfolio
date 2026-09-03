package com.portfolio.web;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.portfolio.service.VisitorService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class VisitorInterceptor implements HandlerInterceptor {

    private final VisitorService visitorService;

    public VisitorInterceptor(VisitorService visitorService) {
        this.visitorService = visitorService;
    }

    @Override
    public void postHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            ModelAndView modelAndView
    ) {
        if (modelAndView == null || modelAndView.getViewName() == null || modelAndView.getViewName().startsWith("redirect:")) {
            return;
        }
        if (!"GET".equalsIgnoreCase(request.getMethod()) || isBot(request.getHeader("User-Agent"))) {
            return;
        }
        modelAndView.addObject("visitorNumber", visitorService.numberFor(request, response));
    }

    private static boolean isBot(String userAgent) {
        if (userAgent == null || userAgent.isBlank()) {
            return false;
        }
        String value = userAgent.toLowerCase();
        return value.contains("bot")
                || value.contains("crawl")
                || value.contains("spider")
                || value.contains("slurp")
                || value.contains("preview")
                || value.contains("monitor");
    }
}
