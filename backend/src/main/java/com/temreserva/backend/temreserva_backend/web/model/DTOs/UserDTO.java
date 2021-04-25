package com.temreserva.backend.temreserva_backend.web.model.DTOs;

import java.time.LocalDateTime;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.br.CPF;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {
    private Long id;

    @NotNull(message = "Senha não pode ser nulo")
    @NotEmpty(message = "Senha não pode ser vazio")
    private String password;

    @NotNull(message = "Nome não pode ser nulo")
    @NotEmpty(message = "Nome não pode ser vazio")
    private String name;

    @CPF(message = "Insira um CPF válido")
    @NotNull(message = "CPF não pode ser nulo")
    @NotEmpty(message = "CPF não pode ser vazio")
    private String cpf;

    @Email
    @NotEmpty(message = "E-mail não pode ser nulo")
    @NotEmpty(message = "E-mail não pode ser vazio")
    private String email;

    private String phoneNumber;

    // private Long idUserType;

    private LocalDateTime registerDate;

    private LocalDateTime updateDate;
}
