package com.atlantis.bf.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A TypeProduit.
 */
@Entity
@Table(name = "type_produit")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TypeProduit implements Serializable {

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
    @Column(name = "is_disabled", nullable = false)
    private Boolean isDisabled;

    @OneToMany(mappedBy = "typeProduit")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Products> products = new HashSet<>();

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

    public TypeProduit libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Boolean isIsDisabled() {
        return isDisabled;
    }

    public TypeProduit isDisabled(Boolean isDisabled) {
        this.isDisabled = isDisabled;
        return this;
    }

    public void setIsDisabled(Boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    public Set<Products> getProducts() {
        return products;
    }

    public TypeProduit products(Set<Products> products) {
        this.products = products;
        return this;
    }

    public TypeProduit addProducts(Products products) {
        this.products.add(products);
        products.setTypeProduit(this);
        return this;
    }

    public TypeProduit removeProducts(Products products) {
        this.products.remove(products);
        products.setTypeProduit(null);
        return this;
    }

    public void setProducts(Set<Products> products) {
        this.products = products;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeProduit)) {
            return false;
        }
        return id != null && id.equals(((TypeProduit) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeProduit{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", isDisabled='" + isIsDisabled() + "'" +
            "}";
    }
}
