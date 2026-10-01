package ma.lias.app.model;

import java.time.LocalDate;

public class AffiliationHistorique {

    private Long id;
    private Long membreId;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String motifDepart;
    private String membreNom;

    public String getMembreNom() { return membreNom; }
    public void setMembreNom(String membreNom) { this.membreNom = membreNom; }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getMembreId() { return membreId; }
    public void setMembreId(Long membreId) { this.membreId = membreId; }

    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }

    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }

    public String getMotifDepart() { return motifDepart; }
    public void setMotifDepart(String motifDepart) { this.motifDepart = motifDepart; }
}