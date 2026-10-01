package ma.lias.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ma.lias.app.dao.MandatDAO;
import ma.lias.app.dao.MembreDAO;
import ma.lias.app.enums.RoleType;
import ma.lias.app.exception.BusinessException;
import ma.lias.app.model.Mandat;
import ma.lias.app.model.Membre;
import ma.lias.app.dao.RoleDAO;

import java.time.LocalDate;
import java.util.List;

public class MandatService {

    private static final Logger logger = LoggerFactory.getLogger(MandatService.class);


    private final MandatDAO dao = new MandatDAO();
    private final MembreDAO membreDAO = new MembreDAO();
    private final RoleService roleService = new RoleService();
    private final RoleDAO roleDAO = new RoleDAO();
    // ✅ Historique
    // ✅ Historique, enrichi avec l'équipe de direction de chaque mandat
    public List<Mandat> findAll() {

        List<Mandat> mandats = dao.findAll();

        for (Mandat m : mandats) {
            List<ma.lias.app.model.Role> gouvernance =
                    roleDAO.findGouvernanceParPeriode(m.getDateDebut(), m.getDateFin());

            m.setViceDirecteurs(gouvernance.stream()
                    .filter(r -> "VICE_DIRECTEUR".equals(r.getNom()))
                    .toList());

            m.setChefsEquipe(gouvernance.stream()
                    .filter(r -> "CHEF_EQUIPE".equals(r.getNom()))
                    .toList());
        }

        return mandats;
    }

    // ✅ Actif
    public Mandat getMandatActif() {
        return dao.findActif();
    }

    // ✅ Créer mandat
    public void create(Mandat m) {

        if (m.getDateFin() != null &&
                m.getDateDebut().isAfter(m.getDateFin()))
            throw new BusinessException("Dates invalides");

        Mandat actif = dao.findActif();

        if (actif != null)
            throw new BusinessException("Un mandat actif existe déjà");

        Membre directeur = membreDAO.findById(m.getDirecteurId());
        if (directeur == null)
            throw new BusinessException("Le membre désigné comme directeur est introuvable.");
        if (!directeur.isActif())
            throw new BusinessException("Le membre désigné comme directeur n'est pas actif.");

        dao.save(m);

        // CDC §5.3/§7 : le mandat de direction doit se refléter dans l'historique des rôles
        try {
            roleService.changerRole(m.getDirecteurId(), RoleType.DIRECTEUR);
        } catch (BusinessException e) {
            // Cas rare (course concurrente) : le mandat est créé, à corriger manuellement via /directeur/roles
            logger.error("Erreur technique", e);
        }
    }

    // ✅ Clôturer mandat actuel
    public void cloturerActuel() {

        Mandat actif = dao.findActif();

        if (actif == null)
            throw new BusinessException("Aucun mandat actif");

        dao.cloturer(actif.getId(),
                LocalDate.now());

        // Le rôle DIRECTEUR de l'ex-directeur est clôturé (il redevient MEMBRE_EQUIPE par défaut)
        roleService.cloturerRoleActif(actif.getDirecteurId());
    }
}