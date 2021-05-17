package com.temreserva.backend.temreserva_backend.web.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReviewDTO {
    private Long idReview;

    private Long idUser;

    private Long idRestaurant;

    private Integer stars;

    private String title;

    private String description;
}
