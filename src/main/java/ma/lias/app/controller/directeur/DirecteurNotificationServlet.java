package ma.lias.app.controller.directeur; 

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ma.lias.app.model.Utilisateur;
import ma.lias.app.service.NotificationService;

@WebServlet("/directeur/notifications")
public class DirecteurNotificationServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final NotificationService service = new NotificationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Utilisateur user = (Utilisateur) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        req.setAttribute("notifications", service.findByUser(user.getId()));
        req.setAttribute("nbNonLues", service.countUnread(user.getId()));
        req.setAttribute("pageTitle", "Mes Notifications");
        req.setAttribute("contentPage", "/WEB-INF/views/directeur/notifications-content.jsp");

        req.getRequestDispatcher("/WEB-INF/views/directeur/layout.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws IOException {

        Utilisateur user = (Utilisateur) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String idParam = req.getParameter("id");
        if (idParam != null) {
            service.marquerLu(Long.parseLong(idParam), user.getId());
        }
        resp.sendRedirect(req.getContextPath() + "/directeur/notifications");
    }
}