package ma.lias.app.model;

import java.time.LocalDateTime;

/**
 * Snapshot des valeurs AVANT modification d'un membre (CDC §3 ⭐ - historisation du profil).
 * Chaque appel à MembreService.update()/changeStatut()/... insère une nouvelle ligne
 * avec les anciennes valeurs : aucune donnée n'est jamais écrasée ni perdue.
 */
public class MembreHistorique {

    private Long id;
    private Long membreId;
    private String nom;
    private String prenom;
    private String telephone;
    private String biographie;
    private String centresInteret;
    private String statut;
    private String role;
    private Long equipeId;
    private String photo;
    private LocalDateTime dateModification;

    // ── Champ d'affichage (jointure), non stocké ──
    private String equipeNom;

    public MembreHistorique() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getMembreId() { return membreId; }
    public void setMembreId(Long membreId) { this.membreId = membreId; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getBiographie() { return biographie; }
    public void setBiographie(String biographie) { this.biographie = biographie; }

    public String getCentresInteret() { return centresInteret; }
    public void setCentresInteret(String centresInteret) { this.centresInteret = centresInteret; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Long getEquipeId() { return equipeId; }
    public void setEquipeId(Long equipeId) { this.equipeId = equipeId; }

    public String getPhoto() { return photo; }
    public void setPhoto(String photo) { this.photo = photo; }

    public LocalDateTime getDateModification() { return dateModification; }
    public void setDateModification(LocalDateTime dateModification) { this.dateModification = dateModification; }

    public String getEquipeNom() { return equipeNom; }
    public void setEquipeNom(String equipeNom) { this.equipeNom = equipeNom; }
}