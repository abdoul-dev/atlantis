package com.atlantis.bf.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * A Depenses.
 */
@Entity
@Table(name = "depenses")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Depenses implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "montant", precision = 21, scale = 2, nullable = false)
    private BigDecimal montant;

    @Column(name = "comments")
    private String comments;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    @Column(name = "annule", nullable = false)
    private Boolean annule;

    @ManyToOne
    @JsonIgnoreProperties(value = "depenses", allowSetters = true)
    private TypeDepense typeDepense;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public Depenses montant(BigDecimal montant) {
        this.montant = montant;
        return this;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public String getComments() {
        return comments;
    }

    public Depenses comments(String comments) {
        this.comments = comments;
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public LocalDate getDate() {
        return date;
    }

    public Depenses date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Boolean isAnnule() {
        return annule;
    }

    public Depenses annule(Boolean annule) {
        this.annule = annule;
        return this;
    }

    public void setAnnule(Boolean annule) {
        this.annule = annule;
    }

    public TypeDepense getTypeDepense() {
        return typeDepense;
    }

    public Depenses typeDepense(TypeDepense typeDepense) {
        this.typeDepense = typeDepense;
        return this;
    }

    public void setTypeDepense(TypeDepense typeDepense) {
        this.typeDepense = typeDepense;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Depenses)) {
            return false;
        }
        return id != null && id.equals(((Depenses) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Depenses{" +
            "id=" + getId() +
            ", montant=" + getMontant() +
            ", comments='" + getComments() + "'" +
            ", date='" + getDate() + "'" +
            ", annule='" + isAnnule() + "'" +
            "}";
    }
}
