package com.temreserva.backend.temreserva_backend.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TB_RESTAURANTE_SEGMENTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantSegment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_RESTAURANTE_SEGMENTO")
    private Long id;

    @OneToOne
    @JoinColumn(name = "ID_RESTAURANTE",nullable = false, updatable = false)
    private Restaurant restaurant;  

    @OneToOne
    @JoinColumn(name = "ID_SEGMENTO",nullable = false, updatable = false)
    private Segment segment;  

    @Column(name = "ATIVO", nullable = false, updatable = true)
    private Boolean active;

    @PrePersist
    public void prePersist(){
        setActive(true);
    }
}
