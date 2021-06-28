package com.temreserva.backend.temreserva_backend.web.model.dto;

import java.sql.Time;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RestaurantDateTimeDTO {
    private Time openingTime;

    private Time closingTime;

    private String day;
}
