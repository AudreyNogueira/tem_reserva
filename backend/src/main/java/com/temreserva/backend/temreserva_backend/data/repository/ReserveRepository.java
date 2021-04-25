package com.temreserva.backend.temreserva_backend.data.repository;

import com.temreserva.backend.temreserva_backend.data.entity.Reserve;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReserveRepository extends JpaRepository<Reserve, Long> {
    
}
