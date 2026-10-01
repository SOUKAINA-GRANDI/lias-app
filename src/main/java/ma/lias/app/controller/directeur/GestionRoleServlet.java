package ma.lias.app.controller.directeur;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ma.lias.app.enums.RoleType;
import ma.lias.app.service.MembreService;
import ma.lias.app.service.RoleService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * CDC §7 - Gestion des rôles et responsabilités ⭐
 * Permet au directeur en mandat de changer le rôle d'un membre
 * (Directeur / Vice-directeur / Chef d'équipe / Membre d'équipe)
 * avec historisation complète (aucune perte de données).
 */
@WebServlet("/directeur/roles")
public class GestionRoleServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(GestionRoleServlet.class);


    private static final long serialVersionUID = 1L;

    private final RoleService roleService = new RoleService();
    private final MembreService membreService = new MembreService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            request.setAttribute("membres", membreService.findAll());
            request.setAttribute("historique", roleService.findAll());
            request.setAttribute("roleTypes", RoleType.values());

        } catch (Exception e) {
            // On évite un 500 muet : on log la vraie cause côté serveur
            // et on l'affiche à l'écran pour pouvoir la diagnostiquer.
            logger.error("Erreur technique", e);
            request.getSession().setAttribute("error",
                    "Impossible de charger la page rôles : " + e.getMessage());
            request.setAttribute("membres", java.util.Collections.emptyList());
            request.setAttribute("historique", java.util.Collections.emptyList());
            request.setAttribute("roleTypes", RoleType.values());
        }

        request.setAttribute("pageTitle", "Rôles & responsabilités");
        request.setAttribute("contentPage", "/WEB-INF/views/directeur/roles-content.jsp");

        request.getRequestDispatcher("/WEB-INF/views/directeur/layout.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String membreIdParam = request.getParameter("membreId");
        String nouveauRoleParam = request.getParameter("nouveauRole");

        try {
            if (membreIdParam == null || !membreIdParam.matches("\\d+")
                    || nouveauRoleParam == null || nouveauRoleParam.isBlank()) {
                throw new IllegalArgumentException("Membre ou rôle manquant.");
            }

            Long membreId = Long.parseLong(membreIdParam);
            RoleType nouveauRole = RoleType.valueOf(nouveauRoleParam);

            roleService.changerRole(membreId, nouveauRole);

            request.getSession().setAttribute("success", "Rôle mis à jour avec succès.");

        } catch (Exception e) {
            request.getSession().setAttribute("error", e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/directeur/roles");
    }
}
