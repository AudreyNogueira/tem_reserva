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
public class RestaurantModel {
    private Long id;

    private String restaurantName;

    private String phoneNumber;

    private String email;

    private Integer maxNumberOfPeople;

    private Integer actualNumberOfPeople;

    private Integer averageStars;

    private AddressModel address;

    private byte[] profileImage;

    private List<byte[]> restaurantImages;
}
