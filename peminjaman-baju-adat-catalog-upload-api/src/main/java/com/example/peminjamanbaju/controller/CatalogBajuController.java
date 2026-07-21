package com.example.peminjamanbaju.controller;

import com.example.peminjamanbaju.dto.CatalogRequest;
import com.example.peminjamanbaju.dto.CatalogResponse;
import com.example.peminjamanbaju.dto.StokRequest;
import com.example.peminjamanbaju.service.CatalogBajuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/catalog")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CatalogBajuController {

    private final CatalogBajuService service;

    @GetMapping
    public List<CatalogResponse> findAll(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String daerah,
            @RequestParam(required = false) Boolean tersedia
    ) {
        return service.findAll(keyword, daerah, tersedia);
    }

    @GetMapping("/{id}")
    public CatalogResponse findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<CatalogResponse> create(
            @Valid @RequestBody CatalogRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(request));
    }

    @PutMapping("/{id}")
    public CatalogResponse update(
            @PathVariable Long id,
            @Valid @RequestBody CatalogRequest request
    ) {
        return service.update(id, request);
    }

    @PatchMapping("/{id}/stok")
    public CatalogResponse updateStok(
            @PathVariable Long id,
            @Valid @RequestBody StokRequest request
    ) {
        return service.updateStok(id, request.stok());
    }

    @PostMapping(
            value = "/{id}/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public CatalogResponse uploadImage(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file
    ) {
        return service.uploadImage(id, file);
    }

    @DeleteMapping("/{id}/image")
    public CatalogResponse deleteImage(@PathVariable Long id) {
        return service.deleteImage(id);
    }

    @DeleteMapping("/{id}")
    public Map<String, String> delete(@PathVariable Long id) {
        service.delete(id);

        return Map.of(
                "message",
                "Katalog baju berhasil dihapus"
        );
    }
}
