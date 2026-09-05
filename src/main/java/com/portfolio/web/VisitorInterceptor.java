package com.portfolio.web;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.portfolio.service.VisitorService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

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
        HttpSession session = request.getSession();
        if (session.getAttribute("visitCounted") == null) {
            session.setAttribute("visitCounted", Boolean.TRUE);
            modelAndView.addObject("visitCount", visitorService.incrementAndGet());
            return;
        }
        modelAndView.addObject("visitCount", visitorService.currentTotal());
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
