package com.example.peminjamanbaju.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PeminjamanRequest(

        @NotBlank(message = "Nama depan wajib diisi")
        @Size(max = 100, message = "Nama depan maksimal 100 karakter")
        String namaDepan,

        @Size(max = 100, message = "Nama belakang maksimal 100 karakter")
        String namaBelakang,

        @NotBlank(message = "Alamat wajib diisi")
        String alamat,


        String lingkarDada,

        String lingkarPinggang,

        @NotBlank(message = "Jenis Kelamin wajib diisi")
        String jenisKelamin,

        @NotBlank(message = "Kualitas wajib diisi")
        String kualitas,

        @NotBlank(message = "ukuran")
        String ukuran,

        @NotBlank(message = "Nomor WhatsApp wajib diisi")
        @Size(max = 30, message = "Nomor WhatsApp maksimal 30 karakter")
        String nomorWhatsapp,

        Long catalogId,

        @Size(max = 150, message = "Jenis baju maksimal 150 karakter")
        String jenisBaju,

        String catatan,

        @NotNull(message = "Tanggal mulai wajib diisi")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate startDate,

        @NotNull(message = "Tanggal selesai wajib diisi")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate endDate
) {
    @AssertTrue(message = "Tanggal selesai harus setelah tanggal mulai")
    public boolean isRentangTanggalValid() {
        if (startDate == null || endDate == null) {
            return true;
        }

        return endDate.isAfter(startDate);
    }

    @AssertTrue(message = "Pilih katalog baju atau isi jenis baju")
    public boolean isPilihanBajuValid() {
        return catalogId != null
                || (jenisBaju != null && !jenisBaju.isBlank());
    }
}
