package ma.lias.app.enums;

public enum TypePublication {

    ARTICLE_REVUE("Article de revue"),
    CONFERENCE("Article de conférence"),
    CHAPITRE_LIVRE("Chapitre de livre"),
    LIVRE("Livre"),
    THESE("Thèse"),
    RAPPORT_TECHNIQUE("Rapport technique"),
    BREVET("Brevet");

    private final String libelle;

    TypePublication(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}