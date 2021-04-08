package com.atlantis.bf.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;

import com.atlantis.bf.domain.LignesVentes;

import java.util.HashSet;
import java.util.Set;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A DTO for the {@link com.atlantis.bf.domain.Ventes} entity.
 */
public class VentesDTO implements Serializable {
    
    private Long id;

    @NotNull
    private BigDecimal montantInitial;

    @NotNull
    private BigDecimal remise;

    @NotNull
    private BigDecimal montantFinal;

    @NotNull
    private LocalDate date;

    @NotNull
    private Boolean annule;


    private Long clientId;

    private String clientFullName;

    private Set<LignesVentes> lignesVentes = new HashSet<>();
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getMontantInitial() {
        return montantInitial;
    }

    public void setMontantInitial(BigDecimal montantInitial) {
        this.montantInitial = montantInitial;
    }

    public BigDecimal getRemise() {
        return remise;
    }

    public void setRemise(BigDecimal remise) {
        this.remise = remise;
    }

    public BigDecimal getMontantFinal() {
        return montantFinal;
    }

    public void setMontantFinal(BigDecimal montantFinal) {
        this.montantFinal = montantFinal;
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

    public Set<LignesVentes> getLignesVentes() {
        return lignesVentes;
    }

    public void setLignesVentes(Set<LignesVentes> lignesVentes) {
        this.lignesVentes = lignesVentes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VentesDTO)) {
            return false;
        }

        return id != null && id.equals(((VentesDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VentesDTO{" +
            "id=" + getId() +
            ", montantInitial=" + getMontantInitial() +
            ", remise=" + getRemise() +
            ", montantFinal=" + getMontantFinal() +
            ", date='" + getDate() + "'" +
            ", annule='" + isAnnule() + "'" +
            ", clientId=" + getClientId() +
            ", clientFullName='" + getClientFullName() + "'" +
            ", lignesVentes=" + lignesVentes + 
            "}";
    }
}
