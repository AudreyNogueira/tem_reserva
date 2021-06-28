package com.temreserva.backend.temreserva_backend.data.entity;

import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TB_RESTAURANTE_DATA_HORARIO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantDateTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DATA_HORARIO_RESTAURANTE")
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "ID_RESTAURANTE",nullable = false, updatable = true)
    private Restaurant restaurant;

    @Column(name = "HORA_INICIO", nullable = true, updatable = true)
    @JsonFormat(pattern = "hh:mm:ss")
    private Time openingTime;

    @Column(name = "HORA_FIM", nullable = true, updatable = true)
    @JsonFormat(pattern = "hh:mm:ss")
    private Time closingTime;

    @Column(name = "DIA", nullable = true, updatable = true)
    private String day;
}
