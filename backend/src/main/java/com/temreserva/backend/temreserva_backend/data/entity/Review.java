package com.temreserva.backend.temreserva_backend.data.entity;

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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TB_AVALIACAO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_AVALIACAO")
    private Long id;

    @OneToOne
    @JoinColumn(name = "ID_USUARIO",nullable = false, updatable = false)
    private User user;

    @OneToOne
    @JoinColumn(name = "ID_RESTAURANTE",nullable = false, updatable = false)
    private Restaurant restaurant;

    @Column(name = "NOTA_AVALIACAO", nullable = false, updatable = false)
    private Integer stars;

    @Column(name = "TITULO", nullable = false, length = 100, updatable = false)
    @NotEmpty(message = "Titulo não pode ser vazio")
    private String title;

    @Column(name = "DESCRICAO", nullable = false, length = 1000, updatable = false)
    @NotEmpty(message = "Descrição não pode ser vazia")
    private String description;

    @Column(name = "DATA_AVALIACAO", updatable = true)
    @JsonFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    private LocalDateTime reviewDate;

    @PrePersist
    public void prePersist(){
        setReviewDate(LocalDateTime.now());
    }
}
