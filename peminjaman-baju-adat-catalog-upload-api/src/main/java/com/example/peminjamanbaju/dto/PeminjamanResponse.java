package com.example.peminjamanbaju.dto;

import com.example.peminjamanbaju.entity.StatusPeminjaman;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PeminjamanResponse(
        Long id,
        String namaLengkap,
        String alamat,
        String nomorWhatsapp,
        Long catalogId,
        String kodeBaju,
        String jenisBaju,
        BigDecimal hargaSewa,
        String catatan,
        LocalDateTime startDate,
        LocalDateTime endDate,
        StatusPeminjaman status,
        LocalDateTime createdAt,
        String whatsappUrl
) {
}
