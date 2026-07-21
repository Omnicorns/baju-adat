package com.example.peminjamanbaju.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class UploadResourceConfig   implements WebMvcConfigurer {
    @Value("${app.upload-directory}")
    private String uploadDirectory;

    @Override
    public void addResourceHandlers(
            ResourceHandlerRegistry registry
    ) {
        Path uploadPath = Paths.get(uploadDirectory)
                .toAbsolutePath()
                .normalize();

        String resourceLocation =
                uploadPath.toUri().toString();

        if (!resourceLocation.endsWith("/")) {
            resourceLocation += "/";
        }

        registry
                .addResourceHandler(
                        "/baju-adat/uploads/**"
                )
                .addResourceLocations(
                        resourceLocation
                );
    }
}