package ma.lias.app.enums;

public enum TypeUtilisateur {

    ADMIN("Administrateur Système"),
    DIRECTEUR("Directeur du Laboratoire"),
    MEMBRE("Membre du Laboratoire");

    private final String libelle;

    TypeUtilisateur(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}