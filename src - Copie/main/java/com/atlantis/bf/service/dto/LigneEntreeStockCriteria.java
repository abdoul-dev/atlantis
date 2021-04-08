package com.atlantis.bf.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.BigDecimalFilter;

/**
 * Criteria class for the {@link com.atlantis.bf.domain.LigneEntreeStock} entity. This class is used
 * in {@link com.atlantis.bf.web.rest.LigneEntreeStockResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ligne-entree-stocks?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LigneEntreeStockCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BigDecimalFilter prixUnitaire;

    private BigDecimalFilter quantite;

    private BigDecimalFilter prixTotal;

    private LongFilter productsId;

    private LongFilter entreestockId;

    public LigneEntreeStockCriteria() {
    }

    public LigneEntreeStockCriteria(LigneEntreeStockCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.prixUnitaire = other.prixUnitaire == null ? null : other.prixUnitaire.copy();
        this.quantite = other.quantite == null ? null : other.quantite.copy();
        this.prixTotal = other.prixTotal == null ? null : other.prixTotal.copy();
        this.productsId = other.productsId == null ? null : other.productsId.copy();
        this.entreestockId = other.entreestockId == null ? null : other.entreestockId.copy();
    }

    @Override
    public LigneEntreeStockCriteria copy() {
        return new LigneEntreeStockCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public BigDecimalFilter getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(BigDecimalFilter prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public BigDecimalFilter getQuantite() {
        return quantite;
    }

    public void setQuantite(BigDecimalFilter quantite) {
        this.quantite = quantite;
    }

    public BigDecimalFilter getPrixTotal() {
        return prixTotal;
    }

    public void setPrixTotal(BigDecimalFilter prixTotal) {
        this.prixTotal = prixTotal;
    }

    public LongFilter getProductsId() {
        return productsId;
    }

    public void setProductsId(LongFilter productsId) {
        this.productsId = productsId;
    }

    public LongFilter getEntreestockId() {
        return entreestockId;
    }

    public void setEntreestockId(LongFilter entreestockId) {
        this.entreestockId = entreestockId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LigneEntreeStockCriteria that = (LigneEntreeStockCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(prixUnitaire, that.prixUnitaire) &&
            Objects.equals(quantite, that.quantite) &&
            Objects.equals(prixTotal, that.prixTotal) &&
            Objects.equals(productsId, that.productsId) &&
            Objects.equals(entreestockId, that.entreestockId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        prixUnitaire,
        quantite,
        prixTotal,
        productsId,
        entreestockId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LigneEntreeStockCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (prixUnitaire != null ? "prixUnitaire=" + prixUnitaire + ", " : "") +
                (quantite != null ? "quantite=" + quantite + ", " : "") +
                (prixTotal != null ? "prixTotal=" + prixTotal + ", " : "") +
                (productsId != null ? "productsId=" + productsId + ", " : "") +
                (entreestockId != null ? "entreestockId=" + entreestockId + ", " : "") +
            "}";
    }

}
