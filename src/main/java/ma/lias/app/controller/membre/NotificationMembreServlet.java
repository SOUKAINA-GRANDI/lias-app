package ma.lias.app.controller.membre;

import ma.lias.app.model.Utilisateur;
import ma.lias.app.service.NotificationService;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/membre/notifications")
public class NotificationMembreServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final NotificationService service = new NotificationService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Utilisateur user = (Utilisateur) session.getAttribute("user");

        request.setAttribute("notifications", service.findByUser(user.getId()));
        request.setAttribute("nbNonLues", service.countUnread(user.getId()));
        request.setAttribute("pageTitle", "Mes Notifications");
        request.setAttribute("contentPage", "/WEB-INF/views/membre/notifications-content.jsp");

        request.getRequestDispatcher("/WEB-INF/views/membre/layout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Utilisateur user = (Utilisateur) session.getAttribute("user");
        String idParam = request.getParameter("id");

        if (idParam != null) {
            service.marquerLu(Long.parseLong(idParam), user.getId());
        }

        // Redirection vers la méthode doGet de l'espace membre pour rafraîchir la liste
        response.sendRedirect(request.getContextPath() + "/membre/notifications");
    }
}