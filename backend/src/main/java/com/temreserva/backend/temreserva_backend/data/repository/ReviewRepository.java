package com.temreserva.backend.temreserva_backend.data.repository;

import com.temreserva.backend.temreserva_backend.data.entity.Review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query(value = "SELECT SUM(NOTA_AVALIACAO)/COUNT(*) AS RESULT FROM TB_AVALIACAO R WHERE ID_RESTAURANTE = :idRestaurant GROUP BY ID_RESTAURANTE", nativeQuery = true)
    public Integer getUpdatedAverageStars(@Param("idRestaurant") Long idRestaurant);

    @Query(value = "SELECT TOP 1 * AS RESULT FROM TB_AVALIACAO R WHERE ID_RESERVA = :idReserve AND ID_USUARIO = :idUser AND ID_RESTAURANTE = :idRestaurant GROUP BY ID_RESTAURANTE", nativeQuery = true)
    public Review existsByReserveUserRestaurant(Long idReserve, Long idUser, Long idRestaurant);
}
