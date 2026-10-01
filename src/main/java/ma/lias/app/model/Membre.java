package ma.lias.app.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Membre {

    // ── Identité ────────────────────────────────────────────
    private Long      id;
    private String    nom;
    private String    prenom;
    private String    email;
    private String    telephone;
    private LocalDate dateNaissance;     // confidentielle

    // ── Appartenance au labo ─────────────────────────────────
    private String    statut;            // PERMANENT, ASSOCIE, DOCTORANT, RETRAITE, ANCIEN
    private String    role;              // DIRECTEUR, VICE_DIRECTEUR, CHEF_EQUIPE, MEMBRE
    private Long      equipeId;
    private Long      utilisateurId;     // lien vers le compte de connexion

    // ── Dates clés ───────────────────────────────────────────
    private LocalDate dateAffiliation;   // date d'entrée au LIAS (clé métier CDC §4.2)
    private LocalDate dateDepart;        // null si encore dans le labo

    // ── Profil ───────────────────────────────────────────────
    private String    photo;             // chemin relatif du fichier image
    private String    biographie;
    private String    centresInteret;
    private String    etablissementOrigine;
    private String    laboratoireOrigine;

    // ── Système ──────────────────────────────────────────────
    private boolean       actif;
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private boolean droitPublication = true;

    public boolean isDroitPublication() { return droitPublication; }
    public void setDroitPublication(boolean droitPublication) { this.droitPublication = droitPublication; }

    // ════════════════════════════════════════════════════════
    // Constructeurs
    // ════════════════════════════════════════════════════════

    public Membre() {}

    /** Constructeur minimal utilisé par MembreDAO.findAll() */
    public Membre(Long id, String nom, String prenom,
                  String statut, boolean actif) {
        this.id     = id;
        this.nom    = nom;
        this.prenom = prenom;
        this.statut = statut;
        this.actif  = actif;
    }

    // ════════════════════════════════════════════════════════
    // Méthodes utilitaires
    // ════════════════════════════════════════════════════════

    /** Retourne "Nom Prénom" */
    public String getNomComplet() {
        return nom + " " + prenom;
    }

    /** Vrai si le membre est encore actif dans le labo */
    public boolean estActif() {
        return actif && dateDepart == null;
    }

    // ════════════════════════════════════════════════════════
    // Getters & Setters
    // ════════════════════════════════════════════════════════

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public LocalDate getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(LocalDate dateNaissance) { this.dateNaissance = dateNaissance; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Long getEquipeId() { return equipeId; }
    public void setEquipeId(Long equipeId) { this.equipeId = equipeId; }

    // Champ d'affichage uniquement (rempli par jointure), non stocké en base
    private String equipeNom;
    public String getEquipeNom() { return equipeNom; }
    public void setEquipeNom(String equipeNom) { this.equipeNom = equipeNom; }

    public Long getUtilisateurId() { return utilisateurId; }
    public void setUtilisateurId(Long utilisateurId) { this.utilisateurId = utilisateurId; }

    public LocalDate getDateAffiliation() { return dateAffiliation; }
    public void setDateAffiliation(LocalDate dateAffiliation) { this.dateAffiliation = dateAffiliation; }

    public LocalDate getDateDepart() { return dateDepart; }
    public void setDateDepart(LocalDate dateDepart) { this.dateDepart = dateDepart; }

    public String getPhoto() { return photo; }
    public void setPhoto(String photo) { this.photo = photo; }

    public String getBiographie() { return biographie; }
    public void setBiographie(String biographie) { this.biographie = biographie; }

    public String getCentresInteret() { return centresInteret; }
    public void setCentresInteret(String centresInteret) { this.centresInteret = centresInteret; }

    public String getEtablissementOrigine() { return etablissementOrigine; }
    public void setEtablissementOrigine(String etablissementOrigine) {
        this.etablissementOrigine = etablissementOrigine;
    }

    public String getLaboratoireOrigine() { return laboratoireOrigine; }
    public void setLaboratoireOrigine(String laboratoireOrigine) {
        this.laboratoireOrigine = laboratoireOrigine;
    }

    public boolean isActif() { return actif; }
    public void setActif(boolean actif) { this.actif = actif; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }

    public LocalDateTime getDateModification() { return dateModification; }
    public void setDateModification(LocalDateTime dateModification) {
        this.dateModification = dateModification;
    }
}