package com.atlantis.bf.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link com.atlantis.bf.domain.Reservation} entity.
 */
public class ReservationDTO implements Serializable {
    
    private Long id;

    @NotNull
    private LocalDate revervePour;

    @NotNull
    private LocalDate date;

    @NotNull
    private Boolean annule;


    private Long clientId;

    private String clientFullName;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getRevervePour() {
        return revervePour;
    }

    public void setRevervePour(LocalDate revervePour) {
        this.revervePour = revervePour;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Boolean isAnnule() {
        return annule;
    }

    public void setAnnule(Boolean annule) {
        this.annule = annule;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getClientFullName() {
        return clientFullName;
    }

    public void setClientFullName(String clientFullName) {
        this.clientFullName = clientFullName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReservationDTO)) {
            return false;
        }

        return id != null && id.equals(((ReservationDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReservationDTO{" +
            "id=" + getId() +
            ", revervePour='" + getRevervePour() + "'" +
            ", date='" + getDate() + "'" +
            ", annule='" + isAnnule() + "'" +
            ", clientId=" + getClientId() +
            ", clientFullName='" + getClientFullName() + "'" +
            "}";
    }
}
