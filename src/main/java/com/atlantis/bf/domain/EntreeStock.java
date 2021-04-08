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
 * A EntreeStock.
 */
@Entity
@Table(name = "entree_stock")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class EntreeStock implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "montant", precision = 21, scale = 2, nullable = false)
    private BigDecimal montant;

    @NotNull
    @Column(name = "annule", nullable = false)
    private Boolean annule;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @OneToMany(mappedBy = "entreestock")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<LigneEntreeStock> ligneEntreeStocks = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "stocks", allowSetters = true)
    private Fournisseur fournisseur;

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

    public EntreeStock montant(BigDecimal montant) {
        this.montant = montant;
        return this;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public Boolean isAnnule() {
        return annule;
    }

    public EntreeStock annule(Boolean annule) {
        this.annule = annule;
        return this;
    }

    public void setAnnule(Boolean annule) {
        this.annule = annule;
    }

    public LocalDate getDate() {
        return date;
    }

    public EntreeStock date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Set<LigneEntreeStock> getLigneEntreeStocks() {
        return ligneEntreeStocks;
    }

    public EntreeStock ligneEntreeStocks(Set<LigneEntreeStock> ligneEntreeStocks) {
        this.ligneEntreeStocks = ligneEntreeStocks;
        return this;
    }

    public EntreeStock addLigneEntreeStock(LigneEntreeStock ligneEntreeStock) {
        this.ligneEntreeStocks.add(ligneEntreeStock);
        ligneEntreeStock.setEntreestock(this);
        return this;
    }

    public EntreeStock removeLigneEntreeStock(LigneEntreeStock ligneEntreeStock) {
        this.ligneEntreeStocks.remove(ligneEntreeStock);
        ligneEntreeStock.setEntreestock(null);
        return this;
    }

    public void setLigneEntreeStocks(Set<LigneEntreeStock> ligneEntreeStocks) {
        this.ligneEntreeStocks = ligneEntreeStocks;
    }

    public Fournisseur getFournisseur() {
        return fournisseur;
    }

    public EntreeStock fournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
        return this;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EntreeStock)) {
            return false;
        }
        return id != null && id.equals(((EntreeStock) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EntreeStock{" +
            "id=" + getId() +
            ", montant=" + getMontant() +
            ", annule='" + isAnnule() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
