package ma.lias.app.model;

import java.time.LocalDateTime;

public class RapportAnnuel {

    private Long id;
    private int annee;
    private LocalDateTime dateGeneration;
    private int nbPublications;
    private int nbEvenements;
    private int nbConventions;
    private int membresActifs;

    public int getNbConventions() { return nbConventions; }
    public void setNbConventions(int nbConventions) { this.nbConventions = nbConventions; }

    public int getMembresActifs() { return membresActifs; }
    public void setMembresActifs(int membresActifs) { this.membresActifs = membresActifs; }
    // ✅ Getters & Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getAnnee() {
        return annee;
    }

    public void setAnnee(int annee) {
        this.annee = annee;
    }

    public LocalDateTime getDateGeneration() {
        return dateGeneration;
    }

    public void setDateGeneration(LocalDateTime dateGeneration) {
        this.dateGeneration = dateGeneration;
    }

    public int getNbPublications() {
        return nbPublications;
    }

    public void setNbPublications(int nbPublications) {
        this.nbPublications = nbPublications;
    }

    public int getNbEvenements() {
        return nbEvenements;
    }

    public void setNbEvenements(int nbEvenements) {
        this.nbEvenements = nbEvenements;
    }
}