package ma.lias.app.model;

import java.time.LocalDateTime;

import ma.lias.app.enums.StatutDemande;

public class DemandeAdhesion {

    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String cvPath;
    private String motivation;
    private String statut;
    private LocalDateTime dateDemande;
    private String statutVise;     // PERMANENT, ASSOCIE ou DOCTORANT
    private String etablissement;
    private Long equipeId;
    private String equipeNom;      // lecture seule, rempli par la jointure

    public String getStatutVise() { return statutVise; }
    public void setStatutVise(String statutVise) { this.statutVise = statutVise; }

    public String getEtablissement() { return etablissement; }
    public void setEtablissement(String etablissement) { this.etablissement = etablissement; }

    public Long getEquipeId() { return equipeId; }
    public void setEquipeId(Long equipeId) { this.equipeId = equipeId; }

    public String getEquipeNom() { return equipeNom; }
    public void setEquipeNom(String equipeNom) { this.equipeNom = equipeNom; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCvPath() { return cvPath; }
    public void setCvPath(String cvPath) { this.cvPath = cvPath; }

    public String getMotivation() { return motivation; }
    public void setMotivation(String motivation) { this.motivation = motivation; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public LocalDateTime getDateDemande() { return dateDemande; }
    public void setDateDemande(LocalDateTime dateDemande) { this.dateDemande = dateDemande; }
    public StatutDemande getStatutEnum() {
        return StatutDemande.valueOf(this.statut);
    }
    public void setStatutEnum(StatutDemande statut) {
        this.statut = statut.name();
    }
}