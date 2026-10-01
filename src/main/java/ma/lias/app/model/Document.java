package ma.lias.app.model;

import java.time.LocalDateTime;

/**
 * CDC §10 - Archivage documentaire ⭐
 * Correspond exactement à la table `document` :
 * (id, titre, type, chemin_fichier, evenement_id, date_upload, archive)
 */
public class Document {

    private Long id;
    private String titre;
    private String type;          // FINANCEMENT, PROGRAMME, ATTESTATION, RAPPORT, ADMIN
    private String cheminFichier; // ex: uploads/documents/xxx.pdf (servi par FileServlet "/uploads/*")
    private Long evenementId;     // optionnel : document lié à un événement
    private LocalDateTime dateUpload;
    private boolean archive;
    private int version = 1;
    private Long documentParentId;      // null pour la toute première version
    private boolean versionCourante = true;

    public int getVersion() { return version; }
    public void setVersion(int version) { this.version = version; }

    public Long getDocumentParentId() { return documentParentId; }
    public void setDocumentParentId(Long documentParentId) { this.documentParentId = documentParentId; }

    public boolean isVersionCourante() { return versionCourante; }
    public void setVersionCourante(boolean versionCourante) { this.versionCourante = versionCourante; }

    // ── Champ d'affichage (jointure), non stocké ────────────
    private String evenementTitre;

    public Document() {}

    /** Extension du fichier, déduite du chemin (pas de colonne dédiée). */
    public String getExtension() {
        if (cheminFichier == null || !cheminFichier.contains(".")) return "";
        return cheminFichier.substring(cheminFichier.lastIndexOf(".") + 1).toLowerCase();
    }

    /** Icône selon l'extension, pour l'affichage. */
    public String getIcone() {
        switch (getExtension()) {
            case "pdf": return "📕";
            case "doc": case "docx": return "📘";
            case "xls": case "xlsx": return "📗";
            case "ppt": case "pptx": return "📙";
            case "zip": case "rar": return "🗜️";
            default: return "📄";
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getCheminFichier() { return cheminFichier; }
    public void setCheminFichier(String cheminFichier) { this.cheminFichier = cheminFichier; }

    public Long getEvenementId() { return evenementId; }
    public void setEvenementId(Long evenementId) { this.evenementId = evenementId; }

    public LocalDateTime getDateUpload() { return dateUpload; }
    public void setDateUpload(LocalDateTime dateUpload) { this.dateUpload = dateUpload; }

    /** Date lisible pour l'affichage (ex: 15/07/2026) au lieu de l'ISO brut. */
    public String getDateFormatee() {
        if (dateUpload == null) return "";
        return dateUpload.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public boolean isArchive() { return archive; }
    public void setArchive(boolean archive) { this.archive = archive; }

    public String getEvenementTitre() { return evenementTitre; }
    public void setEvenementTitre(String evenementTitre) { this.evenementTitre = evenementTitre; }
}