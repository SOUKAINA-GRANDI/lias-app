package ma.lias.app.service;

import ma.lias.app.dao.AffiliationHistoriqueDAO;
import ma.lias.app.dao.MembreDAO;
import ma.lias.app.dao.MembreHistoriqueDAO;
import ma.lias.app.dao.UtilisateurDAO;
import ma.lias.app.enums.StatutMembre;
import ma.lias.app.exception.BusinessException;
import ma.lias.app.model.Membre;

import java.time.LocalDate;
import java.util.List;

public class MembreService {

    private final MembreDAO membreDAO = new MembreDAO();
    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
    
    private final AffiliationHistoriqueDAO affiliationDAO =
            new AffiliationHistoriqueDAO();
    private final MembreHistoriqueDAO membreHistoriqueDAO =
            new MembreHistoriqueDAO();

    // ✅ Créer membre
    public void create(Membre membre) {

        membre.setDateAffiliation(LocalDate.now());
        membre.setActif(true);

        membreDAO.save(membre);

        affiliationDAO.startAffiliation(membre.getId());
    }

    // ✅ Modifier membre avec historisation
    public void update(Membre m) {

        Membre ancien = membreDAO.findById(m.getId());

        if (ancien != null) {
            membreHistoriqueDAO.saveHistorique(ancien);
        }

        membreDAO.update(m);
    }

    // ✅ Désactiver membre
    public void deactivate(Long membreId) {

        Membre membre = membreDAO.findById(membreId);

        if (membre == null)
            throw new BusinessException("Membre introuvable");

        membreHistoriqueDAO.saveHistorique(membre);

        membreDAO.disable(membreId);

        if (membre.getUtilisateurId() != null) {
            utilisateurDAO.toggleActif(
                    membre.getUtilisateurId(),
                    false
            );
        }

        affiliationDAO.endAffiliation(
                membreId,
                "Départ du laboratoire");
    }

    // ✅ Réactiver membre avec un NOUVEAU statut actif
    public void reactivate(Long membreId, StatutMembre nouveauStatut) {
        if (nouveauStatut == null
                || nouveauStatut == StatutMembre.RETRAITE
                || nouveauStatut == StatutMembre.ANCIEN) {
            throw new BusinessException(
                    "Choisissez un statut actif (Permanent, Associé ou Doctorant) pour la réactivation.");
        }

        Membre membre = membreDAO.findById(membreId);
        if (membre == null)
            throw new BusinessException("Membre introuvable");

        membreHistoriqueDAO.saveHistorique(membre);

        membre.setStatut(nouveauStatut.name());
        membreDAO.update(membre);     // écrit le nouveau statut
        membreDAO.enable(membreId);   // écrit actif = true

        if (membre.getUtilisateurId() != null) {
            utilisateurDAO.toggleActif(membre.getUtilisateurId(), true);
        }
        affiliationDAO.startAffiliation(membreId);
    }

    // ✅ Changer statut avec Enum
    public void changeStatut(Long membreId,
                             StatutMembre nouveauStatut) {

        Membre membre = membreDAO.findById(membreId);

        if (membre == null)
            throw new BusinessException("Membre introuvable");

        // ✅ CDC §3 : trace l'ancien statut avant de l'écraser
        membreHistoriqueDAO.saveHistorique(membre);

        membre.setStatut(nouveauStatut.name());

        if (nouveauStatut == StatutMembre.RETRAITE
                || nouveauStatut == StatutMembre.ANCIEN) {

            membre.setActif(false);

            affiliationDAO.endAffiliation(
                    membreId,
                    "Changement statut : "
                            + nouveauStatut.name());

            if (membre.getUtilisateurId() != null) {
                utilisateurDAO.toggleActif(
                        membre.getUtilisateurId(),
                        false
                );
            }
        }

        membreDAO.update(membre);

        if (nouveauStatut == StatutMembre.RETRAITE
                || nouveauStatut == StatutMembre.ANCIEN) {
            membreDAO.disable(membreId);   // écrit actif = false
        }
    }

    public Membre findById(Long id) {
        return membreDAO.findById(id);
    }

    public Membre findByUserId(Long userId) {
        return membreDAO.findByUserId(userId);
    }

    public boolean isActif(Long membreId) {

        if (membreId == null)
            return false;

        Membre membre = membreDAO.findById(membreId);

        return membre != null && membre.isActif();
    }

    public List<Membre> findAll() {
        return membreDAO.findAll();
    }
    /** Liste des membres avec le nom de leur équipe (jointure), pour l'annuaire interne. */
    public List<Membre> findAllAvecEquipe() {
        return membreDAO.findAllPublic();
    }

    /** Pagination (CDC : listes utilisables même avec beaucoup de membres). */
    public List<Membre> findPaginated(int page, int taillePage) {
        int p = Math.max(1, page);
        return membreDAO.findPaginated((p - 1) * taillePage, taillePage);
    }

    public int countAll() {
        return membreDAO.countAll();
    }

    public Membre findByUtilisateurId(Long utilisateurId) {
        return membreDAO.findByUtilisateurId(utilisateurId);
    }

    public java.util.List<ma.lias.app.model.MembreHistorique> findHistorique(Long membreId) {
        return membreHistoriqueDAO.findByMembreId(membreId);
    }
    public List<Membre> findPaginated(int page, int taillePage, String filtre) {
        int p = Math.max(1, page);
        return membreDAO.findPaginated((p - 1) * taillePage, taillePage, filtre);
    }

    public int count(String filtre) {
        return membreDAO.count(filtre);
    }
    public void toggleDroitPublication(Long id) {
        Membre m = membreDAO.findById(id);
        if (m == null) throw new BusinessException("Membre introuvable");
        membreDAO.updateDroitPublication(id, !m.isDroitPublication());
    }
}