package com.reminder.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Objects;

public class ItemDto {
    @NotNull(message = "Name is required")
    private String name;

    @NotNull(message = "Date of last change is required")
    @JsonFormat(pattern = "dd/MM/yyyy") // Formato desejado para a entrada/saída de datas
    private LocalDate dateLastChange;

    @NotNull(message = "Change days interval is required")
    private Integer changeDaysInterval;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ItemDto itemDto = (ItemDto) o;
        return Objects.equals(name, itemDto.name) && Objects.equals(dateLastChange, itemDto.dateLastChange) && Objects.equals(changeDaysInterval, itemDto.changeDaysInterval);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, dateLastChange, changeDaysInterval);
    }
}
