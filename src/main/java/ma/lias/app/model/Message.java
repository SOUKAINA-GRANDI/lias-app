package ma.lias.app.model;

import java.time.LocalDateTime;

public class Message {
    private Long id;
    private Long conversationId;
    private Long expediteurId;
    private String nomExpediteur; // ✅ AJOUTÉ POUR LE CHAT
    private String contenu;
    private LocalDateTime dateEnvoi;
    private boolean lu;

    // ✅ Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getConversationId() { return conversationId; }
    public void setConversationId(Long conversationId) { this.conversationId = conversationId; }

    public Long getExpediteurId() { return expediteurId; }
    public void setExpediteurId(Long expediteurId) { this.expediteurId = expediteurId; }

    public String getNomExpediteur() { return nomExpediteur; }
    public void setNomExpediteur(String nomExpediteur) { this.nomExpediteur = nomExpediteur; }

    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }

    public LocalDateTime getDateEnvoi() { return dateEnvoi; }
    public void setDateEnvoi(LocalDateTime dateEnvoi) { this.dateEnvoi = dateEnvoi; }

    public boolean isLu() { return lu; }
    public void setLu(boolean lu) { this.lu = lu; }
}