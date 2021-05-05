package com.temreserva.backend.temreserva_backend.web.model.Responses;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReserveModel {
    private Long id;
    
    private Long idUser;

    private Long idRestaurant;

    private String period;

    private LocalDateTime reserveDate;

    private Integer amountOfPeople;
}
