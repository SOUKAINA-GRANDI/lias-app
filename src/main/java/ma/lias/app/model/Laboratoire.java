package ma.lias.app.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * CDC §4.1 - Informations du laboratoire (nom, date de création...).
 * Table `laboratoire` : singleton (une seule ligne en pratique).
 */
public class Laboratoire {

    private Long id;
    private String nom;
    private String description;
    private LocalDate dateCreation;
    private String adresse;
    private String emailContact;
    private String telephone;
    private String siteWeb;
    private LocalDateTime dateModification;

    public Laboratoire() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDate dateCreation) { this.dateCreation = dateCreation; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public String getEmailContact() { return emailContact; }
    public void setEmailContact(String emailContact) { this.emailContact = emailContact; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getSiteWeb() { return siteWeb; }
    public void setSiteWeb(String siteWeb) { this.siteWeb = siteWeb; }

    public LocalDateTime getDateModification() { return dateModification; }
    public void setDateModification(LocalDateTime dateModification) { this.dateModification = dateModification; }
}
