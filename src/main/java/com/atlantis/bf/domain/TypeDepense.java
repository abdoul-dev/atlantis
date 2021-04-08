package com.atlantis.bf.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A TypeDepense.
 */
@Entity
@Table(name = "type_depense")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TypeDepense implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Size(max = 50)
    @Column(name = "libelle", length = 50, unique = true)
    private String libelle;

    @NotNull
    @Column(name = "is_disabled", nullable = false)
    private Boolean isDisabled;

    @OneToMany(mappedBy = "typeDepense")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Depenses> depenses = new HashSet<>();

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

    public TypeDepense libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Boolean isIsDisabled() {
        return isDisabled;
    }

    public TypeDepense isDisabled(Boolean isDisabled) {
        this.isDisabled = isDisabled;
        return this;
    }

    public void setIsDisabled(Boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    public Set<Depenses> getDepenses() {
        return depenses;
    }

    public TypeDepense depenses(Set<Depenses> depenses) {
        this.depenses = depenses;
        return this;
    }

    public TypeDepense addDepenses(Depenses depenses) {
        this.depenses.add(depenses);
        depenses.setTypeDepense(this);
        return this;
    }

    public TypeDepense removeDepenses(Depenses depenses) {
        this.depenses.remove(depenses);
        depenses.setTypeDepense(null);
        return this;
    }

    public void setDepenses(Set<Depenses> depenses) {
        this.depenses = depenses;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeDepense)) {
            return false;
        }
        return id != null && id.equals(((TypeDepense) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeDepense{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", isDisabled='" + isIsDisabled() + "'" +
            "}";
    }
}
