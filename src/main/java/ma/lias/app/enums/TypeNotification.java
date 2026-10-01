package ma.lias.app.enums;

public enum TypeNotification {

    ADHESION_VALIDEE("Adhésion acceptée"),
    ADHESION_REFUSEE("Adhésion refusée"),
    NOUVELLE_DEMANDE("Nouvelle demande d'adhésion"),
    DOCUMENT_AJOUTE("Nouveau document ajouté"),
    EVENEMENT_CREE("Nouvel événement créé"),
    MATERIEL_ATTRIBUE("Matériel attribué"),
    REUNION_PLANIFIEE("Réunion planifiée"),
    PV_DISPONIBLE("Procès-verbal disponible"),
    MESSAGE_RECU("Nouveau message reçu");

    private final String libelle;

    TypeNotification(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}