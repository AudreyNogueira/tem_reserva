package com.temreserva.backend.temreserva_backend.data.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.temreserva.backend.temreserva_backend.data.entity.Reserve;
import com.temreserva.backend.temreserva_backend.data.entity.Restaurant;
import com.temreserva.backend.temreserva_backend.data.entity.User;
import com.temreserva.backend.temreserva_backend.web.model.interfaces.ICurrentPeopleModel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReserveRepository extends JpaRepository<Reserve, Long> {

    @Query(value = "SELECT TOP 1 * FROM TB_RESERVA R WHERE R.ID_USUARIO = :idUser AND PERIODO = :period AND CAST(DATA_RESERVA AS DATE) = CAST(:date AS DATE)", nativeQuery = true)
    public Optional<Reserve> existsByPeriodDateAndUser(@Param("idUser") Long idUser, @Param("period") String period, @Param("date") LocalDateTime date);

    @Query(value = "SELECT isnull(SUM(QTD_PESSOAS), 0) FROM TB_RESERVA R WHERE R.ID_RESTAURANTE = :idRestaurant AND PERIODO = :period AND CAST(DATA_RESERVA AS DATE) = CAST(:date AS DATE)", nativeQuery = true)
    public Integer findNumberOfPeopleByRestaurantPeriodAndDate(@Param("idRestaurant") Long idRestaurant, @Param("period") String period, @Param("date") LocalDateTime date);

    public List<Reserve> findByRestaurant(Restaurant restaurant);

    public List<Reserve> findByUser(User user);

    @Query(value = "SELECT * FROM TB_RESERVA R WHERE R.ID_USUARIO = :idUser AND CAST(DATA_RESERVA AS DATE) = CAST(:date AS DATE)", nativeQuery = true)
    public List<Reserve> findByUserAndDate(Long idUser, @Param("date") LocalDateTime date);

    @Query(value = "SELECT * FROM TB_RESERVA R WHERE R.ID_RESTAURANTE = :idRestaurant AND CAST(DATA_RESERVA AS DATE) = CAST(:date AS DATE)", nativeQuery = true)
    public List<Reserve> findByRestaurantCurrentDay(Long idRestaurant, @Param("date") LocalDateTime date);

    @Query(value = "SELECT isnull(SUM(R.QTD_PESSOAS), 0) AS currentPeople, PERIODO AS period FROM TB_RESERVA R WHERE R.ID_RESTAURANTE = :idRestaurant AND CAST(R.DATA_RESERVA AS DATE) = :date GROUP BY R.PERIODO", nativeQuery = true)
    public List<ICurrentPeopleModel> findCurrentPeopleModelByRestaurant(@Param("idRestaurant") Long idRestaurant, @Param("date") LocalDate date);
}
