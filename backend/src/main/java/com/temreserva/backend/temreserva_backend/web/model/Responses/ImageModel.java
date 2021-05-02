package com.temreserva.backend.temreserva_backend.web.model.Responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageModel {
    private Long id;

    private byte[] image;
}
