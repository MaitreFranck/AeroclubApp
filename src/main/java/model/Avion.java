package model;

public class Avion {
    private int id;
    private String immatriculation;
    private String modele;
    private int idCategorie;
    private String statut;
    private double heuresVol;

    public Avion(int id, String immatriculation, String modele, int idCategorie, String statut, double heuresVol) {
        this.id = id;
        this.immatriculation = immatriculation;
        this.modele = modele;
        this.idCategorie = idCategorie;
        this.statut = statut;
        this.heuresVol = heuresVol;
    }

    // Getters
    public int getId() { return id; }
    public String getImmatriculation() { return immatriculation; }
    public String getModele() { return modele; }
    public int getIdCategorie() { return idCategorie; }
    public String getStatut() { return statut; }
    public double getHeuresVol() { return heuresVol; }

    // Setters
    public void setImmatriculation(String immatriculation) { this.immatriculation = immatriculation; }
    public void setModele(String modele) { this.modele = modele; }
    public void setIdCategorie(int idCategorie) { this.idCategorie = idCategorie; }
    public void setStatut(String statut) { this.statut = statut; }
    public void setHeuresVol(double heuresVol) { this.heuresVol = heuresVol; }
}