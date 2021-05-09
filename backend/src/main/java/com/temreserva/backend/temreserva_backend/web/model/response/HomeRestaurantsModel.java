package com.temreserva.backend.temreserva_backend.web.model.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomeRestaurantsModel {
    private List<RestaurantModel> topRated;

    private List<ZoneRestaurantModel> zone;
}
