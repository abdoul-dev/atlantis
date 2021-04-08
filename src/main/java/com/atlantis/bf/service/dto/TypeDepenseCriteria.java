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

/**
 * Criteria class for the {@link com.atlantis.bf.domain.TypeDepense} entity. This class is used
 * in {@link com.atlantis.bf.web.rest.TypeDepenseResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /type-depenses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TypeDepenseCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter libelle;

    private BooleanFilter isDisabled;

    private LongFilter depensesId;

    public TypeDepenseCriteria() {
    }

    public TypeDepenseCriteria(TypeDepenseCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.libelle = other.libelle == null ? null : other.libelle.copy();
        this.isDisabled = other.isDisabled == null ? null : other.isDisabled.copy();
        this.depensesId = other.depensesId == null ? null : other.depensesId.copy();
    }

    @Override
    public TypeDepenseCriteria copy() {
        return new TypeDepenseCriteria(this);
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

    public BooleanFilter getIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(BooleanFilter isDisabled) {
        this.isDisabled = isDisabled;
    }

    public LongFilter getDepensesId() {
        return depensesId;
    }

    public void setDepensesId(LongFilter depensesId) {
        this.depensesId = depensesId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TypeDepenseCriteria that = (TypeDepenseCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(libelle, that.libelle) &&
            Objects.equals(isDisabled, that.isDisabled) &&
            Objects.equals(depensesId, that.depensesId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        libelle,
        isDisabled,
        depensesId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeDepenseCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (libelle != null ? "libelle=" + libelle + ", " : "") +
                (isDisabled != null ? "isDisabled=" + isDisabled + ", " : "") +
                (depensesId != null ? "depensesId=" + depensesId + ", " : "") +
            "}";
    }

}
