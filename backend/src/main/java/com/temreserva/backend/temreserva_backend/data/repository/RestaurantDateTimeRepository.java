package com.temreserva.backend.temreserva_backend.data.repository;

import java.util.List;

import com.temreserva.backend.temreserva_backend.data.entity.Restaurant;
import com.temreserva.backend.temreserva_backend.data.entity.RestaurantDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantDateTimeRepository extends JpaRepository<RestaurantDateTime, Long>{

    List<RestaurantDateTime> findByRestaurant(Restaurant restaurant);
    
}
