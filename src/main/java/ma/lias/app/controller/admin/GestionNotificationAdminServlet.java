package ma.lias.app.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import ma.lias.app.service.NotificationService;
import ma.lias.app.service.UtilisateurService; // ✅ Ajouté pour l'envoi ciblé

import java.io.IOException;

@WebServlet("/admin/notifications")
public class GestionNotificationAdminServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final NotificationService service = new NotificationService();
    private final UtilisateurService utilisateurService = new UtilisateurService(); // ✅ Pour récupérer la liste des utilisateurs

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Charger l'historique des notifications et les utilisateurs pour le formulaire
        request.setAttribute("notifications", service.findAll());
        request.setAttribute("listeUtilisateurs", utilisateurService.findAll()); // ✅ Transmis au JSP

        // 2. Configuration et routage via le Layout Global
        request.setAttribute("pageTitle", "Centre de Notifications & Alertes");
        request.setAttribute("activeMenu", "notifications");
        request.setAttribute("contentPage", "/WEB-INF/views/admin/notifications.jsp");

        request.getRequestDispatcher("/WEB-INF/views/admin/layout.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String message = request.getParameter("message");

        if (message != null && !message.isBlank()) {
            if ("global".equals(action)) {
                service.envoyerGlobal(message.trim());
            } else if ("individuel".equals(action)) {
                String userIdParam = request.getParameter("userId");
                if (userIdParam != null) {
                    Long userId = Long.parseLong(userIdParam);
                    service.envoyerUtilisateur(userId, message.trim());
                }
            }
        }

        response.sendRedirect(request.getContextPath() + "/admin/notifications");
    }
}