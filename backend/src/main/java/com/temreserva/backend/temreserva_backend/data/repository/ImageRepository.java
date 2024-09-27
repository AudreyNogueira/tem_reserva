package com.temreserva.backend.temreserva_backend.data.repository;

import java.util.List;
import java.util.Optional;

import com.temreserva.backend.temreserva_backend.data.entity.Image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByName(String name);

    @Query(value = "SELECT * FROM TB_IMAGEM I WHERE I.ID_DONO = :imageOwnerId", nativeQuery = true)
    Optional<List<Image>> findByOwnerId(@Param("imageOwnerId") Long imageOwnerId);

    @Query(value = "SELECT * FROM TB_IMAGEM I WHERE I.ID_DONO = :imageOwnerId AND I.RESTAURANTE = :isRestaurant AND I.FOTO_PERFIL = :isProfilePic LIMIT 1", nativeQuery = true)
    Optional<Image> findImage(@Param("imageOwnerId") Long imageOwnerId, @Param("isProfilePic") Boolean isProfilePic, @Param("isRestaurant") Boolean isRestaurant);

    @Query(value = "SELECT * FROM TB_IMAGEM I WHERE I.ID_DONO = :imageOwnerId AND I.RESTAURANTE = :isRestaurant AND I.FOTO_PERFIL = :isProfilePic", nativeQuery = true)
    Optional<List<Image>> findRestaurantImages(@Param("imageOwnerId") Long imageOwnerId, @Param("isProfilePic") Boolean isProfilePic, @Param("isRestaurant") Boolean isRestaurant);
}
