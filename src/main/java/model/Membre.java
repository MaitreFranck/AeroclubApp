package model;

import java.time.LocalDate;

public class Membre {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private LocalDate dateNaissance;
    private String numeroLicence;
    private String typeMembre; // pilote, eleve, instructeur, admin
    private String statut;     // actif, inactif
    private double soldeCompte;
    private String droitsUtilisateurs; // utilisateur, consulteur, administrateur

    public Membre(int id, String nom, String prenom, String email, String telephone,
                  LocalDate dateNaissance, String numeroLicence, String typeMembre,
                  String statut, double soldeCompte, String droitsUtilisateurs) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.dateNaissance = dateNaissance;
        this.numeroLicence = numeroLicence;
        this.typeMembre = typeMembre;
        this.statut = statut;
        this.soldeCompte = soldeCompte;
        this.droitsUtilisateurs = droitsUtilisateurs;
    }

    // Getters
    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getEmail() { return email; }
    public String getTelephone() { return telephone; }
    public LocalDate getDateNaissance() { return dateNaissance; }
    public String getNumeroLicence() { return numeroLicence; }
    public String getTypeMembre() { return typeMembre; }
    public String getStatut() { return statut; }
    public double getSoldeCompte() { return soldeCompte; }
    public String getDroitsUtilisateurs() { return droitsUtilisateurs; }

    // Setters
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setEmail(String email) { this.email = email; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public void setDateNaissance(LocalDate dateNaissance) { this.dateNaissance = dateNaissance; }
    public void setNumeroLicence(String numeroLicence) { this.numeroLicence = numeroLicence; }
    public void setTypeMembre(String typeMembre) { this.typeMembre = typeMembre; }
    public void setStatut(String statut) { this.statut = statut; }
    public void setSoldeCompte(double soldeCompte) { this.soldeCompte = soldeCompte; }
    public void setDroitsUtilisateurs(String droits) { this.droitsUtilisateurs = droits; }
}