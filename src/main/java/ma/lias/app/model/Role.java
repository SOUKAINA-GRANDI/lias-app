package ma.lias.app.model;

import java.time.LocalDate;

import ma.lias.app.enums.RoleType;

/**
 * Historique des rôles de gouvernance d'un membre (CDC §7 ⭐).
 * Chaque changement de rôle crée une NOUVELLE ligne (date_debut / date_fin),
 * la ligne précédente n'est jamais supprimée : historique complet conservé.
 */
public class Role {

    private Long id;
    private Long membreId;
    private String nom;          // DIRECTEUR, VICE_DIRECTEUR, CHEF_EQUIPE, MEMBRE_EQUIPE
    private LocalDate dateDebut;
    private LocalDate dateFin;   // null = rôle actuellement actif

    // ── Champs d'affichage (jointure avec membre), non stockés ─────────
    private String membreNom;
    private String membrePrenom;

    public Role() {}

    /** Vrai si ce rôle est le rôle actif actuel (pas encore clôturé) */
    public boolean isActif() {
        return dateFin == null;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getMembreId() { return membreId; }
    public void setMembreId(Long membreId) { this.membreId = membreId; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public RoleType getNomEnum() { return nom != null ? RoleType.valueOf(nom) : null; }
    public void setNomEnum(RoleType type) { this.nom = (type != null) ? type.name() : null; }

    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }

    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }

    public String getMembreNom() { return membreNom; }
    public void setMembreNom(String membreNom) { this.membreNom = membreNom; }

    public String getMembrePrenom() { return membrePrenom; }
    public void setMembrePrenom(String membrePrenom) { this.membrePrenom = membrePrenom; }
}
