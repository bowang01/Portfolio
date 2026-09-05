package com.portfolio.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portfolio.domain.VisitStats;
import com.portfolio.repo.VisitStatsRepository;

@Service
public class VisitorService {

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

    @Transactional(readOnly = true)
    public long currentTotal() {
        return visitStatsRepository.findById(1L)
                .map(VisitStats::getTotalVisitors)
                .orElse(0L);
    }

    @Transactional
    public long incrementAndGet() {
        VisitStats stats = visitStatsRepository.lockById(1L).orElseGet(() -> {
            VisitStats created = new VisitStats();
            created.setId(1L);
            created.setTotalVisitors(0);
            return visitStatsRepository.save(created);
        });
        long next = stats.getTotalVisitors() + 1;
        stats.setTotalVisitors(next);
        visitStatsRepository.save(stats);
        return next;
    }
}
