package com.example.peminjamanbaju.dto;

import com.example.peminjamanbaju.entity.StatusPeminjaman;
import jakarta.validation.constraints.NotNull;

public record StatusRequest(
        @NotNull(message = "Status wajib diisi")
        StatusPeminjaman status
) {
}
