package com.temreserva.backend.temreserva_backend.web.model.response;

import java.time.LocalDate;

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
    
    private LocalDate birthDate;

    private String phoneNumber;

    private ImageModel image;
}
