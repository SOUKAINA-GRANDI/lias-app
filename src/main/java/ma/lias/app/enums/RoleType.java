package ma.lias.app.enums;

/**
 * Rôles de gouvernance d'un membre (CDC §7 - Gestion des rôles et responsabilités).
 * Correspond exactement à l'enum de la colonne `role.nom` en base.
 */
public enum RoleType {
    DIRECTEUR,
    VICE_DIRECTEUR,
    CHEF_EQUIPE,
    MEMBRE_EQUIPE
}
