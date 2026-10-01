package ma.lias.app.controller.membre;

import ma.lias.app.model.Membre;
import ma.lias.app.model.Utilisateur;
import ma.lias.app.service.MembreService;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/membre/profil")
public class ProfilServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final MembreService membreService = new MembreService();

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
                    request.getContextPath() + "/membre/dashboard");
            return;
        }

        request.setAttribute("membre", membre);
        request.setAttribute("pageTitle", "Mon Profil");
        request.setAttribute("contentPage",
                "/WEB-INF/views/membre/profil-content.jsp");

        request.getRequestDispatcher(
                "/WEB-INF/views/membre/layout.jsp")
               .forward(request, response);
    }
}