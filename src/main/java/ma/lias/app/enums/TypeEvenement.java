package ma.lias.app.enums;

public enum TypeEvenement {

    CONFERENCE("Conférence"),
    SEMINAIRE("Séminaire"),
    WORKSHOP("Workshop"),
    JOURNEE_ETUDE("Journée d'étude"),
    SOUTENANCE("Soutenance de thèse"),
    REUNION("Réunion interne");

    private final String libelle;

    TypeEvenement(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}