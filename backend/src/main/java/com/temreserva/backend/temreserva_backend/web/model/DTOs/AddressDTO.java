package com.temreserva.backend.temreserva_backend.web.model.DTOs;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddressDTO {
    @NotNull
    private String cep;

    @NotNull
    private String district;

    @NotNull
    private String address;

    @NotNull
    private String complement;

    @NotNull
    private String locality;

    @NotNull
    private int restaurantNumber;

    @NotNull
    private String zone;

    @NotNull
    private String uf;
}
