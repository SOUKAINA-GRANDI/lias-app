package ma.lias.app.service;

import ma.lias.app.dao.EquipeDAO;
import ma.lias.app.dao.MembreDAO;
import ma.lias.app.dao.MembreEquipeDAO;
import ma.lias.app.dao.MembreHistoriqueDAO;
import ma.lias.app.enums.RoleType;
import ma.lias.app.exception.BusinessException;
import ma.lias.app.model.Equipe;
import ma.lias.app.model.Membre;

import java.util.List;

public class EquipeService {

    private final EquipeDAO dao = new EquipeDAO();
    private final MembreEquipeDAO membreEquipeDAO = new MembreEquipeDAO();
    private final MembreDAO membreDAO = new MembreDAO();
    private final MembreHistoriqueDAO membreHistoriqueDAO = new MembreHistoriqueDAO();
    private final RoleService roleService = new RoleService();

    // ✅ Créer équipe
    public void create(Equipe e) {

        if (e.getNom() == null || e.getNom().isBlank())
            throw new BusinessException("Nom obligatoire");

        dao.save(e);
    }

    // ✅ Modifier équipe
    public void update(Equipe e) {

        if (e.getNom() == null || e.getNom().isBlank())
            throw new BusinessException("Nom obligatoire");

        dao.update(e);
    }

    // ✅ Archiver (soft delete recommandé)
    public void archive(Long id) {
        dao.archive(id);
    }

    // ✅ Liste
    public List<Equipe> findAll() {
        return dao.findAll();
    }

    public Equipe findById(Long id) {
        if (id == null) throw new BusinessException("ID invalide");
        return dao.findById(id);
    }

    /**
     * Affecte un membre à une équipe : historise le changement dans
     * membre_equipe (date_debut/date_fin) et synchronise le champ rapide
     * membre.equipe_id (déjà lu partout ailleurs dans l'appli).
     */
    public void assignerMembre(Long equipeId, Long membreId) {

        if (equipeId == null || membreId == null)
            throw new BusinessException("Équipe ou membre manquant");

        Equipe equipe = dao.findById(equipeId);
        if (equipe == null)
            throw new BusinessException("Équipe introuvable");

        Membre membre = membreDAO.findById(membreId);
        if (membre == null)
            throw new BusinessException("Membre introuvable");

        // Clôture toute affectation active précédente (le membre peut changer d'équipe,
        // mais on ne perd jamais l'historique - règle générale du CDC §21)
        membreEquipeDAO.cloturerActifDuMembre(membreId);

        // Snapshot AVANT écrasement de membre.equipe_id (CDC §3)
        membreHistoriqueDAO.saveHistorique(membre);

        // Ouvre la nouvelle période d'affectation
        membreEquipeDAO.assigner(membreId, equipeId);

        // Champ rapide utilisé partout ailleurs (chat équipe, matériel, dashboard...)
        dao.assignerMembre(equipeId, membreId);
    }


    /** Retire un membre de son équipe actuelle (clôture l'historique + vide equipe_id). */
    public void retirerMembre(Long equipeId, Long membreId) {

        if (equipeId == null || membreId == null)
            throw new BusinessException("Équipe ou membre manquant");

        membreEquipeDAO.retirer(membreId, equipeId);

        Membre membre = membreDAO.findById(membreId);
        if (membre != null) {
            membreHistoriqueDAO.saveHistorique(membre);
            membre.setEquipeId(null);
            membreDAO.update(membre);
        }
    }
    /**
     * Désigne le chef d'une équipe : met à jour equipe.chef_id ET historise
     * le rôle CHEF_EQUIPE du membre (CDC §7 - le rôle est distinct du statut,
     * et son changement doit être tracé).
     */
    public void assignerChef(Long equipeId, Long membreId) {

        Equipe equipe = dao.findById(equipeId);
        if (equipe == null)
            throw new BusinessException("Équipe introuvable");

        Membre membre = membreDAO.findById(membreId);
        if (membre == null)
            throw new BusinessException("Membre introuvable");

        equipe.setChefId(membreId);
        dao.update(equipe);

        roleService.changerRole(membreId, RoleType.CHEF_EQUIPE);

        // Le chef doit logiquement appartenir à l'équipe qu'il dirige
        assignerMembre(equipeId, membreId);
    }
    public int countMembres(Long equipeId) {
        return dao.countMembres(equipeId);
    }
}
