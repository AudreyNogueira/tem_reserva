package com.temreserva.backend.temreserva_backend.web.controllers;

import javax.validation.Valid;

import com.temreserva.backend.temreserva_backend.business.ReserveBusiness;
import com.temreserva.backend.temreserva_backend.data.repository.ReserveRepository;
import com.temreserva.backend.temreserva_backend.data.repository.RestaurantRepository;
import com.temreserva.backend.temreserva_backend.data.repository.UserRepository;
import com.temreserva.backend.temreserva_backend.web.model.DTOs.ReserveDTO;
import com.temreserva.backend.temreserva_backend.web.model.Responses.ReserveModel;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/reserve")
public class ReserveController {
    public final ReserveBusiness business;

    public ReserveController(ReserveRepository reserveRepository, RestaurantRepository restaurantRepository, UserRepository userRepository) {
        business = new ReserveBusiness(reserveRepository, restaurantRepository, userRepository);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ReserveModel createNewReserve(@RequestBody @Valid ReserveDTO dto) {
        return business.createNewReserve(dto);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReserve( @PathVariable Long id ){
        business.deleteReserve(id);
    }
}
