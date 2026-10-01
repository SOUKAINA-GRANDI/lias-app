package ma.lias.app.enums;

public enum TypeMateriel {

    INFORMATIQUE("Matériel informatique"),
    BUREAU("Mobilier de bureau"),
    LABORATOIRE("Équipement de laboratoire"),
    CONSOMMABLE("Consommable"),
    AUDIOVISUEL("Matériel audiovisuel"),
    RESEAU("Équipement réseau"),
    AUTRE("Autre");

    private final String libelle;

    TypeMateriel(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}