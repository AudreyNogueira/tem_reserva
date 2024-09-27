package com.temreserva.backend.temreserva_backend.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "TB_TEMPLATE_EMAIL")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MailTemplate {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "DESCRICAO")
    private String description;

    @Column(name = "HTML", length = 10000)
    private String html;

    @Column(name = "ATIVO")
    private Boolean active;
}
