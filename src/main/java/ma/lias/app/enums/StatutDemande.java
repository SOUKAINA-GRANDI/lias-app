package ma.lias.app.enums;

public enum StatutDemande {

    EN_ATTENTE("En attente de traitement"),
    VALIDEE("Acceptée"),
    REFUSEE("Refusée");

    private final String libelle;

    StatutDemande(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}