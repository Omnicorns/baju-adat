package com.example.peminjamanbaju.repository;

import com.example.peminjamanbaju.entity.CatalogBaju;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CatalogBajuRepository
        extends JpaRepository<CatalogBaju, Long> {

    boolean existsByKodeIgnoreCase(String kode);

    boolean existsByKodeIgnoreCaseAndIdNot(String kode, Long id);

    Optional<CatalogBaju> findByIdAndAktifTrue(Long id);
}
