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
public class RestaurantModel {
    private Long id;

    private String cnpj;

    private String payment;

    private String cleaning;

    private String restaurantName;

    private String phoneNumber;

    private String email;

    private Integer maxNumberOfPeople;

    private List<CurrentPeopleModel> currentPeople;

    private Integer averageStars;

    private String description;

    private AddressModel address;

    private ImageModel profileImage;

    private List<ImageModel> restaurantImages;

    private List<RestaurantDateTimeModel> restaurantDateTime;
}
