package com.temreserva.backend.temreserva_backend.web.model.response;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReserveRestaurantModel {
    private LocalDate day;

    private Integer maxNumberOfPeople;

    private List<CurrentPeopleModel> currentPeople;
    
    List<ReserveModel> reserves;

}

    
