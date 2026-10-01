package ma.lias.app.enums;

public enum StatutMembre {

    PERMANENT("Membre Permanent"),
    ASSOCIE("Membre Associé"),
    DOCTORANT("Doctorant"),
    RETRAITE("Retraité"),
    ANCIEN("Ancien Membre");

    private final String libelle;

    StatutMembre(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}