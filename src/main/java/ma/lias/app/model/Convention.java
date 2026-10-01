package ma.lias.app.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Convention {

    private Long id;
    private String titre;
    private String partenaire;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String description;
    private String cheminFichier;
    private LocalDateTime dateCreation;
    private boolean archive;

    // ✅ Getters & Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getPartenaire() { return partenaire; }
    public void setPartenaire(String partenaire) { this.partenaire = partenaire; }

    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }

    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCheminFichier() { return cheminFichier; }
    public void setCheminFichier(String cheminFichier) { this.cheminFichier = cheminFichier; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
    
    public boolean getArchive() { return archive; }
    public void  setArchive(boolean archive) { this.archive = archive; }
	
	
}