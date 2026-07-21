package com.example.peminjamanbaju.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "peminjaman_baju")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PeminjamanBaju {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nama_depan", nullable = false, length = 100)
    private String namaDepan;

    @Column(name = "nama_belakang", length = 100)
    private String namaBelakang;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String alamat;

    @Column(name = "nomor_whatsapp", nullable = false, length = 30)
    private String nomorWhatsapp;

    @Column(name = "catalog_id")
    private Long catalogId;

    @Column(name = "kode_baju", length = 30)
    private String kodeBaju;

    @Column(name = "jenis_baju", nullable = false, length = 150)
    private String jenisBaju;

    @Column(name = "harga_sewa", precision = 15, scale = 2)
    private BigDecimal hargaSewa;

    @Column(columnDefinition = "TEXT")
    private String catatan;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusPeminjaman status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        if (status == null) {
            status = StatusPeminjaman.MENUNGGU;
        }
    }
}
