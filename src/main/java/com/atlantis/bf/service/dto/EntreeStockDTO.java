package com.atlantis.bf.service.dto;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.*;

import com.atlantis.bf.domain.LigneEntreeStock;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A DTO for the {@link com.atlantis.bf.domain.EntreeStock} entity.
 */
public class EntreeStockDTO implements Serializable {
    
    private Long id;

    @NotNull
    private BigDecimal montant;

    @NotNull
    private Boolean annule;

    @NotNull
    private LocalDate date;


    private Long fournisseurId;

    private String fournisseurFullName;

    private Set<LigneEntreeStock> ligneEntreeStocks = new HashSet<>();
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public Boolean isAnnule() {
        return annule;
    }

    public void setAnnule(Boolean annule) {
        this.annule = annule;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getFournisseurId() {
        return fournisseurId;
    }

    public void setFournisseurId(Long fournisseurId) {
        this.fournisseurId = fournisseurId;
    }

    public String getFournisseurFullName() {
        return fournisseurFullName;
    }

    public void setFournisseurFullName(String fournisseurFullName) {
        this.fournisseurFullName = fournisseurFullName;
    }

    public Set<LigneEntreeStock> getLignesEntreeStock() {
        return ligneEntreeStocks;
    }

    public void setLignesEntreeStock(Set<LigneEntreeStock> ligneEntreeStocks) {
        this.ligneEntreeStocks = ligneEntreeStocks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EntreeStockDTO)) {
            return false;
        }

        return id != null && id.equals(((EntreeStockDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EntreeStockDTO{" +
            "id=" + getId() +
            ", montant=" + getMontant() +
            ", annule='" + isAnnule() + "'" +
            ", date='" + getDate() + "'" +
            ", fournisseurId=" + getFournisseurId() +
            ", fournisseurFullName='" + getFournisseurFullName() + "'" +
            "}";
    }
}
