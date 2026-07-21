package com.example.peminjamanbaju.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record StokRequest(
        @NotNull(message = "Stok wajib diisi")
        @Min(value = 0, message = "Stok tidak boleh negatif")
        Integer stok
) {
}
