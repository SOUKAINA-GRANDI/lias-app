package ma.lias.app.model;

import java.time.LocalDateTime;

public class Notification {
    private Long id;
    private Long utilisateurId;
    private String message;
    private String contenu; // 💡 Nouveau champ
    private String type;
    private boolean lu;
    private LocalDateTime dateCreation;

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUtilisateurId() { return utilisateurId; }
    public void setUtilisateurId(Long utilisateurId) { this.utilisateurId = utilisateurId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getContenu() { return contenu; } // 💡 Nouveau getter
    public void setContenu(String contenu) { this.contenu = contenu; } // 💡 Nouveau setter

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public boolean isLu() { return lu; }
    public void setLu(boolean lu) { this.lu = lu; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime localDateTime) { this.dateCreation = localDateTime; }
    public String getDateFormatee() {
        if (dateCreation == null) return "";
        return dateCreation.format(
                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy 'à' HH:mm"));
    }
}