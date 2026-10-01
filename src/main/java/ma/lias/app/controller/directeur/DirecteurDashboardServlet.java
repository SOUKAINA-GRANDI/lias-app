package ma.lias.app.controller.directeur;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ma.lias.app.service.DemandeAdhesionService;
import ma.lias.app.service.DocumentService;
import ma.lias.app.service.EvenementService;
import ma.lias.app.service.MandatService;
import ma.lias.app.service.MembreService;
import ma.lias.app.service.PublicationService;

@WebServlet("/directeur/dashboard")
public class DirecteurDashboardServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DemandeAdhesionService demandeService = new DemandeAdhesionService();
    private MembreService membreService = new MembreService();
    private EvenementService evenementService = new EvenementService();
    private MandatService mandatService = new MandatService();
    private PublicationService publicationService = new PublicationService();
    private DocumentService documentService = new DocumentService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("nbDemandes",
                demandeService.findAllEnAttente().size());

        request.setAttribute("nbMembres",
                membreService.findAll().size());

        request.setAttribute("nbEvenements",
                evenementService.findAll().size());

        request.setAttribute("nbPublications", publicationService.countAll());

        request.setAttribute("mandatActif",
                mandatService.getMandatActif());

        java.util.List<ma.lias.app.model.Document> tousDocuments = documentService.findAll();
        request.setAttribute("nbDocuments", tousDocuments.size());
        request.setAttribute("documentsRecents",
                tousDocuments.stream().limit(5).toList());

        request.setAttribute("pageTitle", "Dashboard Directeur");
        request.setAttribute("contentPage",
                "/WEB-INF/views/directeur/dashboard.jsp");

        request.getRequestDispatcher(
                "/WEB-INF/views/directeur/layout.jsp")
                .forward(request, response);
    }
}