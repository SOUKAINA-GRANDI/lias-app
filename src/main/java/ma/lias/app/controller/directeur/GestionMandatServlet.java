package ma.lias.app.controller.directeur;

import java.io.IOException;
import java.time.LocalDate;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ma.lias.app.model.Mandat;
import ma.lias.app.service.MandatService;
import ma.lias.app.service.MembreService;

@WebServlet("/directeur/mandats")
public class GestionMandatServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final MandatService service =new MandatService();

    private final MembreService membreService = new MembreService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {

	    String action = request.getParameter("action");
	    if (action == null) {
	        action = "liste";
	    }

	    switch (action) {
	    case "nouveau":
	        request.setAttribute("membres", membreService.findAll()); // ← AJOUTER
	        request.setAttribute("contentPage", "/WEB-INF/views/directeur/mandat-form.jsp");
	        break;

	        case "liste":
	        default:
	            // Récupère l'historique et charge la page principale de listing des mandats
	            request.setAttribute("mandats", service.findAll());
	            request.setAttribute("contentPage", "/WEB-INF/views/directeur/mandats-content.jsp");
	            break;
	    }

	    // Redirection vers le layout principal qui englobe la page de contenu
	    request.getRequestDispatcher("/WEB-INF/views/directeur/layout.jsp")
	           .forward(request, response);
	}

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {

        String action =
                request.getParameter("action");

        try {

            if ("creer".equals(action)) {

                Mandat m = new Mandat();
                m.setDirecteurId(
                        Long.parseLong(
                                request.getParameter("directeurId")));

                m.setDateDebut(
                        LocalDate.parse(
                                request.getParameter("dateDebut")));

                String df =
                        request.getParameter("dateFin");

                if (df != null && !df.isBlank())
                    m.setDateFin(LocalDate.parse(df));

                service.create(m);
            }

            if ("cloturer".equals(action)) {
                service.cloturerActuel();
            }

        } catch (Exception e) {
            request.getSession()
                    .setAttribute("error",
                            e.getMessage());
        }

        response.sendRedirect(
                request.getContextPath()
                        + "/directeur/mandats");
    }
}