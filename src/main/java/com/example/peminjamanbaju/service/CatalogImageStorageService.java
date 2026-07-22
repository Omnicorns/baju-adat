package com.example.peminjamanbaju.service;

import com.example.peminjamanbaju.exception.ValidasiBisnisException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;


@Service
public class CatalogImageStorageService {

    private static final long MAX_FILE_SIZE =
            5L * 1024L * 1024L;

    private static final Map<String, String> ALLOWED_TYPES =
            Map.of(
                    "image/jpeg", ".jpg",
                    "image/png", ".png",
                    "image/webp", ".webp"
            );

    private static final String PUBLIC_PATH =
            "/uploads/catalog/";

    private final Path uploadRoot;

    public CatalogImageStorageService(
            @Value("${app.upload-directory}")
            String uploadDirectory
    ) {
        /*
         * Root property:
         * /opt/peminjaman-baju-adat/uploads
         *
         * File katalog disimpan ke:
         * /opt/peminjaman-baju-adat/uploads/catalog
         */
        this.uploadRoot = Paths
                .get(uploadDirectory)
                .resolve("catalog")
                .toAbsolutePath()
                .normalize();
    }

    public String save(MultipartFile file) {
        validate(file);

        String contentType = file.getContentType();
        String extension = ALLOWED_TYPES.get(contentType);

        String filename =
                UUID.randomUUID() + extension;

        Path target = uploadRoot
                .resolve(filename)
                .normalize();

        if (!target.startsWith(uploadRoot)) {
            throw new ValidasiBisnisException(
                    "Lokasi file tidak valid"
            );
        }

        try {
            Files.createDirectories(uploadRoot);

            try (
                    InputStream inputStream =
                            file.getInputStream()
            ) {
                Files.copy(
                        inputStream,
                        target,
                        StandardCopyOption.REPLACE_EXISTING
                );
            }

            if (!Files.exists(target)) {
                throw new IOException(
                        "File tidak ditemukan setelah disimpan"
                );
            }

            return PUBLIC_PATH + filename;

        } catch (IOException exception) {
            throw new ValidasiBisnisException(
                    "Gambar gagal disimpan: "
                            + exception.getMessage()
            );
        }
    }

    public void deleteByUrl(String imageUrl) {
        if (
                imageUrl == null ||
                        !imageUrl.startsWith(PUBLIC_PATH)
        ) {
            return;
        }

        String filename =
                imageUrl.substring(
                        PUBLIC_PATH.length()
                );

        if (
                filename.isBlank() ||
                        filename.contains("/") ||
                        filename.contains("\\")
        ) {
            return;
        }

        Path target = uploadRoot
                .resolve(filename)
                .normalize();

        if (!target.startsWith(uploadRoot)) {
            return;
        }

        try {
            Files.deleteIfExists(target);
        } catch (IOException ignored) {
            /*
             * Data katalog tetap diproses walaupun
             * gambar lama gagal dihapus.
             */
        }
    }

    private void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ValidasiBisnisException(
                    "File gambar wajib dipilih"
            );
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new ValidasiBisnisException(
                    "Ukuran gambar maksimal 5 MB"
            );
        }

        String contentType = file.getContentType();

        if (
                contentType == null ||
                        !ALLOWED_TYPES.containsKey(contentType)
        ) {
            throw new ValidasiBisnisException(
                    "Format gambar harus JPG, PNG, atau WEBP"
            );
        }
    }
}