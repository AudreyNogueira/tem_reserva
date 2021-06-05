package com.temreserva.backend.temreserva_backend.data.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TB_RESERVA")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reserve {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_RESERVA")
    private Long id;

    @OneToOne
    @JoinColumn(name = "ID_USUARIO",nullable = false, updatable = true)
    private User user;

    @OneToOne
    @JoinColumn(name = "ID_RESTAURANTE",nullable = false, updatable = true)
    private Restaurant restaurant;
 
    @Column(name = "PERIODO", nullable = false, updatable = true)
    private String period;    

    @Column(name = "OBSERVACAO", nullable = true, updatable = true)
    private String observation;

    @Column(name = "DATA_RESERVA", nullable = false, updatable = true)
    @JsonFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    private LocalDateTime reserveDate;

    @Column(name = "QTD_PESSOAS", nullable = false, updatable = true)
    private Integer amountOfPeople;

    @Column(name = "CONFIRMADO", nullable = false, updatable = true)
    private Boolean confirmed;
}