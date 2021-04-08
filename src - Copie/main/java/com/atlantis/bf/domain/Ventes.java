package com.atlantis.bf.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Ventes.
 */
@Entity
@Table(name = "ventes")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Ventes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "montant_initial", precision = 21, scale = 2, nullable = false)
    private BigDecimal montantInitial;

    @NotNull
    @Column(name = "remise", precision = 21, scale = 2, nullable = false)
    private BigDecimal remise;

    @NotNull
    @Column(name = "montant_final", precision = 21, scale = 2, nullable = false)
    private BigDecimal montantFinal;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    @Column(name = "annule", nullable = false)
    private Boolean annule;

    @OneToMany(mappedBy = "ventes")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<LignesVentes> lignesVentes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "ventes", allowSetters = true)
    private Client client;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getMontantInitial() {
        return montantInitial;
    }

    public Ventes montantInitial(BigDecimal montantInitial) {
        this.montantInitial = montantInitial;
        return this;
    }

    public void setMontantInitial(BigDecimal montantInitial) {
        this.montantInitial = montantInitial;
    }

    public BigDecimal getRemise() {
        return remise;
    }

    public Ventes remise(BigDecimal remise) {
        this.remise = remise;
        return this;
    }

    public void setRemise(BigDecimal remise) {
        this.remise = remise;
    }

    public BigDecimal getMontantFinal() {
        return montantFinal;
    }

    public Ventes montantFinal(BigDecimal montantFinal) {
        this.montantFinal = montantFinal;
        return this;
    }

    public void setMontantFinal(BigDecimal montantFinal) {
        this.montantFinal = montantFinal;
    }

    public LocalDate getDate() {
        return date;
    }

    public Ventes date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Boolean isAnnule() {
        return annule;
    }

    public Ventes annule(Boolean annule) {
        this.annule = annule;
        return this;
    }

    public void setAnnule(Boolean annule) {
        this.annule = annule;
    }

    public Set<LignesVentes> getLignesVentes() {
        return lignesVentes;
    }

    public Ventes lignesVentes(Set<LignesVentes> lignesVentes) {
        this.lignesVentes = lignesVentes;
        return this;
    }

    public Ventes addLignesVentes(LignesVentes lignesVentes) {
        this.lignesVentes.add(lignesVentes);
        lignesVentes.setVentes(this);
        return this;
    }

    public Ventes removeLignesVentes(LignesVentes lignesVentes) {
        this.lignesVentes.remove(lignesVentes);
        lignesVentes.setVentes(null);
        return this;
    }

    public void setLignesVentes(Set<LignesVentes> lignesVentes) {
        this.lignesVentes = lignesVentes;
    }

    public Client getClient() {
        return client;
    }

    public Ventes client(Client client) {
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
        if (!(o instanceof Ventes)) {
            return false;
        }
        return id != null && id.equals(((Ventes) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ventes{" +
            "id=" + getId() +
            ", montantInitial=" + getMontantInitial() +
            ", remise=" + getRemise() +
            ", montantFinal=" + getMontantFinal() +
            ", date='" + getDate() + "'" +
            ", annule='" + isAnnule() + "'" +
            "}";
    }
}
