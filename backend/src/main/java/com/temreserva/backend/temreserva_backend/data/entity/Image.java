package com.temreserva.backend.temreserva_backend.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TB_IMAGEM")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Image {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NOME")
    private String name;

    @Column(name = "TIPO")
    private String type;

    @Column(name = "RESTAURANTE")
    private Boolean isRestaurant;

    @Column(name = "FOTO_PERFIL")
    private Boolean isProfilePic;

    @Column(name = "ID_DONO")
    private Long imageOwnerId;

    @Column(name = "BYTES", length = 10000000)
    private byte[] picByte;
}
