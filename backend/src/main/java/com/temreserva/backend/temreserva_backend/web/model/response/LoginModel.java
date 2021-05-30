package com.temreserva.backend.temreserva_backend.web.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginModel {
    private UserModel user;

    private RestaurantModel restaurant;
}
