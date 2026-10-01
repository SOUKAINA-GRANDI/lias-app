package ma.lias.app.service;

import ma.lias.app.dao.MembreDAO;
import ma.lias.app.dao.MembreHistoriqueDAO;
import ma.lias.app.dao.RoleDAO;
import ma.lias.app.enums.RoleType;
import ma.lias.app.exception.BusinessException;
import ma.lias.app.model.Membre;
import ma.lias.app.model.Role;

import java.time.LocalDate;
import java.util.List;

/**
 * CDC §7 - Gestion des rôles et responsabilités ⭐
 * Règles :
 *  - Un membre peut changer de rôle.
 *  - Aucun changement ne supprime les données (on clôture l'ancien rôle, on n'écrase rien).
 *  - Historique complet des rôles conservé (table `role`).
 */
public class RoleService {

    private final RoleDAO roleDAO = new RoleDAO();
    private final MembreDAO membreDAO = new MembreDAO();
    private final MembreHistoriqueDAO membreHistoriqueDAO = new MembreHistoriqueDAO();


    /**
     * Change le rôle d'un membre : clôture l'ancien rôle actif (date_fin renseignée,
     * jamais supprimé) puis ouvre une nouvelle période avec le nouveau rôle.
     * Met aussi à jour membre.role pour rester compatible avec le reste de l'appli
     * qui lit ce champ directement (affichage rapide, contrôle d'accès messagerie...).
     */
    public void changerRole(Long membreId, RoleType nouveauRole) {

        if (membreId == null || nouveauRole == null) {
            throw new BusinessException("Membre ou rôle manquant.");
        }

        Membre membre = membreDAO.findById(membreId);
        if (membre == null) {
            throw new BusinessException("Membre introuvable.");
        }

        Role actif = roleDAO.findActifByMembre(membreId);
        if (actif != null && nouveauRole.name().equals(actif.getNom())) {
            throw new BusinessException("Ce membre possède déjà le rôle " + nouveauRole.name() + ".");
        }

        // Règle de gouvernance : un seul DIRECTEUR actif à la fois
        if (nouveauRole == RoleType.DIRECTEUR) {
            Role directeurActif = roleDAO.findDirecteurActif();
            if (directeurActif != null && !directeurActif.getMembreId().equals(membreId)) {
                throw new BusinessException(
                        "Un autre membre (id=" + directeurActif.getMembreId() + ") est déjà Directeur actif.");
            }
        }

        LocalDate aujourdHui = LocalDate.now();

        // 1. Clôture l'ancien rôle actif (aucune suppression : juste date_fin renseignée)
        roleDAO.cloturerActif(membreId, aujourdHui);

        // 2. Ouvre la nouvelle période de rôle
        Role nouveau = new Role();
        nouveau.setMembreId(membreId);
        nouveau.setNomEnum(nouveauRole);
        nouveau.setDateDebut(aujourdHui);
        roleDAO.save(nouveau);

        // 3. Snapshot AVANT écrasement (CDC §3), puis synchronise le champ rapide
        //    membre.role (compatibilité affichage/contrôles existants)
        membreHistoriqueDAO.saveHistorique(membre);
        membre.setRole(nouveauRole.name());
        membreDAO.update(membre);
    }

    /**
     * Clôture simplement le rôle actif d'un membre sans en ouvrir un nouveau
     * précis (utilisé par ex. quand un mandat de direction se termine).
     * Le membre repasse par défaut sur MEMBRE_EQUIPE.
     */
    public void cloturerRoleActif(Long membreId) {
        try {
            changerRole(membreId, RoleType.MEMBRE_EQUIPE);
        } catch (BusinessException ignored) {
            // déjà MEMBRE_EQUIPE : rien à faire
        }
    }

    public Role findActif(Long membreId) {
        return roleDAO.findActifByMembre(membreId);
    }

    public List<Role> findHistorique(Long membreId) {
        return roleDAO.findHistoriqueByMembre(membreId);
    }

    public List<Role> findAll() {
        return roleDAO.findAllWithMembre();
    }
}