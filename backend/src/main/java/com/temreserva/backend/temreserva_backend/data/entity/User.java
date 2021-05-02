package com.temreserva.backend.temreserva_backend.data.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.temreserva.backend.temreserva_backend.web.model.DTOs.UserDTO;

import org.hibernate.validator.constraints.br.CPF;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TB_USUARIO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {   

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_USUARIO")
    private Long id;

    @Column(name = "NOME", nullable = false, length = 500, updatable = true)
    @NotEmpty(message = "Nome não pode ser nulo")
    private String name;

    @Column(name="CPF", nullable = false, length = 11, updatable = true, unique = true)
    @NotEmpty(message = "Cpf não pode ser vazio")
    @CPF(message = "CPF deve estar no modelo correto")
    private String cpf;

    @Column(name = "TELEFONE", nullable = true, length = 11, updatable = true)
    private String phoneNumber;

    @OneToOne
    @JoinColumn(name = "ID_CREDENCIAL",nullable = false, updatable = false)
    private Credential credential;

    @Column(name = "DATA_NASCIMENTO", updatable = true)
    @JsonFormat(pattern = "dd/MM/yyyy")
    // @Temporal(TemporalType.DATE)
    private LocalDate birthDate;

    @Column(name = "DATA_CADASTRO", updatable = false)
    @JsonFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    private LocalDateTime registerDate;

    @Column(name = "DATA_ATUALIZACAO", updatable = true)
    @JsonFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    private LocalDateTime updateDate;

    @PrePersist
    public void prePersist(){
        setRegisterDate(LocalDateTime.now());
        setUpdateDate(LocalDateTime.now());
    }

    public User(UserDTO dto) {
        name = dto.getName();
        cpf = dto.getCpf();
        phoneNumber = dto.getPhoneNumber();
        registerDate = LocalDateTime.now();
        updateDate = LocalDateTime.now(); 
        birthDate = dto.getBirthDate();
    }   
}
