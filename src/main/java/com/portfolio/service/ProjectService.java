package com.portfolio.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.portfolio.domain.Project;
import com.portfolio.domain.ProjectImage;
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
        project.getGalleryImages().size();
        return project;
    }

    @Transactional
    public Project save(
            ProjectForm form,
            MultipartFile coverFile,
            List<MultipartFile> galleryFiles,
            List<Long> removeGalleryIds
    ) {
        Project project = form.getId() == null ? new Project() : get(form.getId());
        form.applyTo(project);

        String stored = storageService.store(coverFile);
        if (stored != null) {
            storageService.deleteIfStored(project.getCoverImage());
            project.setCoverImage(stored);
        } else if (form.getCoverImage() != null && !form.getCoverImage().isBlank()) {
            project.setCoverImage(form.getCoverImage());
        }

        if (removeGalleryIds != null && !removeGalleryIds.isEmpty()) {
            Set<Long> remove = new HashSet<>(removeGalleryIds);
            project.getGalleryImages().removeIf(image -> {
                if (image.getId() == null || !remove.contains(image.getId())) {
                    return false;
                }
                storageService.deleteIfStored(image.getImageUrl());
                return true;
            });
        }

        int nextOrder = project.getGalleryImages().size();
        if (galleryFiles != null) {
            for (MultipartFile file : galleryFiles) {
                if (file == null || file.isEmpty()) {
                    continue;
                }
                if (project.getGalleryImages().size() >= 20) {
                    throw new IllegalArgumentException("A project can have at most 20 detail images");
                }
                String url = storageService.store(file);
                if (url == null) {
                    continue;
                }
                ProjectImage image = new ProjectImage();
                image.setImageUrl(url);
                image.setSortOrder(nextOrder++);
                project.addGalleryImage(image);
            }
        }

        return projectRepository.save(project);
    }

    @Transactional
    public void delete(Long id) {
        Project project = get(id);
        storageService.deleteIfStored(project.getCoverImage());
        project.getGalleryImages().forEach(image -> storageService.deleteIfStored(image.getImageUrl()));
        projectRepository.delete(project);
    }
}
