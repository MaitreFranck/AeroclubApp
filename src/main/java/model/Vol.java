package model;

import java.time.LocalDateTime;

public class Vol {
    private int id;
    private String immatriculationAvion; // Jointure
    private String nomPilote;           // Jointure
    private LocalDateTime dateHeureDepart;
    private LocalDateTime dateHeureArrivee;
    private double duree;

    public Vol(int id, String immatriculationAvion, String nomPilote, LocalDateTime dep, LocalDateTime arr, double duree) {
        this.id = id;
        this.immatriculationAvion = immatriculationAvion;
        this.nomPilote = nomPilote;
        this.dateHeureDepart = dep;
        this.dateHeureArrivee = arr;
        this.duree = duree;
    }

    // Getters
    public int getId() { return id; }
    public String getImmatriculationAvion() { return immatriculationAvion; }
    public String getNomPilote() { return nomPilote; }
    public LocalDateTime getDateHeureDepart() { return dateHeureDepart; }
    public LocalDateTime getDateHeureArrivee() { return dateHeureArrivee; }
    public double getDuree() { return duree; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setImmatriculationAvion(String immatriculationAvion) { this.immatriculationAvion = immatriculationAvion; }
    public void setNomPilote(String nomPilote) { this.nomPilote = nomPilote; }
    public void setDateHeureDepart(LocalDateTime dateHeureDepart) { this.dateHeureDepart = dateHeureDepart; }
    public void setDateHeureArrivee(LocalDateTime dateHeureArrivee) { this.dateHeureArrivee = dateHeureArrivee; }
    public void setDuree(double duree) { this.duree = duree; }
}