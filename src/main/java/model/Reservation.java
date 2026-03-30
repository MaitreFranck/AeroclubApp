package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Reservation {
    private int id;
    private String immatriculationAvion; // Récupéré via Jointure SQL
    private String nomMembre;            // Récupéré via Jointure SQL
    private LocalDate date;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private String statut;

    // Constructeur
    public Reservation(int id, String immatriculationAvion, String nomMembre, LocalDate date, LocalTime debut, LocalTime fin, String statut) {
        this.id = id;
        this.immatriculationAvion = immatriculationAvion;
        this.nomMembre = nomMembre;
        this.date = date;
        this.heureDebut = debut;
        this.heureFin = fin;
        this.statut = statut;
    }

    // --- GETTERS ---
    public int getId() { return id; }
    public String getImmatriculationAvion() { return immatriculationAvion; }
    public String getNomMembre() { return nomMembre; }
    public LocalDate getDate() { return date; }
    public LocalTime getHeureDebut() { return heureDebut; }
    public LocalTime getHeureFin() { return heureFin; }
    public String getStatut() { return statut; }

    // --- SETTERS ---
    public void setId(int id) { this.id = id; }
    public void setImmatriculationAvion(String immatriculationAvion) { this.immatriculationAvion = immatriculationAvion; }
    public void setNomMembre(String nomMembre) { this.nomMembre = nomMembre; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setHeureDebut(LocalTime heureDebut) { this.heureDebut = heureDebut; }
    public void setHeureFin(LocalTime heureFin) { this.heureFin = heureFin; }
    public void setStatut(String statut) { this.statut = statut; }
}