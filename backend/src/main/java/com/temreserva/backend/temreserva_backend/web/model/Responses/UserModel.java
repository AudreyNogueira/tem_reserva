package com.temreserva.backend.temreserva_backend.web.model.Responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserModel {
    private Long id;

    private String name;

    private String cpf;

    private String email;

    private String phoneNumber;

    private byte[] image;
}
