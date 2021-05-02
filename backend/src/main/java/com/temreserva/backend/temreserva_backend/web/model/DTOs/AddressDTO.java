package com.temreserva.backend.temreserva_backend.web.model.DTOs;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddressDTO {
    private String cep;

    private String district;

    private String address;

    private String complement;

    private String locality;

    private int restaurantNumber;

    private String zone;

    private String uf;
}
