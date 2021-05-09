package com.temreserva.backend.temreserva_backend.web.model.response;

import com.temreserva.backend.temreserva_backend.web.model.interfaces.ICurrentPeopleModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurrentPeopleModel implements ICurrentPeopleModel {
    private int currentPeople;

    private String period;
}
