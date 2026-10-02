package ma.lias.app.service;

import ma.lias.app.dao.UtilisateurDAO;
import ma.lias.app.exception.BusinessException;
import ma.lias.app.model.Utilisateur;
import ma.lias.app.util.PasswordUtil;
import ma.lias.app.util.EmailUtil;
import ma.lias.app.dao.MembreDAO;
import ma.lias.app.model.Membre;
import ma.lias.app.service.MembreService;
import ma.lias.app.enums.StatutMembre;

import java.util.List;
import java.util.UUID;

public class UtilisateurService {

    private final UtilisateurDAO dao =new UtilisateurDAO();
    private final MembreDAO membreDAO = new MembreDAO();
    private final MembreService membreService = new MembreService();

    public List<Utilisateur> findAll() { return dao.findAll();}

    public void toggleActif(Long id, boolean actif) {

        Utilisateur u = dao.findById(id);

        if (u == null)
            throw new BusinessException("Utilisateur introuvable");

        dao.toggleActif(id, actif);

        Membre membre = membreDAO.findByUtilisateurId(id);

        if (membre != null) {

            if (!actif) {
                // Par défaut, une désactivation depuis l'admin pose le membre en "Ancien membre"
                // (sauf s'il était déjà Retraité, auquel cas on garde ce statut plus précis).
                if (!"RETRAITE".equals(membre.getStatut())) {
                    membreService.changeStatut(membre.getId(), StatutMembre.ANCIEN);
                } else {
                    membreDAO.disable(membre.getId());
                }

            } else {
                if ("RETRAITE".equals(membre.getStatut()) || "ANCIEN".equals(membre.getStatut())) {
                    dao.toggleActif(id, false);
                    throw new BusinessException(
                            "Ce membre est " + membre.getStatut().toLowerCase()
                            + ". Réactivez-le depuis la page Membres du directeur en choisissant un nouveau statut.");
                }
                membreDAO.enable(membre.getId());
            }
        }
    }

    public void resetPassword(Long id) {

        Utilisateur u = dao.findById(id);

        if (u == null)
            throw new BusinessException("Utilisateur introuvable");

        String newPassword =
                UUID.randomUUID()
                    .toString()
                    .replace("-", "")
                    .substring(0, 8);

        String hashed =
                PasswordUtil.hash(newPassword);

        dao.updatePassword(id, hashed);

        EmailUtil.sendHtmlEmail(
                u.getEmail(),
                "Réinitialisation mot de passe",
                ma.lias.app.util.EmailTemplateUtil.render(
                        "reset-password.html",
                        "#2563eb",
                        java.util.Map.of("nouveauMotDePasse", newPassword)
                )
        );
    }
    public List<Utilisateur> findByQuery(String query) {
        if (query == null || query.trim().isEmpty()) {
            return dao.findAll();
        }
        return dao.findByQuery(query.trim());
    }
}