package com.temreserva.backend.temreserva_backend.web.model.DTOs;

import javax.validation.constraints.Email;

import org.hibernate.validator.constraints.br.CPF;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {
    private String password;

    private String actualPassword;

    private String name;

    @CPF
    private String cpf;

    @Email
    private String email;

    private String phoneNumber;
}
