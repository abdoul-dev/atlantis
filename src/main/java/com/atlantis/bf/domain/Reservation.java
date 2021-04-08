package com.atlantis.bf.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Reservation.
 */
@Entity
@Table(name = "reservation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "reverve_pour", nullable = false)
    private LocalDate revervePour;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    @Column(name = "annule", nullable = false)
    private Boolean annule;

    @ManyToOne
    @JsonIgnoreProperties(value = "reservations", allowSetters = true)
    private Client client;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getRevervePour() {
        return revervePour;
    }

    public Reservation revervePour(LocalDate revervePour) {
        this.revervePour = revervePour;
        return this;
    }

    public void setRevervePour(LocalDate revervePour) {
        this.revervePour = revervePour;
    }

    public LocalDate getDate() {
        return date;
    }

    public Reservation date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Boolean isAnnule() {
        return annule;
    }

    public Reservation annule(Boolean annule) {
        this.annule = annule;
        return this;
    }

    public void setAnnule(Boolean annule) {
        this.annule = annule;
    }

    public Client getClient() {
        return client;
    }

    public Reservation client(Client client) {
        this.client = client;
        return this;
    }

    public void setClient(Client client) {
        this.client = client;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reservation)) {
            return false;
        }
        return id != null && id.equals(((Reservation) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Reservation{" +
            "id=" + getId() +
            ", revervePour='" + getRevervePour() + "'" +
            ", date='" + getDate() + "'" +
            ", annule='" + isAnnule() + "'" +
            "}";
    }
}
