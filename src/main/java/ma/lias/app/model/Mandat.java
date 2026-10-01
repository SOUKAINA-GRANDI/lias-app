package ma.lias.app.model;

import java.time.LocalDate;
import java.util.List;

public class Mandat {

    private Long id;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Long directeurId;
    private boolean actif;
    // ── Champs d'affichage (calculés), non stockés ──
    private String directeurNom;
    private String directeurPrenom;
    private List<Role> viceDirecteurs = new java.util.ArrayList<>();
    private List<Role> chefsEquipe = new java.util.ArrayList<>();

    public String getDirecteurNom() { return directeurNom; }
    public void setDirecteurNom(String directeurNom) { this.directeurNom = directeurNom; }

    public String getDirecteurPrenom() { return directeurPrenom; }
    public void setDirecteurPrenom(String directeurPrenom) { this.directeurPrenom = directeurPrenom; }

    public List<Role> getViceDirecteurs() { return viceDirecteurs; }
    public void setViceDirecteurs(List<Role> viceDirecteurs) { this.viceDirecteurs = viceDirecteurs; }

    public List<Role> getChefsEquipe() { return chefsEquipe; }
    public void setChefsEquipe(List<Role> chefsEquipe) { this.chefsEquipe = chefsEquipe; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }

    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }

    public Long getDirecteurId() { return directeurId; }
    public void setDirecteurId(Long directeurId) { this.directeurId = directeurId; }

    public boolean isActif() { return actif; }
    public void setActif(boolean actif) { this.actif = actif; }
}