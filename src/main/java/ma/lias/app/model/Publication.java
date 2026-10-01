package ma.lias.app.model;

import java.time.LocalDateTime;

public class Publication {

    private Long id;
    private String titre;
    private String auteurs;
    private int annee;
    private String type;
    private String description;
    private Long membreId;
    private LocalDateTime dateCreation;
    private String auteurPrincipal;
    private boolean supprime;

    public boolean isSupprime() {
        return supprime;
    }

    public void setSupprime(boolean supprime) {
        this.supprime = supprime;
    }
    
    public String getAuteurPrincipal() {
        return auteurPrincipal;
    }

    public void setAuteurPrincipal(String auteurPrincipal) {
        this.auteurPrincipal = auteurPrincipal;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getAuteurs() { return auteurs; }
    public void setAuteurs(String auteurs) { this.auteurs = auteurs; }

    public int getAnnee() { return annee; }
    public void setAnnee(int annee) { this.annee = annee; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getMembreId() { return membreId; }
    public void setMembreId(Long membreId) { this.membreId = membreId; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
}