package com.portfolio.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.portfolio.config.AppProperties;

@Service
public class StorageService {

    private static final Set<String> ALLOWED = Set.of("image/jpeg", "image/png", "image/webp", "image/gif");

    private final Path uploadDir;

    public StorageService(AppProperties properties) {
        this.uploadDir = Path.of(properties.getUploadDir()).toAbsolutePath().normalize();
    }

    public String store(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        String contentType = file.getContentType() == null ? "" : file.getContentType().toLowerCase(Locale.ROOT);
        if (!ALLOWED.contains(contentType)) {
            throw new IllegalArgumentException("Only JPG, PNG, WEBP, or GIF images are allowed");
        }
        try {
            Files.createDirectories(uploadDir);
            String ext = extension(file.getOriginalFilename(), contentType);
            String filename = UUID.randomUUID().toString().replace("-", "") + ext;
            Path target = uploadDir.resolve(filename);
            file.transferTo(target);
            return "/uploads/" + filename;
        } catch (IOException ex) {
            throw new IllegalStateException("Could not save the image", ex);
        }
    }

    public void deleteIfStored(String publicPath) {
        if (publicPath == null || !publicPath.startsWith("/uploads/")) {
            return;
        }
        Path file = uploadDir.resolve(publicPath.substring("/uploads/".length())).normalize();
        if (!file.startsWith(uploadDir)) {
            return;
        }
        try {
            Files.deleteIfExists(file);
        } catch (IOException ignored) {
            // keep going if the old file is already gone
        }
    }

    private static String extension(String original, String contentType) {
        if (original != null) {
            int dot = original.lastIndexOf('.');
            if (dot >= 0) {
                return original.substring(dot).toLowerCase(Locale.ROOT);
            }
        }
        return switch (contentType) {
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            case "image/gif" -> ".gif";
            default -> ".jpg";
        };
    }
}
