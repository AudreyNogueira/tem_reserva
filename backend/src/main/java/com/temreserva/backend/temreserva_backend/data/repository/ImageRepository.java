package com.temreserva.backend.temreserva_backend.data.repository;

import java.util.Optional;

import com.temreserva.backend.temreserva_backend.data.entity.Image;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByName(String name);
}
