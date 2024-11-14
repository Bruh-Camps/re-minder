package com.reminder.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table( name = "item" )
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column( name = "nome", columnDefinition = "VARCHAR(100)", nullable = false )
    private String nome;

    @Column( name = "dataUltimaTroca", columnDefinition = "DATE", nullable = false )
    @Temporal( TemporalType.DATE )
    @JsonFormat( pattern = "dd/MM/yyyy", timezone = "America/Sao_Paulo" )
    private LocalDate dataUltimaTroca;

    @Column( name = "intervaloTrocaDias", columnDefinition = "INT", nullable = true )
    private Integer intervaloTrocaDias; // Dias até a próxima troca

    @Column( name = "dataProximaTroca", columnDefinition = "DATE", nullable = false )
    @Temporal( TemporalType.DATE )
    @JsonFormat( pattern = "dd/MM/yyyy", timezone = "America/Sao_Paulo" )
    private LocalDate dataProximaTroca;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataUltimaTroca() {
        return dataUltimaTroca;
    }

    public void setDataUltimaTroca(LocalDate dataUltimaTroca) {
        this.dataUltimaTroca = dataUltimaTroca;
    }

    public Integer getIntervaloTrocaDias() {
        return intervaloTrocaDias;
    }

    public void setIntervaloTrocaDias(Integer intervaloTrocaDias) {
        this.intervaloTrocaDias = intervaloTrocaDias;
    }

    public LocalDate getDataProximaTroca() {
        return dataProximaTroca;
    }

    public void setDataProximaTroca(LocalDate dataProximaTroca) {
        this.dataProximaTroca = dataProximaTroca;
    }
}
