package ma.lias.app.model;

import java.time.LocalDateTime;

public class Utilisateur {

    private Long id;
    private String email;
    private String password;
    private String type; // ADMIN or MEMBRE
    private boolean actif;
    private LocalDateTime dateCreation;
    private String nom;
    private String prenom;

    // Sécurité : lien d'activation à durée de vie limitée (remplace l'envoi de mot de passe en clair)
    private String activationToken;
    private LocalDateTime tokenExpiration;

    public Utilisateur() {}

    public Utilisateur(Long id, String email, String password,
                       String type, boolean actif) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.type = type;
        this.actif = actif;
    }
    public String getNomComplet() {
        if (prenom != null && nom != null)
            return prenom + " " + nom;
        if (nom != null) return nom;
        return email;
    }

    public boolean isAdmin() {
        return "ADMIN".equals(this.type);
    }

    public boolean isMembre() {
        return "MEMBRE".equals(this.type);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public boolean isActif() { return actif; }
    public void setActif(boolean actif) { this.actif = actif; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
 
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getActivationToken() { return activationToken; }
    public void setActivationToken(String activationToken) { this.activationToken = activationToken; }

    public LocalDateTime getTokenExpiration() { return tokenExpiration; }
    public void setTokenExpiration(LocalDateTime tokenExpiration) { this.tokenExpiration = tokenExpiration; }

    /** Vrai si le compte a été créé mais que le membre n'a pas encore choisi son mot de passe. */
    public boolean isActivationEnAttente() {
        return activationToken != null;
    }
}