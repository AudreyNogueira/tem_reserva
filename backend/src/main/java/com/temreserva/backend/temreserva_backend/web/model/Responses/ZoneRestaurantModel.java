package com.temreserva.backend.temreserva_backend.web.model.Responses;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ZoneRestaurantModel {
    private String zoneName;

    private List<RestaurantModel> restaurantList;
}
