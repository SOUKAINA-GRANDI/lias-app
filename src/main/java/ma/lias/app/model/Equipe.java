package ma.lias.app.model;

import java.time.LocalDateTime;

public class Equipe {

    private Long id;
    private String nom;
    private String description;
    private LocalDateTime dateCreation;
    private Long chefId;
    private String chefNom;
    private String chefPrenom;

    public String getChefPrenom() { return chefPrenom; }
    public void setChefPrenom(String chefPrenom) { this.chefPrenom = chefPrenom; }

    public String getChefNom() { return chefNom; }
    public void setChefNom(String chefNom) { this.chefNom = chefNom; }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
    
    public Long getChefId() { return chefId; }
    public void setChefId(Long chefId) { this.chefId = chefId; }

}