package ma.lias.app.service;

import ma.lias.app.dao.DemandeMaterielDAO;
import ma.lias.app.enums.StatutDemande;
import ma.lias.app.exception.BusinessException;
import ma.lias.app.model.DemandeMateriel;
import ma.lias.app.dao.MembreDAO;
import ma.lias.app.model.DemandeMateriel;

import java.util.List;

public class DemandeMaterielService {

    private final DemandeMaterielDAO demandeDAO =new DemandeMaterielDAO();
    private final NotificationService notificationService = new NotificationService();
    private final MembreDAO membreDAO = new MembreDAO();
    

    public List<DemandeMateriel> findAllEnAttente() {
        return demandeDAO.findAllEnAttente();
    }

    public void updateStatut(Long id, StatutDemande statut) {

        if (statut == null)
            throw new BusinessException("Statut invalide");

        DemandeMateriel demande = demandeDAO.findById(id);
        demandeDAO.updateStatut(id, statut.name());

        if (demande != null) {
            var membre = membreDAO.findById(demande.getMembreId());
            if (membre != null && membre.getUtilisateurId() != null) {
                String message = (statut == StatutDemande.VALIDEE)
                        ? "Votre demande de matériel « " + demande.getDescription() + " » a été validée."
                        : "Votre demande de matériel « " + demande.getDescription() + " » a été refusée.";
                notificationService.notifyUser(membre.getUtilisateurId(), message);
            }
        }
    }

    public void soumettre(Long membreId,
                          String description) {

        if (description == null ||
            description.isBlank()) {

            throw new BusinessException(
                    "Description obligatoire");
        }

        demandeDAO.create(membreId, description);
    }
}