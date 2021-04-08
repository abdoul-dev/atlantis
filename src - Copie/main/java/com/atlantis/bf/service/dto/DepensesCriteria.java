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
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link com.atlantis.bf.domain.Depenses} entity. This class is used
 * in {@link com.atlantis.bf.web.rest.DepensesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /depenses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DepensesCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BigDecimalFilter montant;

    private StringFilter comments;

    private LocalDateFilter date;

    private BooleanFilter annule;

    private LongFilter typeDepenseId;

    public DepensesCriteria() {
    }

    public DepensesCriteria(DepensesCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.montant = other.montant == null ? null : other.montant.copy();
        this.comments = other.comments == null ? null : other.comments.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.annule = other.annule == null ? null : other.annule.copy();
        this.typeDepenseId = other.typeDepenseId == null ? null : other.typeDepenseId.copy();
    }

    @Override
    public DepensesCriteria copy() {
        return new DepensesCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public BigDecimalFilter getMontant() {
        return montant;
    }

    public void setMontant(BigDecimalFilter montant) {
        this.montant = montant;
    }

    public StringFilter getComments() {
        return comments;
    }

    public void setComments(StringFilter comments) {
        this.comments = comments;
    }

    public LocalDateFilter getDate() {
        return date;
    }

    public void setDate(LocalDateFilter date) {
        this.date = date;
    }

    public BooleanFilter getAnnule() {
        return annule;
    }

    public void setAnnule(BooleanFilter annule) {
        this.annule = annule;
    }

    public LongFilter getTypeDepenseId() {
        return typeDepenseId;
    }

    public void setTypeDepenseId(LongFilter typeDepenseId) {
        this.typeDepenseId = typeDepenseId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DepensesCriteria that = (DepensesCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(montant, that.montant) &&
            Objects.equals(comments, that.comments) &&
            Objects.equals(date, that.date) &&
            Objects.equals(annule, that.annule) &&
            Objects.equals(typeDepenseId, that.typeDepenseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        montant,
        comments,
        date,
        annule,
        typeDepenseId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepensesCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (montant != null ? "montant=" + montant + ", " : "") +
                (comments != null ? "comments=" + comments + ", " : "") +
                (date != null ? "date=" + date + ", " : "") +
                (annule != null ? "annule=" + annule + ", " : "") +
                (typeDepenseId != null ? "typeDepenseId=" + typeDepenseId + ", " : "") +
            "}";
    }

}
