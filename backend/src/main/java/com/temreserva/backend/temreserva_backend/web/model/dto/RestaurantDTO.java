package com.temreserva.backend.temreserva_backend.web.model.dto;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RestaurantDTO {    
    private String password;

    private String actualPassword;

    private String payment;

    private String email;

    private String cnpj;

    private String restaurantName;

    private String description;

    private String openDaysOfWeek;

    private String phoneNumber;

    private Time openingTime;

    private Time closingTime;

    private Integer spacingOfTables;

    private Boolean handicappedAdapted;
    
    private String cleaning;

    private Integer maxNumberOfPeople;

    private Integer actualNumberOfPeople;

    private Integer averageStars;

    private LocalDateTime registerDate;

    private LocalDateTime updateDate;

    private AddressDTO address;   

    private List<RestaurantDateTimeDTO> restaurantDateTime;}
