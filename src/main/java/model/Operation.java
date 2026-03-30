package model;

import java.time.LocalDateTime;

public class Operation {
    private int id;
    private String nomMembre; // Jointure
    private LocalDateTime dateOperation;
    private double montant;
    private String type; // 'debit', 'credit'
    private String modePaiement;

    public Operation(int id, String nomMembre, LocalDateTime date, double montant, String type, String mode) {
        this.id = id;
        this.nomMembre = nomMembre;
        this.dateOperation = date;
        this.montant = montant;
        this.type = type;
        this.modePaiement = mode;
    }

    // Getters
    public int getId() { return id; }
    public String getNomMembre() { return nomMembre; }
    public LocalDateTime getDateOperation() { return dateOperation; }
    public double getMontant() { return montant; }
    public String getType() { return type; }
    public String getModePaiement() { return modePaiement; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setNomMembre(String nomMembre) { this.nomMembre = nomMembre; }
    public void setDateOperation(LocalDateTime dateOperation) { this.dateOperation = dateOperation; }
    public void setMontant(double montant) { this.montant = montant; }
    public void setType(String type) { this.type = type; }
    public void setModePaiement(String modePaiement) { this.modePaiement = modePaiement; }
}