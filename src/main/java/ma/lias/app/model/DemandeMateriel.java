package ma.lias.app.model;

import java.time.LocalDateTime;

public class DemandeMateriel {

    private Long id;
    private Long membreId;
    private String description;
    private String statut;
    private LocalDateTime dateDemande;

    // ✅ Getters & Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMembreId() {
        return membreId;
    }

    public void setMembreId(Long membreId) {
        this.membreId = membreId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public LocalDateTime getDateDemande() {
        return dateDemande;
    }

    public void setDateDemande(LocalDateTime dateDemande) {
        this.dateDemande = dateDemande;
    }
    
 // À ajouter à l'intérieur de ta classe ma.lias.app.model.DemandeMateriel

    public String getDesignationExtraite() {
        if (this.description == null) return "N/A";
        for (String line : this.description.split("\n")) {
            if (line.startsWith("MATÉRIEL :")) return line.replace("MATÉRIEL :", "").trim();
        }
        return this.description; // Fallback si ancien format
    }

    public String getQuantiteExtraite() {
        if (this.description == null) return "1";
        for (String line : this.description.split("\n")) {
            if (line.startsWith("QUANTITÉ :")) return line.replace("QUANTITÉ :", "").trim();
        }
        return "1";
    }

    public String getUrgenceExtraite() {
        if (this.description == null) return "Normale";
        for (String line : this.description.split("\n")) {
            if (line.startsWith("URGENCE :")) return line.replace("URGENCE :", "").trim();
        }
        return "Normale";
    }

    public String getJustificationExtraite() {
        if (this.description == null) return "";
        StringBuilder justification = new StringBuilder();
        boolean found = false;
        for (String line : this.description.split("\n")) {
            if (line.startsWith("JUSTIFICATION :")) {
                justification.append(line.replace("JUSTIFICATION :", "").trim());
                found = true;
                continue;
            }
            if (found) {
                justification.append("\n").append(line);
            }
        }
        return found ? justification.toString() : this.description;
    }
}