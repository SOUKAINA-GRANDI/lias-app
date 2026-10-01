package ma.lias.app.controller.membre;

import ma.lias.app.model.Utilisateur;
import ma.lias.app.service.NotificationService;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/membre/notifications/read")
public class MarkNotificationServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final NotificationService service =
            new NotificationService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);

        if (session == null ||
            session.getAttribute("user") == null) {

            response.sendRedirect(
                    request.getContextPath() + "/login");
            return;
        }

        Utilisateur user =
                (Utilisateur) session.getAttribute("user");

        String idParam = request.getParameter("id");

        if (idParam == null) {
            response.sendRedirect(
                    request.getContextPath()
                    + "/membre/notifications");
            return;
        }

        Long id;

        try {
            id = Long.parseLong(idParam);
        } catch (NumberFormatException e) {
            response.sendRedirect(
                    request.getContextPath()
                    + "/membre/notifications");
            return;
        }

        // ✅ Sécuriser : marquer seulement si notification appartient à l'utilisateur
        service.marquerLu(id, user.getId());

        response.sendRedirect(
                request.getContextPath()
                + "/membre/notifications");
    }
}