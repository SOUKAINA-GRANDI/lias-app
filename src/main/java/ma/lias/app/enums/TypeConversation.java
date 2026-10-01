package ma.lias.app.enums;

public enum TypeConversation {

    PRIVE("Conversation privée"),
    EQUIPE("Discussion d'équipe"),
    EVENEMENT("Discussion liée à un événement");

    private final String libelle;

    TypeConversation(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}