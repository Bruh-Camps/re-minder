package com.reminder.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

@Entity
@Table( name = "ITEMS" )
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;

    @NotNull(message = "Name is required")
    @Column( name = "NAME", columnDefinition = "VARCHAR(100)", nullable = false )
    private String name;

    @NotNull(message = "Date of last change is required")
    @Column( name = "DATE_LAST_CHANGE", columnDefinition = "DATE", nullable = false )
    @Temporal( TemporalType.DATE )
    @JsonFormat( pattern = "dd/MM/yyyy", timezone = "America/Sao_Paulo" )
    private LocalDate dateLastChange;

    @Positive(message = "Change days interval must be a positive number")
    @Column( name = "CHANGE_DAYS_INTERVAL", columnDefinition = "INT", nullable = true )
    private Integer changeDaysInterval; // Dias até a próxima troca

    //@Null(message = "Date of next change is calculated automatically based on chande days interval.")
    @Column( name = "DATE_NEXT_CHANGE", columnDefinition = "DATE", nullable = false )
    @Temporal( TemporalType.DATE )
    @JsonFormat( pattern = "dd/MM/yyyy", timezone = "America/Sao_Paulo" )
    private LocalDate dateNextChange;

    @ManyToOne (fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_ID")
    @JsonBackReference
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateLastChange() {
        return dateLastChange;
    }

    public void setDateLastChange(LocalDate dateLastChange) {
        this.dateLastChange = dateLastChange;
    }

    public Integer getChangeDaysInterval() {
        return changeDaysInterval;
    }

    public void setChangeDaysInterval(Integer changeDaysInterval) {
        this.changeDaysInterval = changeDaysInterval;
    }

    public LocalDate getDateNextChange() {
        return dateNextChange;
    }

    public void setDateNextChange(LocalDate dateNextChange) {
        this.dateNextChange = dateNextChange;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
