package ma.lias.app.model;

import java.time.LocalDateTime;

public class MessageEquipe {

    private Long id;
    private Long equipeId;
    private Long expediteurId;
    private String contenu;
    private LocalDateTime dateEnvoi;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEquipeId() { return equipeId; }
    public void setEquipeId(Long equipeId) { this.equipeId = equipeId; }

    public Long getExpediteurId() { return expediteurId; }
    public void setExpediteurId(Long expediteurId) { this.expediteurId = expediteurId; }

    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }

    public LocalDateTime getDateEnvoi() { return dateEnvoi; }
    public void setDateEnvoi(LocalDateTime dateEnvoi) { this.dateEnvoi = dateEnvoi; }
}