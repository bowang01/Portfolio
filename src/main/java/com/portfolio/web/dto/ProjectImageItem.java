package com.portfolio.web.dto;

import com.portfolio.domain.ProjectImage;

public class ProjectImageItem {

    private Long id;
    private String imageUrl;

    public static ProjectImageItem from(ProjectImage image) {
        ProjectImageItem item = new ProjectImageItem();
        item.id = image.getId();
        item.imageUrl = image.getImageUrl();
        return item;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
