package com.temreserva.backend.temreserva_backend.web.model.dto;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReserveDTO {
    private Long idUser;

    private Long idRestaurant;

    private String period;

    private LocalDateTime reserveDate;

    private Integer amountOfPeople;

    private String observation;
}
