package com.example.peminjamanbaju.config;

import com.example.peminjamanbaju.entity.CatalogBaju;
import com.example.peminjamanbaju.repository.CatalogBajuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CatalogDataInitializer implements CommandLineRunner {

    private final CatalogBajuRepository repository;

    @Override
    public void run(String... args) {
        if (repository.count() > 0) {
            return;
        }

        repository.saveAll(
                List.of(
                        create(
                                "JWA-001",
                                "Beskap Jawa Cokelat",
                                "Jawa",
                                "Beskap pria lengkap untuk acara formal.",
                                "M, L, XL",
                                150000,
                                3,
                                "/catalog/beskap-jawa.svg"
                        ),
                        create(
                                "JWA-002",
                                "Kebaya Jawa Merah",
                                "Jawa",
                                "Kebaya wanita dengan kain dan selendang.",
                                "S, M, L",
                                175000,
                                2,
                                "/catalog/kebaya-jawa.svg"
                        ),
                        create(
                                "BLI-001",
                                "Pakaian Adat Bali",
                                "Bali",
                                "Set pakaian adat Bali untuk pria atau wanita.",
                                "M, L, XL",
                                180000,
                                4,
                                "/catalog/baju-bali.svg"
                        ),
                        create(
                                "BTK-001",
                                "Pakaian Batak Ulos",
                                "Batak",
                                "Pakaian adat Batak dilengkapi kain ulos.",
                                "M, L",
                                165000,
                                2,
                                "/catalog/baju-batak.svg"
                        ),
                        create(
                                "ACH-001",
                                "Pakaian Adat Aceh",
                                "Aceh",
                                "Pakaian tradisional Aceh untuk acara khusus.",
                                "M, L, XL",
                                170000,
                                1,
                                "/catalog/baju-aceh.svg"
                        )
                )
        );
    }

    private CatalogBaju create(
            String kode,
            String nama,
            String daerah,
            String deskripsi,
            String ukuran,
            int harga,
            int stok,
            String imageUrl
    ) {
        return CatalogBaju.builder()
                .kode(kode)
                .nama(nama)
                .daerah(daerah)
                .deskripsi(deskripsi)
                .ukuran(ukuran)
                .harga(BigDecimal.valueOf(harga))
                .stok(stok)
                .imageUrl(imageUrl)
                .aktif(true)
                .build();
    }
}
