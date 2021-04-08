package com.atlantis.bf.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A DTO for the {@link com.atlantis.bf.domain.Depenses} entity.
 */
public class DepensesDTO implements Serializable {
    
    private Long id;

    @NotNull
    private BigDecimal montant;

    private String comments;

    @NotNull
    private LocalDate date;

    @NotNull
    private Boolean annule;


    private Long typeDepenseId;

    private String typeDepenseLibelle;
    
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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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

    public Long getTypeDepenseId() {
        return typeDepenseId;
    }

    public void setTypeDepenseId(Long typeDepenseId) {
        this.typeDepenseId = typeDepenseId;
    }

    public String getTypeDepenseLibelle() {
        return typeDepenseLibelle;
    }

    public void setTypeDepenseLibelle(String typeDepenseLibelle) {
        this.typeDepenseLibelle = typeDepenseLibelle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DepensesDTO)) {
            return false;
        }

        return id != null && id.equals(((DepensesDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepensesDTO{" +
            "id=" + getId() +
            ", montant=" + getMontant() +
            ", comments='" + getComments() + "'" +
            ", date='" + getDate() + "'" +
            ", annule='" + isAnnule() + "'" +
            ", typeDepenseId=" + getTypeDepenseId() +
            ", typeDepenseLibelle='" + getTypeDepenseLibelle() + "'" +
            "}";
    }
}
