package ma.lias.app.controller;

import java.io.IOException;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ma.lias.app.model.Mandat;
import ma.lias.app.model.Membre;
import ma.lias.app.model.Utilisateur;
import ma.lias.app.service.MandatService;
import ma.lias.app.service.MembreService;
import ma.lias.app.service.SearchService;
import ma.lias.app.service.SearchService.Niveau;

@WebServlet("/search")
public class SearchServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final SearchService service = new SearchService();
    private final MembreService membreService = new MembreService();
    private final MandatService mandatService = new MandatService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String q = request.getParameter("q");
        if (q == null || q.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        q = q.trim();

        // Niveau d'accès + layout, déterminés côté serveur
        Niveau niveau = Niveau.PUBLIC;
        String layout = "/WEB-INF/views/public/layout.jsp";

        HttpSession session = request.getSession(false);
        Object o = (session != null) ? session.getAttribute("user") : null;

        if (o instanceof Utilisateur user) {
            if (user.isAdmin()) {
                niveau = Niveau.DIRECTION;
                layout = "/WEB-INF/views/admin/layout.jsp";
            } else {
                Membre membre = membreService.findByUserId(user.getId());
                if (membre != null) {
                    Mandat mandat = mandatService.getMandatActif();
                    boolean directeur = mandat != null
                            && mandat.getDirecteurId() != null
                            && mandat.getDirecteurId().equals(membre.getId());
                    if (directeur) {
                        niveau = Niveau.DIRECTION;
                        layout = "/WEB-INF/views/directeur/layout.jsp";
                    } else {
                        niveau = niveauPourStatut(membre.getStatut());
                        layout = "/WEB-INF/views/membre/layout.jsp";
                        // Pour que le menu masque les modules interdits (doctorant, associé)
                        request.setAttribute("statutMembreConnecte", membre.getStatut());
                    }
                }
            }
        }

        Map<String, Object> results = service.search(q, niveau);
        request.setAttribute("query", q);
        request.setAttribute("membres", results.get("membres"));
        request.setAttribute("publications", results.get("publications"));
        request.setAttribute("evenements", results.get("evenements"));
        request.setAttribute("conventions", results.get("conventions"));
        request.setAttribute("reunions", results.get("reunions"));
        request.setAttribute("documents", results.get("documents"));
        request.setAttribute("pageTitle", "Résultats de recherche");
        request.setAttribute("contentPage", "/WEB-INF/views/shared/search-content.jsp");

        request.getRequestDispatcher(layout).forward(request, response);
    }

    private Niveau niveauPourStatut(String statut) {
        if (statut == null) return Niveau.PUBLIC;
        return switch (statut) {
            case "PERMANENT" -> Niveau.PERMANENT;
            case "ASSOCIE"   -> Niveau.ASSOCIE;
            case "DOCTORANT" -> Niveau.DOCTORANT;
            default          -> Niveau.PUBLIC;
        };
    }
}