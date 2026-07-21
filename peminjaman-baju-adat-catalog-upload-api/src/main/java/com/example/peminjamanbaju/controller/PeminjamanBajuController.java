package com.example.peminjamanbaju.controller;

import com.example.peminjamanbaju.dto.PeminjamanRequest;
import com.example.peminjamanbaju.dto.PeminjamanResponse;
import com.example.peminjamanbaju.dto.StatusRequest;
import com.example.peminjamanbaju.service.PeminjamanBajuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/baju-adat/api/peminjaman")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PeminjamanBajuController {

    private final PeminjamanBajuService service;

    @PostMapping
    public ResponseEntity<PeminjamanResponse> create(
            @Valid @RequestBody PeminjamanRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(request));
    }

    @GetMapping
    public List<PeminjamanResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public PeminjamanResponse findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}/status")
    public PeminjamanResponse updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody StatusRequest request
    ) {
        return service.updateStatus(id, request.status());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(
            @PathVariable Long id
    ) {
        service.delete(id);

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Data peminjaman berhasil dihapus"
                )
        );
    }
}
