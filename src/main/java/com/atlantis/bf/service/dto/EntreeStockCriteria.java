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
 * Criteria class for the {@link com.atlantis.bf.domain.EntreeStock} entity. This class is used
 * in {@link com.atlantis.bf.web.rest.EntreeStockResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /entree-stocks?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EntreeStockCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BigDecimalFilter montant;

    private BooleanFilter annule;

    private LocalDateFilter date;

    private LongFilter ligneEntreeStockId;

    private LongFilter fournisseurId;

    public EntreeStockCriteria() {
    }

    public EntreeStockCriteria(EntreeStockCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.montant = other.montant == null ? null : other.montant.copy();
        this.annule = other.annule == null ? null : other.annule.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.ligneEntreeStockId = other.ligneEntreeStockId == null ? null : other.ligneEntreeStockId.copy();
        this.fournisseurId = other.fournisseurId == null ? null : other.fournisseurId.copy();
    }

    @Override
    public EntreeStockCriteria copy() {
        return new EntreeStockCriteria(this);
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

    public BooleanFilter getAnnule() {
        return annule;
    }

    public void setAnnule(BooleanFilter annule) {
        this.annule = annule;
    }

    public LocalDateFilter getDate() {
        return date;
    }

    public void setDate(LocalDateFilter date) {
        this.date = date;
    }

    public LongFilter getLigneEntreeStockId() {
        return ligneEntreeStockId;
    }

    public void setLigneEntreeStockId(LongFilter ligneEntreeStockId) {
        this.ligneEntreeStockId = ligneEntreeStockId;
    }

    public LongFilter getFournisseurId() {
        return fournisseurId;
    }

    public void setFournisseurId(LongFilter fournisseurId) {
        this.fournisseurId = fournisseurId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EntreeStockCriteria that = (EntreeStockCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(montant, that.montant) &&
            Objects.equals(annule, that.annule) &&
            Objects.equals(date, that.date) &&
            Objects.equals(ligneEntreeStockId, that.ligneEntreeStockId) &&
            Objects.equals(fournisseurId, that.fournisseurId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        montant,
        annule,
        date,
        ligneEntreeStockId,
        fournisseurId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EntreeStockCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (montant != null ? "montant=" + montant + ", " : "") +
                (annule != null ? "annule=" + annule + ", " : "") +
                (date != null ? "date=" + date + ", " : "") +
                (ligneEntreeStockId != null ? "ligneEntreeStockId=" + ligneEntreeStockId + ", " : "") +
                (fournisseurId != null ? "fournisseurId=" + fournisseurId + ", " : "") +
            "}";
    }

}
