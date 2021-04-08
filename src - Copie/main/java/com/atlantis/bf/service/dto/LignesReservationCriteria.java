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
 * Criteria class for the {@link com.atlantis.bf.domain.LignesReservation} entity. This class is used
 * in {@link com.atlantis.bf.web.rest.LignesReservationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /lignes-reservations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LignesReservationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BigDecimalFilter quantite;

    private LongFilter productsId;

    public LignesReservationCriteria() {
    }

    public LignesReservationCriteria(LignesReservationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.quantite = other.quantite == null ? null : other.quantite.copy();
        this.productsId = other.productsId == null ? null : other.productsId.copy();
    }

    @Override
    public LignesReservationCriteria copy() {
        return new LignesReservationCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public BigDecimalFilter getQuantite() {
        return quantite;
    }

    public void setQuantite(BigDecimalFilter quantite) {
        this.quantite = quantite;
    }

    public LongFilter getProductsId() {
        return productsId;
    }

    public void setProductsId(LongFilter productsId) {
        this.productsId = productsId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LignesReservationCriteria that = (LignesReservationCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(quantite, that.quantite) &&
            Objects.equals(productsId, that.productsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        quantite,
        productsId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LignesReservationCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (quantite != null ? "quantite=" + quantite + ", " : "") +
                (productsId != null ? "productsId=" + productsId + ", " : "") +
            "}";
    }

}
