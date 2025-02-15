package com.temreserva.backend.temreserva_backend.data.repository;

import java.util.List;

import com.temreserva.backend.temreserva_backend.data.entity.Credential;
import com.temreserva.backend.temreserva_backend.data.entity.Restaurant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    @Query(value = "SELECT * FROM TB_RESTAURANTE R WHERE R.NOME LIKE %:name%", nativeQuery = true)
    public List<Restaurant> findByName(@Param("name") String name);

    public Restaurant findByCredential(Credential restaurantCredentials);

    @Query(value = "SELECT TOP :size * FROM TB_RESTAURANTE R", nativeQuery = true)
    public List<Restaurant> findBySize(@Param("size") Integer size);

    @Query(value = "SELECT TOP :size * FROM TB_RESTAURANTE R ORDER BY R.NOTA_AVALIACAO_MEDIA DESC", nativeQuery = true)
    public List<Restaurant> findByAverageStars(@Param("size") Integer size);
}
