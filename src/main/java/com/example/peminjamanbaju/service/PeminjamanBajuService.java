package com.example.peminjamanbaju.service;

import com.example.peminjamanbaju.dto.PeminjamanRequest;
import com.example.peminjamanbaju.dto.PeminjamanResponse;
import com.example.peminjamanbaju.entity.CatalogBaju;
import com.example.peminjamanbaju.entity.PeminjamanBaju;
import com.example.peminjamanbaju.entity.StatusPeminjaman;
import com.example.peminjamanbaju.exception.DataTidakDitemukanException;
import com.example.peminjamanbaju.exception.ValidasiBisnisException;
import com.example.peminjamanbaju.repository.PeminjamanBajuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class    PeminjamanBajuService {

    private final PeminjamanBajuRepository repository;
    private final CatalogBajuService catalogService;

    @Value("${app.whatsapp-admin}")
    private String whatsappAdmin;

    @Transactional
    public PeminjamanResponse create(PeminjamanRequest request) {
        Long catalogId = null;
        String kodeBaju = null;
        String jenisBaju = clean(request.jenisBaju());
        BigDecimal hargaSewa = null;

        if (request.catalogId() != null) {
            CatalogBaju catalog =
                    catalogService.findActiveEntity(request.catalogId());

            if (catalog.getStok() <= 0) {
                throw new ValidasiBisnisException(
                        "Baju " + catalog.getNama() + " sedang tidak tersedia"
                );
            }

            catalogId = catalog.getId();
            kodeBaju = catalog.getKode();
            jenisBaju = catalog.getNama();
            hargaSewa = catalog.getHarga();
        }

        PeminjamanBaju data = PeminjamanBaju.builder()
                .namaDepan(request.namaDepan().trim())
                .namaBelakang(clean(request.namaBelakang()))
                .alamat(request.alamat().trim())
                .nomorWhatsapp(normalisasiNomorWhatsapp(
                        request.nomorWhatsapp()
                ))
                .catalogId(catalogId)
                .kodeBaju(kodeBaju)
                .jenisBaju(jenisBaju)
                .hargaSewa(hargaSewa)
                .catatan(clean(request.catatan()))
                .ukuran(clean(request.ukuran()))
                .lingkarDada(clean(request.lingkarDada()))
                .startDate(request.startDate().atStartOfDay())
                .endDate(request.endDate().atStartOfDay())
                .status(StatusPeminjaman.MENUNGGU)
                .build();

        return toResponse(repository.save(data));
    }

    @Transactional(readOnly = true)
    public List<PeminjamanResponse> findAll() {
        return repository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PeminjamanResponse findById(Long id) {
        return toResponse(findEntity(id));
    }

    @Transactional
    public PeminjamanResponse updateStatus(
            Long id,
            StatusPeminjaman status
    ) {
        PeminjamanBaju data = findEntity(id);
        data.setStatus(status);

        return toResponse(repository.save(data));
    }

    @Transactional
    public void delete(Long id) {
        PeminjamanBaju data = findEntity(id);
        repository.delete(data);
    }

    private PeminjamanBaju findEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new DataTidakDitemukanException(
                                "Data peminjaman dengan ID "
                                        + id
                                        + " tidak ditemukan"
                        )
                );
    }

    private PeminjamanResponse toResponse(PeminjamanBaju data) {
        String namaLengkap = data.getNamaDepan();

        if (
                data.getNamaBelakang() != null
                && !data.getNamaBelakang().isBlank()
        ) {
            namaLengkap += " " + data.getNamaBelakang();
        }

        return new PeminjamanResponse(
                data.getId(),
                namaLengkap,
                data.getAlamat(),
                data.getNomorWhatsapp(),
                data.getCatalogId(),
                data.getKodeBaju(),
                data.getJenisBaju(),
                data.getHargaSewa(),
                data.getCatatan(),
                data.getStartDate(),
                data.getEndDate(),
                data.getStatus(),
                data.getCreatedAt(),
                buatWhatsappUrl(data, namaLengkap)
        );
    }

    private String buatWhatsappUrl(
            PeminjamanBaju data,
            String namaLengkap
    ) {
        DateTimeFormatter dateFormatter =
                DateTimeFormatter.ofPattern("dd-MM-yyyy");

        NumberFormat currencyFormatter =
                NumberFormat.getCurrencyInstance(
                        Locale.forLanguageTag("id-ID")
                );

        String catatan = data.getCatatan();

        if (catatan == null || catatan.isBlank()) {
            catatan = "-";
        }

        String kode = data.getKodeBaju() == null
                ? "-"
                : data.getKodeBaju();

        String harga = data.getHargaSewa() == null
                ? "-"
                : currencyFormatter.format(data.getHargaSewa());

        String message = """
                Halo, saya ingin melakukan peminjaman baju adat.

                ID Pesanan: %s
                Nama: %s
                Alamat: %s
                Nomor WhatsApp: %s
                Kode Baju: %s
                Pilihan Baju: %s
                Ukuran: %s
                Lingkar Dada: %s cm
                Tanggal Mulai: %s
                Tanggal Selesai: %s
                Catatan: %s
                """.formatted(
                data.getId(),
                namaLengkap,
                data.getAlamat(),
                data.getNomorWhatsapp(),
                kode,
                data.getJenisBaju(),
                data.getUkuran(),
                data.getLingkarDada(),
                data.getStartDate(),
                data.getEndDate(),
                catatan
        );

        String encodedMessage = URLEncoder.encode(
                message,
                StandardCharsets.UTF_8
        ).replace("+", "%20");

        return "https://wa.me/"
                + normalisasiNomorWhatsapp(whatsappAdmin)
                + "?text="
                + encodedMessage;
    }

    private String normalisasiNomorWhatsapp(String nomor) {
        if (nomor == null) {
            return "";
        }
        if (!nomor.matches("^[0-9+()\\s-]+$")) {
            throw new IllegalArgumentException(
                    "Nomor WhatsApp tidak boleh mengandung huruf"
            );
        }


        String hasil = nomor.replaceAll("[^0-9]", "");

        if (hasil.startsWith("0")) {
            hasil = "62" + hasil.substring(1);
        }

        return hasil;
    }

    private String clean(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }
}
