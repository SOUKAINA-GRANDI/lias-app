package ma.lias.app.model;

import java.time.LocalDate;

public class AttributionMateriel {

    private Long id;
    private Long materielId;
    private Long membreId;
    private LocalDate dateAttribution;
    private LocalDate dateRetour;
    private String nom;    // rempli par la jointure, pas une colonne de attribution_materiel
    private String type;

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    // Constructeur vide obligatoire
    public AttributionMateriel() {
    }

    // Constructeur complet
    public AttributionMateriel(Long id, Long materielId, Long membreId, LocalDate dateAttribution, LocalDate dateRetour) {
        this.id = id;
        this.materielId = materielId;
        this.membreId = membreId;
        this.dateAttribution = dateAttribution;
        this.dateRetour = dateRetour;
    }

    // Getters et Setters complets
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMaterielId() {
        return materielId;
    }

    public void setMaterielId(Long materielId) {
        this.materielId = materielId;
    }

    public Long getMembreId() {
        return membreId;
    }

    public void setMembreId(Long membreId) {
        this.membreId = membreId;
    }

    public LocalDate getDateAttribution() {
        return dateAttribution;
    }

    public void setDateAttribution(LocalDate dateAttribution) {
        this.dateAttribution = dateAttribution;
    }

    public LocalDate getDateRetour() {
        return dateRetour;
    }

    public void setDateRetour(LocalDate dateRetour) {
        this.dateRetour = dateRetour;
    }
}