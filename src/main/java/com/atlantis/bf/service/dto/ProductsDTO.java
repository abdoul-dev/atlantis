package com.atlantis.bf.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A DTO for the {@link com.atlantis.bf.domain.Products} entity.
 */
public class ProductsDTO implements Serializable {
    
    private Long id;

    @NotNull
    @Size(max = 100)
    private String libelle;

    @NotNull
    private BigDecimal prixVente;

    @NotNull
    private Boolean isDisabled;

    private BigDecimal quantite;

    private String comments;


    private Long typeProduitId;

    private String typeProduitLibelle;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public BigDecimal getPrixVente() {
        return prixVente;
    }

    public void setPrixVente(BigDecimal prixVente) {
        this.prixVente = prixVente;
    }

    public Boolean isIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(Boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    public BigDecimal getQuantite() {
        return quantite;
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Long getTypeProduitId() {
        return typeProduitId;
    }

    public void setTypeProduitId(Long typeProduitId) {
        this.typeProduitId = typeProduitId;
    }

    public String getTypeProduitLibelle() {
        return typeProduitLibelle;
    }

    public void setTypeProduitLibelle(String typeProduitLibelle) {
        this.typeProduitLibelle = typeProduitLibelle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductsDTO)) {
            return false;
        }

        return id != null && id.equals(((ProductsDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductsDTO{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", prixVente=" + getPrixVente() +
            ", isDisabled='" + isIsDisabled() + "'" +
            ", quantite=" + getQuantite() +
            ", comments='" + getComments() + "'" +
            ", typeProduitId=" + getTypeProduitId() +
            ", typeProduitLibelle='" + getTypeProduitLibelle() + "'" +
            "}";
    }
}
