package com.temreserva.backend.temreserva_backend.data.entity;

import java.sql.Time;
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
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.temreserva.backend.temreserva_backend.web.model.dto.RestaurantDTO;

import org.hibernate.validator.constraints.br.CNPJ;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TB_RESTAURANTE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_RESTAURANTE")
    private Long id;

    @OneToOne
    @JoinColumn(name = "ID_CREDENCIAL",nullable = false, updatable = false)
    private Credential credential;

    @Column(name = "CNPJ", nullable = false, length = 14, updatable = true)
    @NotEmpty(message = "CNPJ não pode ser vazio")
    @CNPJ(message = "Insira um CNPJ válido")
    private String cnpj;

    @Column(name = "TELEFONE", nullable = true, length = 11, updatable = true)
    private String phoneNumber;

    @Column(name = "NOME", nullable = false, length = 1000, updatable = true)
    @NotEmpty(message = "Nome não pode ser vazio")
    private String restaurantName;

    @Column(name = "DESCRICAO", nullable = true, length = 1000, updatable = true)
    private String description;

    @Column(name = "DIAS_SEMANAIS_ABERTOS", nullable = true, length = 500, updatable = true)
    private String openDaysOfWeek;

    @Column(name = "HORA_INICIO", nullable = true, updatable = true)
    @JsonFormat(pattern = "hh:mm:ss")
    private Time openingTime;

    @Column(name = "HORA_FIM", nullable = true, updatable = true)
    @JsonFormat(pattern = "hh:mm:ss")
    private Time closingTime;

    @Column(name = "DISTANCIAMENTO_MESAS", nullable = true, updatable = true)
    @NotNull(message = "A sinalização de espaçamento entre as mesas não pode ser nula")
    private Integer spacingOfTables;

    @Column(name = "ADAPTACAO_DEFICIENTE", nullable = true, updatable = true)
    private Boolean handicappedAdapted;

    @Column(name = "PERIODICIDADE_HIGIENIZACAO", nullable = true, updatable = true)
    private String cleaning;

    @Column(name = "QTD_PESSOAS_MAXIMA", nullable = true, updatable = true)
    // @NotNull(message = "A quantidade de pessoas máxima não pode ser nula")
    private Integer maxNumberOfPeople;

    @Column(name = "QTD_PESSOAS_ATUAL", nullable = true, updatable = true)
    private Integer actualNumberOfPeople;

    @Column(name = "NOTA_AVALIACAO_MEDIA", nullable = true, updatable = true)
    private Integer averageStars;

    @Column(name = "FORMA_PAGAMENTO", nullable = true, updatable = true)
    private String payment;

    @Column(name = "DATA_CADASTRO", nullable = true, updatable = false)
    @JsonFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    private LocalDateTime registerDate;

    @Column(name = "DATA_ATUALIZACAO", nullable = true, updatable = true)
    @JsonFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    private LocalDateTime updateDate;

    @PrePersist
    public void prePersist() {
        setHandicappedAdapted(false);
        setActualNumberOfPeople(0);
        setAverageStars(0);
        setRegisterDate(LocalDateTime.now());
        setUpdateDate(LocalDateTime.now());
    }

    public Restaurant(RestaurantDTO dto) {
        cnpj = dto.getCnpj();
        phoneNumber = dto.getPhoneNumber();
        restaurantName = dto.getRestaurantName();
        description = dto.getDescription() == null ? "" : dto.getDescription();
        openDaysOfWeek = dto.getOpenDaysOfWeek() == null ? "" : dto.getOpenDaysOfWeek();
        openingTime = dto.getOpeningTime() == null ? new Time(0) : dto.getOpeningTime();
        closingTime = dto.getClosingTime() == null ? new Time(0) : dto.getClosingTime();
        spacingOfTables = dto.getSpacingOfTables() == null ? 0 : dto.getSpacingOfTables();
        cleaning = dto.getCleaning();   
        maxNumberOfPeople = dto.getMaxNumberOfPeople() == null ? 0 : dto.getMaxNumberOfPeople(); 
        handicappedAdapted = false;
        actualNumberOfPeople = dto.getActualNumberOfPeople() != null ? dto.getActualNumberOfPeople() : 0;
        averageStars = 0;
        registerDate = LocalDateTime.now();
        updateDate = LocalDateTime.now(); 
        payment = dto.getPayment();
    }
}
