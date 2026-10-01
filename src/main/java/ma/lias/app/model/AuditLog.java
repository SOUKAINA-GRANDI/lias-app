package ma.lias.app.model;

import java.time.LocalDateTime;

public class AuditLog {

    private Long id;
    private Long utilisateurId;
    private String emailUtilisateur;   // ✅ NOUVEAU
    private String action;
    private String entityName;
    private Long entityId;
    private LocalDateTime dateAction;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUtilisateurId() { return utilisateurId; }
    public void setUtilisateurId(Long utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public String getEmailUtilisateur() {
        return emailUtilisateur;
    }

    public void setEmailUtilisateur(String emailUtilisateur) {
        this.emailUtilisateur = emailUtilisateur;
    }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getEntityName() { return entityName; }
    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Long getEntityId() { return entityId; }
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public LocalDateTime getDateAction() { return dateAction; }
    public void setDateAction(LocalDateTime dateAction) {
        this.dateAction = dateAction;
    }
}