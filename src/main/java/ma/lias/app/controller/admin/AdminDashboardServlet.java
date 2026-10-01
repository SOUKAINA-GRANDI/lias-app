package ma.lias.app.controller.admin;

import ma.lias.app.dao.MembreDAO;
import ma.lias.app.dao.PublicationDAO;
import ma.lias.app.dao.EvenementDAO;
import ma.lias.app.dao.ConventionDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private MembreDAO membreDAO = new MembreDAO();
    private PublicationDAO publicationDAO = new PublicationDAO();
    private EvenementDAO evenementDAO = new EvenementDAO();
    private ConventionDAO conventionDAO = new ConventionDAO();

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("pageTitle", "Vue d'ensemble");

        request.setAttribute("contentPage",
                "/WEB-INF/views/admin/dashboard.jsp");

        request.setAttribute("totalMembres", membreDAO.countAll());
        request.setAttribute("totalPublications", publicationDAO.countAll());
        request.setAttribute("totalEvenements", evenementDAO.countAll());
        request.setAttribute("totalConventions", conventionDAO.countAll());

        request.getRequestDispatcher("/WEB-INF/views/admin/layout.jsp")
               .forward(request, response);
    }
}