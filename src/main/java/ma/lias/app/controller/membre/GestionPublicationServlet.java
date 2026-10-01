package ma.lias.app.controller.membre;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ma.lias.app.model.Publication;
import ma.lias.app.model.Utilisateur;
import ma.lias.app.service.MembreService;
import ma.lias.app.service.PublicationService;

@WebServlet("/membre/publications")
public class GestionPublicationServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final PublicationService service = new PublicationService();
    private final MembreService membreService = new MembreService();

	@Override
	protected void doGet(HttpServletRequest request,
	                     HttpServletResponse response)
	        throws ServletException, IOException {

	    Utilisateur user = (Utilisateur) request.getSession().getAttribute("user");
        Long membreId = membreService.findByUserId(user.getId()).getId();

	    String action = request.getParameter("action");
	    if (action == null) action = "liste";

	    if ("nouveau".equals(action)) {

	        // Pas de "publication" en attribut -> le formulaire s'affiche vide
	        request.setAttribute("pageTitle", "Nouvelle publication");
	        request.setAttribute("contentPage",
	                "/WEB-INF/views/membre/publication-form.jsp");

	    } else if ("edit".equals(action)) {

	        String idParam = request.getParameter("id");
	        if (idParam == null || !idParam.matches("\\d+")) {
	            response.sendRedirect(request.getContextPath() + "/membre/publications");
	            return;
	        }

	        Long id = Long.parseLong(idParam);
	        Publication publication = service.findById(id);

	        // ⚠️ Sécurité : un membre ne peut éditer que SES propres publications
	        if (publication == null || !publication.getMembreId().equals(membreId)) {
	            request.getSession().setAttribute("error",
	                    "Vous ne pouvez modifier que vos propres publications.");
	            response.sendRedirect(request.getContextPath() + "/membre/publications");
	            return;
	        }

	        request.setAttribute("publication", publication);
	        request.setAttribute("pageTitle", "Modifier la publication");
	        request.setAttribute("contentPage",
	                "/WEB-INF/views/membre/publication-form.jsp");

	    } else {
	        // ✅ 1. Tes publications personnelles (gérables)
	        request.setAttribute("mesPublications", service.findByMembre(membreId));

	        // ✅ 2. Toutes les publications du labo (lecture seule)
	        request.setAttribute("toutesPublications", service.findAll());

	        request.setAttribute("pageTitle", "Publications");
	        request.setAttribute("contentPage",
	                "/WEB-INF/views/membre/mes-publications.jsp");
	    }

	    request.getRequestDispatcher(
	            "/WEB-INF/views/membre/layout.jsp")
	            .forward(request, response);
	}

    @Override
    protected void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException {

                Utilisateur user = (Utilisateur) request.getSession().getAttribute("user");
                 ma.lias.app.model.Membre membre = membreService.findByUserId(user.getId());
                Long membreId = membre.getId();

                 String action = request.getParameter("action");

        try {

            if ("ajouter".equals(action)) {
                if ("ASSOCIE".equals(membre.getStatut()) && !membre.isDroitPublication()) {
                    throw new ma.lias.app.exception.BusinessException(
                            "Vous n'avez pas le droit d'ajouter des publications. Contactez la direction.");
                }

                Publication p = new Publication();
                p.setTitre(request.getParameter("titre"));
                p.setAuteurs(request.getParameter("auteurs"));
                p.setAnnee(Integer.parseInt(request.getParameter("annee")));
                p.setType(request.getParameter("type"));
                p.setDescription(request.getParameter("description"));
                p.setMembreId(membreId);

                service.create(p);
            }

            if ("modifier".equals(action)) {

                Publication p = new Publication();
                p.setId(Long.parseLong(request.getParameter("id")));
                p.setTitre(request.getParameter("titre"));
                p.setAuteurs(request.getParameter("auteurs"));
                p.setAnnee(Integer.parseInt(request.getParameter("annee")));
                p.setType(request.getParameter("type"));
                p.setDescription(request.getParameter("description"));
                p.setMembreId(membreId);

                service.update(p, membreId);
            }

            if ("supprimer".equals(action)) {

                Long id = Long.parseLong(request.getParameter("id"));
                service.softDelete(id, membreId);
            }

        } catch (Exception e) {
            request.getSession().setAttribute("error", e.getMessage());
        }

        response.sendRedirect(
                request.getContextPath() + "/membre/publications");
    }
}