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
 * Criteria class for the {@link com.atlantis.bf.domain.Products} entity. This class is used
 * in {@link com.atlantis.bf.web.rest.ProductsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /products?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProductsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter libelle;

    private BigDecimalFilter prixVente;

    private BooleanFilter isDisabled;

    private StringFilter comments;

    private LongFilter ligneStockId;

    private LongFilter ligneVentesId;

    private LongFilter ligneReservationId;

    private LongFilter typeProduitId;

    public ProductsCriteria() {
    }

    public ProductsCriteria(ProductsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.libelle = other.libelle == null ? null : other.libelle.copy();
        this.prixVente = other.prixVente == null ? null : other.prixVente.copy();
        this.isDisabled = other.isDisabled == null ? null : other.isDisabled.copy();
        this.comments = other.comments == null ? null : other.comments.copy();
        this.ligneStockId = other.ligneStockId == null ? null : other.ligneStockId.copy();
        this.ligneVentesId = other.ligneVentesId == null ? null : other.ligneVentesId.copy();
        this.ligneReservationId = other.ligneReservationId == null ? null : other.ligneReservationId.copy();
        this.typeProduitId = other.typeProduitId == null ? null : other.typeProduitId.copy();
    }

    @Override
    public ProductsCriteria copy() {
        return new ProductsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getLibelle() {
        return libelle;
    }

    public void setLibelle(StringFilter libelle) {
        this.libelle = libelle;
    }

    public BigDecimalFilter getPrixVente() {
        return prixVente;
    }

    public void setPrixVente(BigDecimalFilter prixVente) {
        this.prixVente = prixVente;
    }

    public BooleanFilter getIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(BooleanFilter isDisabled) {
        this.isDisabled = isDisabled;
    }

    public StringFilter getComments() {
        return comments;
    }

    public void setComments(StringFilter comments) {
        this.comments = comments;
    }

    public LongFilter getLigneStockId() {
        return ligneStockId;
    }

    public void setLigneStockId(LongFilter ligneStockId) {
        this.ligneStockId = ligneStockId;
    }

    public LongFilter getLigneVentesId() {
        return ligneVentesId;
    }

    public void setLigneVentesId(LongFilter ligneVentesId) {
        this.ligneVentesId = ligneVentesId;
    }

    public LongFilter getLigneReservationId() {
        return ligneReservationId;
    }

    public void setLigneReservationId(LongFilter ligneReservationId) {
        this.ligneReservationId = ligneReservationId;
    }

    public LongFilter getTypeProduitId() {
        return typeProduitId;
    }

    public void setTypeProduitId(LongFilter typeProduitId) {
        this.typeProduitId = typeProduitId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductsCriteria that = (ProductsCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(libelle, that.libelle) &&
            Objects.equals(prixVente, that.prixVente) &&
            Objects.equals(isDisabled, that.isDisabled) &&
            Objects.equals(comments, that.comments) &&
            Objects.equals(ligneStockId, that.ligneStockId) &&
            Objects.equals(ligneVentesId, that.ligneVentesId) &&
            Objects.equals(ligneReservationId, that.ligneReservationId) &&
            Objects.equals(typeProduitId, that.typeProduitId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        libelle,
        prixVente,
        isDisabled,
        comments,
        ligneStockId,
        ligneVentesId,
        ligneReservationId,
        typeProduitId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (libelle != null ? "libelle=" + libelle + ", " : "") +
                (prixVente != null ? "prixVente=" + prixVente + ", " : "") +
                (isDisabled != null ? "isDisabled=" + isDisabled + ", " : "") +
                (comments != null ? "comments=" + comments + ", " : "") +
                (ligneStockId != null ? "ligneStockId=" + ligneStockId + ", " : "") +
                (ligneVentesId != null ? "ligneVentesId=" + ligneVentesId + ", " : "") +
                (ligneReservationId != null ? "ligneReservationId=" + ligneReservationId + ", " : "") +
                (typeProduitId != null ? "typeProduitId=" + typeProduitId + ", " : "") +
            "}";
    }

}
