package com.example.peminjamanbaju.service;

import com.example.peminjamanbaju.dto.CatalogRequest;
import com.example.peminjamanbaju.dto.CatalogResponse;
import com.example.peminjamanbaju.entity.CatalogBaju;
import com.example.peminjamanbaju.exception.DataTidakDitemukanException;
import com.example.peminjamanbaju.exception.ValidasiBisnisException;
import com.example.peminjamanbaju.repository.CatalogBajuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class CatalogBajuService {

    private final CatalogBajuRepository repository;
    private final CatalogImageStorageService imageStorageService;

    @Transactional(readOnly = true)
    public List<CatalogResponse> findAll(
            String keyword,
            String daerah,
            Boolean tersedia
    ) {
        String normalizedKeyword = normalize(keyword);
        String normalizedDaerah = normalize(daerah);

        return repository.findAll()
                .stream()
                .filter(CatalogBaju::getAktif)
                .filter(item ->
                        normalizedKeyword == null
                                || containsIgnoreCase(
                                        item.getKode(),
                                        normalizedKeyword
                                )
                                || containsIgnoreCase(
                                        item.getNama(),
                                        normalizedKeyword
                                )
                                || containsIgnoreCase(
                                        item.getDaerah(),
                                        normalizedKeyword
                                )
                )
                .filter(item ->
                        normalizedDaerah == null
                                || item.getDaerah().equalsIgnoreCase(
                                        normalizedDaerah
                                )
                )
                .filter(item ->
                        tersedia == null
                                || tersedia.equals(item.getStok() > 0)
                )
                .sorted(
                        Comparator.comparing(
                                CatalogBaju::getNama,
                                String.CASE_INSENSITIVE_ORDER
                        )
                )
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CatalogResponse findById(Long id) {
        return toResponse(findEntity(id));
    }

    @Transactional
    public CatalogResponse create(CatalogRequest request) {
        String kode = request.kode().trim().toUpperCase(Locale.ROOT);

        if (repository.existsByKodeIgnoreCase(kode)) {
            throw new ValidasiBisnisException(
                    "Kode katalog " + kode + " sudah digunakan"
            );
        }

        CatalogBaju data = CatalogBaju.builder()
                .kode(kode)
                .nama(request.nama().trim())
                .daerah(request.daerah().trim())
                .deskripsi(clean(request.deskripsi()))
                .ukuran(clean(request.ukuran()))
                .harga(request.harga())
                .stok(request.stok())
                .imageUrl(clean(request.imageUrl()))
                .aktif(request.aktif() == null || request.aktif())
                .build();

        return toResponse(repository.save(data));
    }

    @Transactional
    public CatalogResponse update(
            Long id,
            CatalogRequest request
    ) {
        CatalogBaju data = findEntity(id);
        String kode = request.kode().trim().toUpperCase(Locale.ROOT);

        if (repository.existsByKodeIgnoreCaseAndIdNot(kode, id)) {
            throw new ValidasiBisnisException(
                    "Kode katalog " + kode + " sudah digunakan"
            );
        }

        data.setKode(kode);
        data.setNama(request.nama().trim());
        data.setDaerah(request.daerah().trim());
        data.setDeskripsi(clean(request.deskripsi()));
        data.setUkuran(clean(request.ukuran()));
        data.setHarga(request.harga());
        data.setStok(request.stok());
        data.setImageUrl(clean(request.imageUrl()));

        if (request.aktif() != null) {
            data.setAktif(request.aktif());
        }

        return toResponse(repository.save(data));
    }

    @Transactional
    public CatalogResponse updateStok(Long id, Integer stok) {
        CatalogBaju data = findEntity(id);
        data.setStok(stok);

        return toResponse(repository.save(data));
    }

    @Transactional
    public CatalogResponse uploadImage(Long id, MultipartFile file) {
        CatalogBaju data = findEntity(id);
        String oldImageUrl = data.getImageUrl();
        String newImageUrl = imageStorageService.save(file);

        try {
            data.setImageUrl(newImageUrl);
            CatalogResponse response = toResponse(repository.save(data));
            imageStorageService.deleteByUrl(oldImageUrl);
            return response;
        } catch (RuntimeException exception) {
            imageStorageService.deleteByUrl(newImageUrl);
            throw exception;
        }
    }

    @Transactional
    public CatalogResponse deleteImage(Long id) {
        CatalogBaju data = findEntity(id);
        String oldImageUrl = data.getImageUrl();

        data.setImageUrl(null);
        CatalogResponse response = toResponse(repository.save(data));
        imageStorageService.deleteByUrl(oldImageUrl);

        return response;
    }

    @Transactional
    public void delete(Long id) {
        CatalogBaju data = findEntity(id);
        String oldImageUrl = data.getImageUrl();

        repository.delete(data);
        imageStorageService.deleteByUrl(oldImageUrl);
    }

    @Transactional(readOnly = true)
    public CatalogBaju findActiveEntity(Long id) {
        return repository.findByIdAndAktifTrue(id)
                .orElseThrow(() ->
                        new DataTidakDitemukanException(
                                "Katalog baju dengan ID "
                                        + id
                                        + " tidak ditemukan atau tidak aktif"
                        )
                );
    }

    private CatalogBaju findEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new DataTidakDitemukanException(
                                "Katalog baju dengan ID "
                                        + id
                                        + " tidak ditemukan"
                        )
                );
    }

    private CatalogResponse toResponse(CatalogBaju data) {
        return new CatalogResponse(
                data.getId(),
                data.getKode(),
                data.getNama(),
                data.getDaerah(),
                data.getDeskripsi(),
                data.getUkuran(),
                data.getHarga(),
                data.getStok(),
                data.getStok() > 0,
                data.getImageUrl(),
                data.getAktif(),
                data.getCreatedAt(),
                data.getUpdatedAt()
        );
    }

    private String clean(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }

    private String normalize(String value) {
        String cleaned = clean(value);
        return cleaned == null
                ? null
                : cleaned.toLowerCase(Locale.ROOT);
    }

    private boolean containsIgnoreCase(
            String source,
            String keyword
    ) {
        return source != null
                && source.toLowerCase(Locale.ROOT)
                .contains(keyword);
    }
}
