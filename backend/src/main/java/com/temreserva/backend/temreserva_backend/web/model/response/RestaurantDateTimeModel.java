package com.temreserva.backend.temreserva_backend.web.model.response;

import java.sql.Time;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantDateTimeModel {
    private Time openingTime;

    private Time closingTime;

    private String day;
}
