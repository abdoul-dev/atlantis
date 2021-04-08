package com.atlantis.bf.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * A Products.
 */
@Entity
@Table(name = "products")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Products implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "libelle", length = 100, nullable = false, unique = true)
    private String libelle;

    @NotNull
    @Column(name = "prix_vente", precision = 21, scale = 2, nullable = false)
    private BigDecimal prixVente;

    @NotNull
    @Column(name = "is_disabled", nullable = false)
    private Boolean isDisabled;

    @Column(name = "quantite", precision = 21, scale = 2)
    private BigDecimal quantite;

    @Column(name = "comments")
    private String comments;

    @OneToMany(mappedBy = "products")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<LigneEntreeStock> ligneStocks = new HashSet<>();

    @OneToMany(mappedBy = "products")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<LignesVentes> ligneVentes = new HashSet<>();

    @OneToMany(mappedBy = "products")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<LignesReservation> ligneReservations = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "products", allowSetters = true)
    private TypeProduit typeProduit;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public Products libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public BigDecimal getPrixVente() {
        return prixVente;
    }

    public Products prixVente(BigDecimal prixVente) {
        this.prixVente = prixVente;
        return this;
    }

    public void setPrixVente(BigDecimal prixVente) {
        this.prixVente = prixVente;
    }

    public Boolean isIsDisabled() {
        return isDisabled;
    }

    public Products isDisabled(Boolean isDisabled) {
        this.isDisabled = isDisabled;
        return this;
    }

    public void setIsDisabled(Boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    public BigDecimal getQuantite() {
        return quantite;
    }

    public Products quantite(BigDecimal quantite) {
        this.quantite = quantite;
        return this;
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public String getComments() {
        return comments;
    }

    public Products comments(String comments) {
        this.comments = comments;
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Set<LigneEntreeStock> getLigneStocks() {
        return ligneStocks;
    }

    public Products ligneStocks(Set<LigneEntreeStock> ligneEntreeStocks) {
        this.ligneStocks = ligneEntreeStocks;
        return this;
    }

    public Products addLigneStock(LigneEntreeStock ligneEntreeStock) {
        this.ligneStocks.add(ligneEntreeStock);
        ligneEntreeStock.setProducts(this);
        return this;
    }

    public Products removeLigneStock(LigneEntreeStock ligneEntreeStock) {
        this.ligneStocks.remove(ligneEntreeStock);
        ligneEntreeStock.setProducts(null);
        return this;
    }

    public void setLigneStocks(Set<LigneEntreeStock> ligneEntreeStocks) {
        this.ligneStocks = ligneEntreeStocks;
    }

    public Set<LignesVentes> getLigneVentes() {
        return ligneVentes;
    }

    public Products ligneVentes(Set<LignesVentes> lignesVentes) {
        this.ligneVentes = lignesVentes;
        return this;
    }

    public Products addLigneVentes(LignesVentes lignesVentes) {
        this.ligneVentes.add(lignesVentes);
        lignesVentes.setProducts(this);
        return this;
    }

    public Products removeLigneVentes(LignesVentes lignesVentes) {
        this.ligneVentes.remove(lignesVentes);
        lignesVentes.setProducts(null);
        return this;
    }

    public void setLigneVentes(Set<LignesVentes> lignesVentes) {
        this.ligneVentes = lignesVentes;
    }

    public Set<LignesReservation> getLigneReservations() {
        return ligneReservations;
    }

    public Products ligneReservations(Set<LignesReservation> lignesReservations) {
        this.ligneReservations = lignesReservations;
        return this;
    }

    public Products addLigneReservation(LignesReservation lignesReservation) {
        this.ligneReservations.add(lignesReservation);
        lignesReservation.setProducts(this);
        return this;
    }

    public Products removeLigneReservation(LignesReservation lignesReservation) {
        this.ligneReservations.remove(lignesReservation);
        lignesReservation.setProducts(null);
        return this;
    }

    public void setLigneReservations(Set<LignesReservation> lignesReservations) {
        this.ligneReservations = lignesReservations;
    }

    public TypeProduit getTypeProduit() {
        return typeProduit;
    }

    public Products typeProduit(TypeProduit typeProduit) {
        this.typeProduit = typeProduit;
        return this;
    }

    public void setTypeProduit(TypeProduit typeProduit) {
        this.typeProduit = typeProduit;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Products)) {
            return false;
        }
        return id != null && id.equals(((Products) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Products{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", prixVente=" + getPrixVente() +
            ", isDisabled='" + isIsDisabled() + "'" +
            ", quantite=" + getQuantite() +
            ", comments='" + getComments() + "'" +
            "}";
    }
}
