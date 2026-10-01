package ma.lias.app.controller.membre;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import ma.lias.app.model.Utilisateur;
import ma.lias.app.model.Membre;
import ma.lias.app.service.DocumentService;
import ma.lias.app.service.MembreService;
import ma.lias.app.service.PublicationService;
import ma.lias.app.service.EvenementService;
import ma.lias.app.service.NotificationService;

import java.io.IOException;

@WebServlet("/membre/dashboard")
public class MembreDashboardServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final MembreService membreService = new MembreService();
    private final PublicationService publicationService = new PublicationService();
    private final EvenementService evenementService = new EvenementService();
    private final NotificationService notificationService = new NotificationService();
    private final DocumentService documentService = new DocumentService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null ||
            session.getAttribute("user") == null) {

            response.sendRedirect(
                    request.getContextPath() + "/login");
            return;
        }

        Utilisateur user =
                (Utilisateur) session.getAttribute("user");

        Membre membre =
                membreService.findByUserId(user.getId());

        if (membre == null) {
            response.sendRedirect(
                    request.getContextPath() + "/login");
            return;
        }

        // ✅ Statistiques simples
        request.setAttribute("nbPublications",
                publicationService.findByMembre(membre.getId()).size());

        request.setAttribute("nbEvenements",
                evenementService.findAll().size());

        request.setAttribute("nbMessagesNonLus",
                notificationService.countUnread(user.getId()));

        java.util.List<ma.lias.app.model.Document> tousDocuments = documentService.findAll();
        request.setAttribute("documentsRecents",
                tousDocuments.stream().limit(5).toList());

        request.setAttribute("pageTitle",
                "Tableau de bord");

        // ✅ BON fichier
        request.setAttribute("contentPage",
                "/WEB-INF/views/membre/dashboard.jsp");

        // ✅ BON layout
        request.getRequestDispatcher(
                "/WEB-INF/views/membre/layout.jsp")
               .forward(request, response);
    }
}