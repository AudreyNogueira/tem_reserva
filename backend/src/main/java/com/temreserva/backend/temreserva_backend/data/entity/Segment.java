package com.temreserva.backend.temreserva_backend.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TB_SEGMENTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Segment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SEGMENTO")
    private Long id;

    @Column(name = "DESCRICAO", nullable = false, length = 500, updatable = true)
    @NotEmpty(message = "Descrição não pode ser nula")
    private String description;

    @Column(name = "ATIVO", nullable = false, updatable = true)
    private Boolean active;

    @PrePersist
    public void prePersist(){
        setActive(true);
    }
}
