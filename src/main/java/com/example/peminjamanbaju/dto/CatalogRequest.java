package com.example.peminjamanbaju.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CatalogRequest(

        @NotBlank(message = "Kode katalog wajib diisi")
        @Size(max = 30, message = "Kode katalog maksimal 30 karakter")
        String kode,

        @NotBlank(message = "Nama baju wajib diisi")
        @Size(max = 150, message = "Nama baju maksimal 150 karakter")
        String nama,

        @NotBlank(message = "Daerah asal wajib diisi")
        @Size(max = 100, message = "Daerah maksimal 100 karakter")
        String daerah,

        String deskripsi,

        @Size(max = 100, message = "Ukuran maksimal 100 karakter")
        String ukuran,

        @NotNull(message = "Harga wajib diisi")
        @DecimalMin(value = "0.0", inclusive = true, message = "Harga tidak boleh negatif")
        BigDecimal harga,

        @NotNull(message = "Stok wajib diisi")
        @Min(value = 0, message = "Stok tidak boleh negatif")
        Integer stok,

        @Size(max = 500, message = "Image URL maksimal 500 karakter")
        String imageUrl,

        Boolean aktif
) {
}
