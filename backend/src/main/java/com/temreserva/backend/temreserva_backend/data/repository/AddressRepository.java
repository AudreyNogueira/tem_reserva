package com.temreserva.backend.temreserva_backend.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.temreserva.backend.temreserva_backend.data.entity.Address;
import com.temreserva.backend.temreserva_backend.data.entity.Restaurant;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {    
    public Address findByRestaurant(Restaurant restaurant);

    @Query(value = "SELECT DISTINCT(E.ZONA) FROM TB_ENDERECO E", nativeQuery = true)
    public List<String> findByDistinctZoneNames();

    @Query(value = "SELECT * FROM TB_ENDERECO E WHERE E.ZONA LIKE %:zone% LIMIT 4", nativeQuery = true)
    public List<Address> findByZoneForHome(@Param("zone") String zone);

    public List<Address> findByZone(String zone);
}
