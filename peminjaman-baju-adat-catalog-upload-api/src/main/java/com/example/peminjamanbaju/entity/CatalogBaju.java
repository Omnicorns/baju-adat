package com.example.peminjamanbaju.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "catalog_baju",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_catalog_baju_kode",
                        columnNames = "kode"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CatalogBaju {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String kode;

    @Column(nullable = false, length = 150)
    private String nama;

    @Column(nullable = false, length = 100)
    private String daerah;

    @Column(columnDefinition = "TEXT")
    private String deskripsi;

    @Column(length = 100)
    private String ukuran;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal harga;

    @Column(nullable = false)
    private Integer stok;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(nullable = false)
    private Boolean aktif;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();

        if (aktif == null) {
            aktif = true;
        }

        if (stok == null) {
            stok = 0;
        }

        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
