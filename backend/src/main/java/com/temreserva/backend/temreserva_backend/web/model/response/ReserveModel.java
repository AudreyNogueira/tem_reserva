package com.temreserva.backend.temreserva_backend.web.model.response;

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

    private String period;

    private LocalDateTime reserveDate;

    private Integer amountOfPeople;

    private String observation;

    private Boolean confirmed;

    private UserModel user;

    private RestaurantModel restaurant;
}
