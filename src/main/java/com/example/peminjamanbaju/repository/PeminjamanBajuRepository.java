package com.example.peminjamanbaju.repository;

import com.example.peminjamanbaju.entity.PeminjamanBaju;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PeminjamanBajuRepository
        extends JpaRepository<PeminjamanBaju, Long> {

    List<PeminjamanBaju> findAllByOrderByCreatedAtDesc();
}
