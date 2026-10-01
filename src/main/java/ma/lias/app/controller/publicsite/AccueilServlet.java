package ma.lias.app.controller.publicsite;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ma.lias.app.dao.ConventionDAO;
import ma.lias.app.dao.EquipeDAO;
import ma.lias.app.dao.EvenementDAO;
import ma.lias.app.dao.MembreDAO;
import ma.lias.app.dao.PublicationDAO;

@WebServlet("/public/accueil")
public class AccueilServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MembreDAO membreDAO = new MembreDAO();
    private PublicationDAO publicationDAO = new PublicationDAO();
    private EvenementDAO evenementDAO = new EvenementDAO();
    private ConventionDAO conventionDAO = new ConventionDAO();
    private EquipeDAO equipeDAO = new EquipeDAO();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("pageTitle", "Accueil");

        // ✅ Statistiques réelles
        request.setAttribute("nbMembres",
                membreDAO.countAll());

        request.setAttribute("nbPublications",
                publicationDAO.countAll());

        request.setAttribute("nbEvenements",
                evenementDAO.countAll());

        request.setAttribute("nbConventions",
                conventionDAO.countAll());

        // ✅ Équipes réelles
        request.setAttribute("equipes",
                equipeDAO.findAll());

        request.setAttribute("contentPage",
                "/WEB-INF/views/public/accueil.jsp");

        request.getRequestDispatcher(
                "/WEB-INF/views/public/layout.jsp")
                .forward(request, response);
       }
}