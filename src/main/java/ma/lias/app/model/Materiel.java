package ma.lias.app.model;

import java.sql.Date;

public class Materiel {

    private Long id;
    private String nom;
    private String type;
    private String marque;
    private int quantiteTotale;
    private int quantiteDisponible;
    private Date dateAchat;

    // Constructeur vide obligatoire
    public Materiel() {
    }

    // Constructeur complet
    public Materiel(Long id, String nom, String type, String marque, int quantiteTotale, int quantiteDisponible, Date dateAchat) {
        this.id = id;
        this.nom = nom;
        this.type = type;
        this.marque = marque;
        this.quantiteTotale = quantiteTotale;
        this.quantiteDisponible = quantiteDisponible;
        this.dateAchat = dateAchat;
    }

    // Getters et Setters complets
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public int getQuantiteTotale() {
        return quantiteTotale;
    }

    public void setQuantiteTotale(int quantiteTotale) {
        this.quantiteTotale = quantiteTotale;
    }

    public int getQuantiteDisponible() {
        return quantiteDisponible;
    }

    public void setQuantiteDisponible(int quantiteDisponible) {
        this.quantiteDisponible = quantiteDisponible;
    }

    public Date getDateAchat() {
        return dateAchat;
    }

    public void setDateAchat(Date date) {
        this.dateAchat = date;
    }
}