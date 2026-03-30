package model;

import java.time.LocalDate;

public class Cotisation {
    private int id;
    private String nomMembre; // Jointure SQL
    private LocalDate datePaiement;
    private double montant;
    private int annee;

    public Cotisation(int id, String nomMembre, LocalDate datePaiement, double montant, int annee) {
        this.id = id;
        this.nomMembre = nomMembre;
        this.datePaiement = datePaiement;
        this.montant = montant;
        this.annee = annee;
    }

    // Getters
    public int getId() { return id; }
    public String getNomMembre() { return nomMembre; }
    public LocalDate getDatePaiement() { return datePaiement; }
    public double getMontant() { return montant; }
    public int getAnnee() { return annee; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setNomMembre(String nomMembre) { this.nomMembre = nomMembre; }
    public void setDatePaiement(LocalDate datePaiement) { this.datePaiement = datePaiement; }
    public void setMontant(double montant) { this.montant = montant; }
    public void setAnnee(int annee) { this.annee = annee; }
}