package com.example.peminjamanbaju.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CatalogResponse(
        Long id,
        String kode,
        String nama,
        String daerah,
        String deskripsi,
        String ukuran,
        BigDecimal harga,
        Integer stok,
        Boolean tersedia,
        String imageUrl,
        Boolean aktif,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
