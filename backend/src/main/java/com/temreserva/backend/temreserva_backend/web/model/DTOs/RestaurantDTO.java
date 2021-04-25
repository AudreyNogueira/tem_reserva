package com.temreserva.backend.temreserva_backend.web.model.DTOs;

import java.sql.Time;
import java.time.LocalDateTime;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.br.CNPJ;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RestaurantDTO {    
    @NotNull(message = "Senha não pode ser nulo")
    @NotEmpty(message = "Senha não pode ser vazio")
    private String password;

    @Email
    @NotEmpty(message = "E-mail não pode ser nulo")
    @NotEmpty(message = "E-mail não pode ser vazio")
    private String email;

    @CNPJ(message = "Insira um CNPJ válido")
    @NotNull(message = "CNPJ não pode ser nulo")
    @NotEmpty(message = "CNPJ não pode ser vazio")
    private String cnpj;

    @NotNull(message = "Nome não pode ser nulo")
    @NotEmpty(message = "Nome não pode ser vazio")
    private String restaurantName;

    private String description;

    private String openDaysOfWeek;

    private Time openingTime;

    private Time closingTime;

    private Integer spacingOfTables;

    private Boolean handicappedAdapted;
    
    private Integer cleaningPeriodicity;

    private Integer maxNumberOfPeople;

    private Integer actualNumberOfPeople;

    private Integer averageStars;

    private LocalDateTime registerDate;

    private LocalDateTime updateDate;

    private AddressDTO address;   
}
