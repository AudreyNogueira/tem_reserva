package com.temreserva.backend.temreserva_backend.web.model.DTOs;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReserveDTO {
    @NotNull(message = "ID do usuário não pode ser nulo")
    private Long idUser;

    @NotNull(message = "ID do restaurante não pode ser nulo")
    private Long idRestaurant;

    @NotNull(message = "Data de reserva não pode ser nulo")
    private LocalDateTime reserveDate;

    @NotNull(message = "Número de pessoas não pode ser nulo")
    private Integer amountOfPeople;
}
