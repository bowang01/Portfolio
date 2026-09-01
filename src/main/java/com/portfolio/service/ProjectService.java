package com.portfolio.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.portfolio.domain.Project;
import com.portfolio.repo.ProjectRepository;
import com.portfolio.web.dto.ProjectForm;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final StorageService storageService;

    public ProjectService(ProjectRepository projectRepository, StorageService storageService) {
        this.projectRepository = projectRepository;
        this.storageService = storageService;
    }

    @Transactional(readOnly = true)
    public List<Project> listAll() {
        return projectRepository.findAllByOrderBySortOrderAscCreatedAtDesc();
    }

    @Transactional(readOnly = true)
    public Project get(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
        project.getTestAccounts().size();
        project.getTestEndpoints().size();
        return project;
    }

    @Transactional
    public Project save(ProjectForm form, MultipartFile coverFile) {
        Project project = form.getId() == null ? new Project() : get(form.getId());
        form.applyTo(project);

        String stored = storageService.store(coverFile);
        if (stored != null) {
            storageService.deleteIfStored(project.getCoverImage());
            project.setCoverImage(stored);
        } else if (form.getCoverImage() != null && !form.getCoverImage().isBlank()) {
            project.setCoverImage(form.getCoverImage());
        }

        return projectRepository.save(project);
    }

    @Transactional
    public void delete(Long id) {
        Project project = get(id);
        storageService.deleteIfStored(project.getCoverImage());
        projectRepository.delete(project);
    }
}
