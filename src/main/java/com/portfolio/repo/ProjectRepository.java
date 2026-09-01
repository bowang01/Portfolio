package com.portfolio.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.portfolio.domain.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findAllByOrderBySortOrderAscCreatedAtDesc();
}
