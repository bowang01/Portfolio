package com.portfolio.service;

import java.time.Duration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portfolio.domain.VisitStats;
import com.portfolio.repo.VisitStatsRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class VisitorService {

    static final String COOKIE_NAME = "visitor_no";

    private final VisitStatsRepository visitStatsRepository;

    public VisitorService(VisitStatsRepository visitStatsRepository) {
        this.visitStatsRepository = visitStatsRepository;
    }

    @Transactional
    public void ensureRow() {
        if (visitStatsRepository.existsById(1L)) {
            return;
        }
        VisitStats stats = new VisitStats();
        stats.setId(1L);
        stats.setTotalVisitors(0);
        visitStatsRepository.save(stats);
    }

    @Transactional
    public long numberFor(HttpServletRequest request, HttpServletResponse response) {
        Long existing = readCookie(request);
        if (existing != null) {
            return existing;
        }

        VisitStats stats = visitStatsRepository.lockById(1L).orElseGet(() -> {
            VisitStats created = new VisitStats();
            created.setId(1L);
            created.setTotalVisitors(0);
            return visitStatsRepository.save(created);
        });
        long next = stats.getTotalVisitors() + 1;
        stats.setTotalVisitors(next);
        visitStatsRepository.save(stats);
        writeCookie(request, response, next);
        return next;
    }

    private static Long readCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (!COOKIE_NAME.equals(cookie.getName())) {
                continue;
            }
            try {
                long value = Long.parseLong(cookie.getValue());
                if (value > 0) {
                    return value;
                }
            } catch (NumberFormatException ignored) {
                return null;
            }
        }
        return null;
    }

    private static void writeCookie(HttpServletRequest request, HttpServletResponse response, long number) {
        ResponseCookie cookie = ResponseCookie.from(COOKIE_NAME, Long.toString(number))
                .httpOnly(true)
                .path("/")
                .maxAge(Duration.ofDays(3650))
                .sameSite("Lax")
                .secure(request.isSecure())
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
