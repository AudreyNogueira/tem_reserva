package com.temreserva.backend.temreserva_backend.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TB_ENDERECO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ENDERECO")
    private Long id;

    @OneToOne
    @JoinColumn(name = "ID_RESTAURANTE",nullable = false, updatable = false)
    private Restaurant restaurant;

    @Column(name = "CEP", nullable = false, length = 1000, updatable = false)
    @NotEmpty(message = "CEP não pode ser vazio")
    private String cep;

    @Column(name = "BAIRRO", nullable = false, length = 1000, updatable = false)
    @NotEmpty(message = "Bairro não pode ser vazio")
    private String district;

    @Column(name = "LOGRADOURO", nullable = false, length = 1000, updatable = false)
    @NotEmpty(message = "Logradouro não pode ser vazio")
    private String address;

    @Column(name = "COMPLEMENTO", nullable = false, length = 1000, updatable = false)
    private String complement;

    @Column(name = "LOCALIDADE", nullable = false, length = 1000, updatable = false)
    @NotEmpty(message = "Localidade não pode ser vazio")
    private String locality;

    @Column(name = "NUMERO", nullable = false, length = 1000, updatable = false)
    @NotNull(message = "Número não pode ser nulo")
    private int restaurantNumber;

    @Column(name = "ZONA", nullable = false, length = 1000, updatable = false)
    @NotEmpty(message = "Zona não pode ser vazio")
    private String zone;

    @Column(name = "UF", nullable = false, length = 1000, updatable = false)
    @NotEmpty(message = "UF não pode ser vazio")
    private String uf;
}
