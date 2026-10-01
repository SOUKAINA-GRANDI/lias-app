package ma.lias.app.controller.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ma.lias.app.model.Utilisateur;
import ma.lias.app.service.AuditService;
import ma.lias.app.service.UtilisateurService;

@WebServlet("/admin/utilisateurs")
public class GestionUtilisateursServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(GestionUtilisateursServlet.class);


    private static final long serialVersionUID = 1L;
    private final UtilisateurService service = new UtilisateurService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String query = request.getParameter("q");
        List<Utilisateur> utilisateurs;

        if (query != null && !query.trim().isEmpty()) {
            utilisateurs = service.findByQuery(query);
        } else {
            utilisateurs = service.findAll();
        }

        request.setAttribute("utilisateurs", utilisateurs);

        request.setAttribute("pageTitle", "Gestion des Utilisateurs");
        request.setAttribute("activeMenu", "utilisateurs");
        request.setAttribute("contentPage", "/WEB-INF/views/admin/utilisateurs.jsp");

        request.getRequestDispatcher("/WEB-INF/views/admin/layout.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action"); 
        String idParam = request.getParameter("id");
        
        HttpSession session = request.getSession();
        Utilisateur adminConnecte = (Utilisateur) session.getAttribute("user");

        if (action != null && idParam != null && adminConnecte != null) {
            try {
                Long utilisateurIdCible = Long.parseLong(idParam);
                AuditService auditService = new AuditService();
                
                switch (action) {
                    case "activer":
                        // 1. Mise à jour réelle via le service (actif = true)
                        service.toggleActif(utilisateurIdCible, true);
                        // 2. Trace d'audit
                        auditService.log(adminConnecte, "activer", "Utilisateur", utilisateurIdCible);
                        break;
                        
                    case "desactiver":
                        // 1. Mise à jour réelle via le service (actif = false)
                        service.toggleActif(utilisateurIdCible, false);
                        // 2. Trace d'audit
                        auditService.log(adminConnecte, "desactiver", "Utilisateur", utilisateurIdCible);
                        break;
                        
                    case "resetPassword":
                        // 1. Réinitialisation + Envoi d'email automatique via le service
                        service.resetPassword(utilisateurIdCible);
                        // 2. Trace d'audit
                        auditService.log(adminConnecte, "resetPassword", "Utilisateur", utilisateurIdCible);
                        break;
                }
            } catch (Exception e) {
                logger.error("Erreur technique", e);
                session.setAttribute("error", e.getMessage());
            }
        }
        
        // Redirection pour rafraîchir la liste sans re-soumettre le formulaire
        response.sendRedirect(request.getContextPath() + "/admin/utilisateurs");
    }
}