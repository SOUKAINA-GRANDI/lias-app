package ma.lias.app.service;

import ma.lias.app.dao.EvenementDAO;
import ma.lias.app.dao.ParticipationEvenementDAO;
import ma.lias.app.exception.BusinessException;
import ma.lias.app.model.Evenement;

import java.util.List;
import ma.lias.app.dao.EvenementOrganisateurDAO;
import ma.lias.app.model.Membre;

import java.util.Map;
import java.util.Set;
public class EvenementService {

    private final EvenementDAO dao = new EvenementDAO();
    private final ParticipationEvenementDAO participationDAO = new ParticipationEvenementDAO();
    private final MembreService membreService = new MembreService();
    private final EvenementOrganisateurDAO organisateurDAO = new EvenementOrganisateurDAO();
    private final NotificationService notificationService = new NotificationService();
    
    
    // ===================== ORGANISATEURS =====================

    public void create(Evenement e, List<Long> organisateurIds) {
        verifierOrganisateurs(organisateurIds);   // avant de créer : rien n'est écrit si invalide
        create(e);
        if (e.getId() == null) {
            throw new BusinessException("Échec de l'enregistrement de l'événement");
        }
        affecterOrganisateurs(e, organisateurIds);
    }

    public void update(Evenement e, List<Long> organisateurIds) {
        verifierOrganisateurs(organisateurIds);
        update(e);
        affecterOrganisateurs(e, organisateurIds);
    }

    private void verifierOrganisateurs(List<Long> ids) {
        for (Long id : ids) {
            Membre m = membreService.findById(id);
            if (m == null || !m.isActif() || !"PERMANENT".equals(m.getStatut())) {
                throw new BusinessException(
                        "Un organisateur doit être un membre permanent actif.");
            }
        }
    }

    private void affecterOrganisateurs(Evenement e, List<Long> ids) {

        Set<Long> avant = organisateurDAO.findIdsByEvenement(e.getId());
        organisateurDAO.remplacer(e.getId(), ids);

        // Notification uniquement pour les NOUVEAUX organisateurs
        for (Long id : ids) {
            if (avant.contains(id)) continue;

            Membre m = membreService.findById(id);
            if (m != null && m.getUtilisateurId() != null) {
                notificationService.notifyUser(m.getUtilisateurId(),
                        "Vous avez été désigné(e) organisateur de l'événement « "
                                + e.getTitre() + " ».");
            }
        }
    }

    public Set<Long> findOrganisateurIds(Long evenementId) {
        return organisateurDAO.findIdsByEvenement(evenementId);
    }

    public String findOrganisateursTexte(Long evenementId) {
        return organisateurDAO.findNomsByEvenement(evenementId);
    }

    public Map<Long, String> findOrganisateursParEvenement() {
        return organisateurDAO.findNomsParEvenement();
    }

    public void participer(Long membreId, Long evenementId) {

        if (!membreService.isActif(membreId)) {
            throw new BusinessException("Compte inactif");
        }

        Evenement e = dao.findById(evenementId);

        if (e == null)
            throw new BusinessException("Événement introuvable");

        if (e.isArchive())
            throw new BusinessException("Événement archivé");

        if (participationDAO.isInscritActif(membreId, evenementId))
            throw new BusinessException("Déjà inscrit");

        participationDAO.participer(membreId, evenementId);
    }

    public void annulerParticipation(Long membreId, Long evenementId) {

        participationDAO.annulerParticipation(membreId, evenementId);
    }

    public void create(Evenement e) {

    	if (e.getDateFin() != null &&
    		    e.getDateDebut().isAfter(e.getDateFin())) {
    		    throw new BusinessException("Dates invalides");
    		}
        dao.save(e);
        notificationService.notifierStatuts(
                java.util.List.of("PERMANENT", "ASSOCIE", "DOCTORANT"),
                "Nouvel événement : « " + e.getTitre() + " » le " + e.getDateDebut().toLocalDate() + ".");
    }

    public void update(Evenement e) {

        if (e.getId() == null) {
            throw new BusinessException("ID événement obligatoire");
        }

        if (e.getDateDebut() == null) {
            throw new BusinessException("Date début obligatoire");
        }

        if (e.getDateFin() != null && e.getDateDebut().isAfter(e.getDateFin())) {
            throw new BusinessException("La date de début doit être avant la date de fin");
        }

        dao.update(e);
    }
    public Evenement findById(Long id) {

        if (id == null) {
            return null;
        }

        return dao.findById(id);
    }


    public void archive(Long id) {
        dao.archive(id);
    }

    public List<Evenement> findAll() {
        return dao.findAll();
    }

    public List<Evenement> findByYear(int year) {
        return dao.findByYear(year);
    }
}