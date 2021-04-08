package com.atlantis.bf.service.dto;

import javax.validation.constraints.*;

import com.atlantis.bf.domain.Products;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A DTO for the {@link com.atlantis.bf.domain.LigneEntreeStock} entity.
 */
public class LigneEntreeStockDTO implements Serializable {
    
    private Long id;

    @NotNull
    private BigDecimal prixUnitaire;

    @NotNull
    private BigDecimal quantite;

    @NotNull
    private BigDecimal prixTotal;


    private Long productsId;

    private String productsLibelle;

    private Long entreestockId;

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

    public Long getEntreestockId() {
        return entreestockId;
    }

    public void setEntreestockId(Long entreeStockId) {
        this.entreestockId = entreeStockId;
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
        if (!(o instanceof LigneEntreeStockDTO)) {
            return false;
        }

        return id != null && id.equals(((LigneEntreeStockDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LigneEntreeStockDTO{" +
            "id=" + getId() +
            ", prixUnitaire=" + getPrixUnitaire() +
            ", quantite=" + getQuantite() +
            ", prixTotal=" + getPrixTotal() +
            ", productsId=" + getProductsId() +
            ", productsLibelle='" + getProductsLibelle() + "'" +
            ", entreestockId=" + getEntreestockId() +
            "}";
    }
}
