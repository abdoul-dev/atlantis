package com.atlantis.bf.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link com.atlantis.bf.domain.TypeDepense} entity.
 */
public class TypeDepenseDTO implements Serializable {
    
    private Long id;

    @Size(max = 50)
    private String libelle;

    @NotNull
    private Boolean isDisabled;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Boolean isIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(Boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeDepenseDTO)) {
            return false;
        }

        return id != null && id.equals(((TypeDepenseDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeDepenseDTO{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", isDisabled='" + isIsDisabled() + "'" +
            "}";
    }
}
