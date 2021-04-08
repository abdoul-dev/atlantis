package com.atlantis.bf.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A DTO for the {@link com.atlantis.bf.domain.LignesReservation} entity.
 */
public class LignesReservationDTO implements Serializable {
    
    private Long id;

    @NotNull
    private BigDecimal quantite;


    private Long productsId;

    private String productsLibelle;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getQuantite() {
        return quantite;
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public Long getProductsId() {
        return productsId;
    }

    public void setProductsId(Long productsId) {
        this.productsId = productsId;
    }

    public String getProductsLibelle() {
        return productsLibelle;
    }

    public void setProductsLibelle(String productsLibelle) {
        this.productsLibelle = productsLibelle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LignesReservationDTO)) {
            return false;
        }

        return id != null && id.equals(((LignesReservationDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LignesReservationDTO{" +
            "id=" + getId() +
            ", quantite=" + getQuantite() +
            ", productsId=" + getProductsId() +
            ", productsLibelle='" + getProductsLibelle() + "'" +
            "}";
    }
}
