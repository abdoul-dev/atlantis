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
 * Criteria class for the {@link com.atlantis.bf.domain.Ventes} entity. This class is used
 * in {@link com.atlantis.bf.web.rest.VentesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ventes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class VentesCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BigDecimalFilter montantInitial;

    private BigDecimalFilter remise;

    private BigDecimalFilter montantFinal;

    private LocalDateFilter date;

    private BooleanFilter annule;

    private LongFilter lignesVentesId;

    private LongFilter clientId;

    public VentesCriteria() {
    }

    public VentesCriteria(VentesCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.montantInitial = other.montantInitial == null ? null : other.montantInitial.copy();
        this.remise = other.remise == null ? null : other.remise.copy();
        this.montantFinal = other.montantFinal == null ? null : other.montantFinal.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.annule = other.annule == null ? null : other.annule.copy();
        this.lignesVentesId = other.lignesVentesId == null ? null : other.lignesVentesId.copy();
        this.clientId = other.clientId == null ? null : other.clientId.copy();
    }

    @Override
    public VentesCriteria copy() {
        return new VentesCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public BigDecimalFilter getMontantInitial() {
        return montantInitial;
    }

    public void setMontantInitial(BigDecimalFilter montantInitial) {
        this.montantInitial = montantInitial;
    }

    public BigDecimalFilter getRemise() {
        return remise;
    }

    public void setRemise(BigDecimalFilter remise) {
        this.remise = remise;
    }

    public BigDecimalFilter getMontantFinal() {
        return montantFinal;
    }

    public void setMontantFinal(BigDecimalFilter montantFinal) {
        this.montantFinal = montantFinal;
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

    public LongFilter getLignesVentesId() {
        return lignesVentesId;
    }

    public void setLignesVentesId(LongFilter lignesVentesId) {
        this.lignesVentesId = lignesVentesId;
    }

    public LongFilter getClientId() {
        return clientId;
    }

    public void setClientId(LongFilter clientId) {
        this.clientId = clientId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final VentesCriteria that = (VentesCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(montantInitial, that.montantInitial) &&
            Objects.equals(remise, that.remise) &&
            Objects.equals(montantFinal, that.montantFinal) &&
            Objects.equals(date, that.date) &&
            Objects.equals(annule, that.annule) &&
            Objects.equals(lignesVentesId, that.lignesVentesId) &&
            Objects.equals(clientId, that.clientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        montantInitial,
        remise,
        montantFinal,
        date,
        annule,
        lignesVentesId,
        clientId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VentesCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (montantInitial != null ? "montantInitial=" + montantInitial + ", " : "") +
                (remise != null ? "remise=" + remise + ", " : "") +
                (montantFinal != null ? "montantFinal=" + montantFinal + ", " : "") +
                (date != null ? "date=" + date + ", " : "") +
                (annule != null ? "annule=" + annule + ", " : "") +
                (lignesVentesId != null ? "lignesVentesId=" + lignesVentesId + ", " : "") +
                (clientId != null ? "clientId=" + clientId + ", " : "") +
            "}";
    }

}
