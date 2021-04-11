package com.atlantis.bf.reporting;

import java.math.BigDecimal;
import java.time.LocalDate;


public class depenses {

    private BigDecimal montant;

    private String comments;

    private LocalDate date;

    private Boolean annule;

    private String typeDepenseLibelle;

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Boolean isAnnule() {
        return annule;
    }

    public void setAnnule(Boolean annule) {
        this.annule = annule;
    }

    public String getTypeDepenseLibelle() {
        return typeDepenseLibelle;
    }

    public void setTypeDepenseLibelle(String typeDepenseLibelle) {
        this.typeDepenseLibelle = typeDepenseLibelle;
    }

}
