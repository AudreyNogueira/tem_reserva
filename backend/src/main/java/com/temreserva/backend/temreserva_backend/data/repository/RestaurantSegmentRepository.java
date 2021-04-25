package com.temreserva.backend.temreserva_backend.data.repository;

import com.temreserva.backend.temreserva_backend.data.entity.RestaurantSegment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantSegmentRepository extends JpaRepository<RestaurantSegment, Long> {
    
}
