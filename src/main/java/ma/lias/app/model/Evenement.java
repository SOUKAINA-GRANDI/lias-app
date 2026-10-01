package ma.lias.app.model;

import java.time.LocalDateTime;

public class Evenement {

    private Long id;
    private String titre;
    private String description;
    private String type;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private String lieu;
    private LocalDateTime dateCreation;
    private boolean archive;
    private boolean ouvertAuxAssocies = true;

    public boolean isOuvertAuxAssocies() { return ouvertAuxAssocies; }
    public void setOuvertAuxAssocies(boolean ouvertAuxAssocies) { this.ouvertAuxAssocies = ouvertAuxAssocies; }
    
    public boolean isArchive() {
        return archive;
    }

    public void setArchive(boolean archive) {
        this.archive = archive;
    }

    public Evenement() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public LocalDateTime getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDateTime dateDebut) { this.dateDebut = dateDebut; }

    public LocalDateTime getDateFin() { return dateFin; }
    public void setDateFin(LocalDateTime dateFin) { this.dateFin = dateFin; }

    public String getLieu() { return lieu; }
    public void setLieu(String lieu) { this.lieu = lieu; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }

    /** Jour du mois pour la "date box" (ex: "15"). */
    public String getJour() {
        return dateDebut != null
                ? String.format("%02d", dateDebut.getDayOfMonth())
                : "--";
    }

    /** Mois abrégé en français pour la "date box" (ex: "JUIL"). */
    public String getMoisAbrege() {
        if (dateDebut == null) return "";
        String[] mois = {"JANV","FÉVR","MARS","AVR","MAI","JUIN","JUIL","AOÛT","SEPT","OCT","NOV","DÉC"};
        return mois[dateDebut.getMonthValue() - 1];
    }

    /** Date lisible complète (ex: "15 juillet 2026 à 14:00"). */
    public String getDateLisible() {
        if (dateDebut == null) return "Date à confirmer";
        return dateDebut.format(
                java.time.format.DateTimeFormatter.ofPattern("d MMMM yyyy 'à' HH:mm",
                        java.util.Locale.FRENCH));
    }

    /** true si l'événement est déjà passé (par rapport à maintenant). */
    public boolean isPasse() {
        return dateDebut != null && dateDebut.isBefore(LocalDateTime.now());
    }
}