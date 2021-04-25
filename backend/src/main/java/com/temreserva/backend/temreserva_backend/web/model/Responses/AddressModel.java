package com.temreserva.backend.temreserva_backend.web.model.Responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressModel {
    private String cep;
    
    private String district;

    private String address;

    private String complement;

    private String locality;

    private int restaurantNumber;

    private String zone;

    private String uf;
}
