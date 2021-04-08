package com.atlantis.bf.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import com.atlantis.bf.domain.Products;

/**
 * A DTO for the {@link com.atlantis.bf.domain.LignesVentes} entity.
 */
public class LignesVentesDTO implements Serializable {
    
    private Long id;

    @NotNull
    private BigDecimal prixUnitaire;

    @NotNull
    private BigDecimal quantite;

    @NotNull
    private BigDecimal prixTotal;


    private Long productsId;

    private String productsLibelle;

    private Long ventesId;

    private Products products = new Products(); 
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public BigDecimal getQuantite() {
        return quantite;
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public BigDecimal getPrixTotal() {
        return prixTotal;
    }

    public void setPrixTotal(BigDecimal prixTotal) {
        this.prixTotal = prixTotal;
    }

    public Long getProductsId() {
        return productsId;
    }

    public void setProductsId(Long productsId) {
        this.productsId = productsId;
    }

    public String getProductsLibelle() {
        return productsLibelle;
    }

    public void setProductsLibelle(String productsLibelle) {
        this.productsLibelle = productsLibelle;
    }

    public Long getVentesId() {
        return ventesId;
    }

    public void setVentesId(Long ventesId) {
        this.ventesId = ventesId;
    }

    public Products getProducts() {
        return products;
    }

    public void setProducts(Products products) {
        this.products = products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LignesVentesDTO)) {
            return false;
        }

        return id != null && id.equals(((LignesVentesDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LignesVentesDTO{" +
            "id=" + getId() +
            ", prixUnitaire=" + getPrixUnitaire() +
            ", quantite=" + getQuantite() +
            ", prixTotal=" + getPrixTotal() +
            ", productsId=" + getProductsId() +
            ", productsLibelle='" + getProductsLibelle() + "'" +
            ", ventesId=" + getVentesId() +
            "}";
    }
}
