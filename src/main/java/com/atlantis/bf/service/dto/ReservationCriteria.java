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
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link com.atlantis.bf.domain.Reservation} entity. This class is used
 * in {@link com.atlantis.bf.web.rest.ReservationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /reservations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ReservationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter revervePour;

    private LocalDateFilter date;

    private BooleanFilter annule;

    private LongFilter clientId;

    public ReservationCriteria() {
    }

    public ReservationCriteria(ReservationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.revervePour = other.revervePour == null ? null : other.revervePour.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.annule = other.annule == null ? null : other.annule.copy();
        this.clientId = other.clientId == null ? null : other.clientId.copy();
    }

    @Override
    public ReservationCriteria copy() {
        return new ReservationCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getRevervePour() {
        return revervePour;
    }

    public void setRevervePour(LocalDateFilter revervePour) {
        this.revervePour = revervePour;
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
        final ReservationCriteria that = (ReservationCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(revervePour, that.revervePour) &&
            Objects.equals(date, that.date) &&
            Objects.equals(annule, that.annule) &&
            Objects.equals(clientId, that.clientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        revervePour,
        date,
        annule,
        clientId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReservationCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (revervePour != null ? "revervePour=" + revervePour + ", " : "") +
                (date != null ? "date=" + date + ", " : "") +
                (annule != null ? "annule=" + annule + ", " : "") +
                (clientId != null ? "clientId=" + clientId + ", " : "") +
            "}";
    }

}
