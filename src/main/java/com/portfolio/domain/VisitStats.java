package com.portfolio.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "visit_stats")
public class VisitStats {

    @Id
    private Long id = 1L;

    @Column(nullable = false)
    private long totalVisitors = 0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getTotalVisitors() {
        return totalVisitors;
    }

    public void setTotalVisitors(long totalVisitors) {
        this.totalVisitors = totalVisitors;
    }
}
