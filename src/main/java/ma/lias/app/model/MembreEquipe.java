package ma.lias.app.model;

import java.time.LocalDate;

public class MembreEquipe {

    private Long id;
    private Long membreId;
    private Long equipeId;
    private LocalDate dateDebut;
    private LocalDate dateFin;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getMembreId() { return membreId; }
    public void setMembreId(Long membreId) { this.membreId = membreId; }

    public Long getEquipeId() { return equipeId; }
    public void setEquipeId(Long equipeId) { this.equipeId = equipeId; }

    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }

    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }
}