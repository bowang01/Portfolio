package com.portfolio.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.portfolio.domain.VisitStats;

import jakarta.persistence.LockModeType;

public interface VisitStatsRepository extends JpaRepository<VisitStats, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select v from VisitStats v where v.id = :id")
    Optional<VisitStats> lockById(@Param("id") Long id);
}
