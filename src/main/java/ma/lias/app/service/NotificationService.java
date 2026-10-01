package ma.lias.app.service;

import ma.lias.app.dao.NotificationDAO;
import ma.lias.app.dao.UtilisateurDAO;
import ma.lias.app.model.Notification;
import ma.lias.app.model.Membre;
import java.util.Collection;

import java.util.List;

public class NotificationService {

    private final NotificationDAO dao = new NotificationDAO();
    private final MembreService membreService = new MembreService();
    private final UtilisateurDAO utilisateurDAO =new UtilisateurDAO();

    // ✅ Récupérer notifications d'un utilisateur
    public List<Notification> findByUser(Long userId) { return dao.findByUser(userId);}

    // ✅ Marquer une notification comme lue (sécurisé)
    public void marquerLu(Long notificationId,
                          Long userId) {

        dao.markAsReadSecure(notificationId, userId);
    }
    /** Notifie tous les membres actifs dont le statut figure dans la liste. */
    public void notifierStatuts(Collection<String> statuts, String message) {
        for (Membre m : membreService.findAll()) {
            if (m.getUtilisateurId() != null && statuts.contains(m.getStatut())) {
                dao.create(m.getUtilisateurId(), message);
            }
        }
    }

    // ✅ Compter notifications non lues
    public int countUnread(Long userId) {
        return dao.countUnread(userId);
    }
    public void notifyUser(Long userId, String message) {
        dao.create(userId, message);
    }
    public List<Notification> findAll() {
        return dao.findAll();
    }
    
    public void envoyerGlobal(String message) {

        List<Long> ids =
                utilisateurDAO.findAllIds();

        for (Long id : ids) {
            dao.create(id, message);
        }
    }

    public void envoyerUtilisateur(Long id,
                                   String message) {

        dao.create(id, message);
    }
    
}
