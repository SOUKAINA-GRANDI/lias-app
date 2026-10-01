package ma.lias.app.controller.directeur;

import ma.lias.app.model.Equipe;
import ma.lias.app.service.EquipeService;
import ma.lias.app.service.MembreService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * CDC §5.3 (chefs d'équipe) / §7 (rôles) / §21 (traçabilité) :
 * le directeur crée les équipes, y affecte des membres et désigne un chef.
 */
@WebServlet("/directeur/equipes")
public class GestionEquipeServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final EquipeService equipeService = new EquipeService();
    private final MembreService membreService = new MembreService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "liste";

        switch (action) {

            case "nouveau":
                request.setAttribute("contentPage", "/WEB-INF/views/directeur/equipe-form.jsp");
                break;

            case "edit": {
                Long id = parseIdOrRedirect(request, response);
                if (id == null) return;
                request.setAttribute("equipe", equipeService.findById(id));
                request.setAttribute("contentPage", "/WEB-INF/views/directeur/equipe-form.jsp");
                break;
            }

            case "membres": {
                Long id = parseIdOrRedirect(request, response);
                if (id == null) return;
                request.setAttribute("equipe", equipeService.findById(id));
                request.setAttribute("tousMembres", membreService.findAll());
                request.setAttribute("contentPage", "/WEB-INF/views/directeur/equipe-membres.jsp");
                break;
            }

            default:
                request.setAttribute("equipes", equipeService.findAll());
                request.setAttribute("contentPage", "/WEB-INF/views/directeur/equipes-content.jsp");
        }

        request.setAttribute("pageTitle", "Équipes du laboratoire");
        request.getRequestDispatcher("/WEB-INF/views/directeur/layout.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String action = request.getParameter("action");
        String redirect = request.getContextPath() + "/directeur/equipes";

        try {
            switch (action) {

                case "ajouter": {
                    Equipe e = new Equipe();
                    e.setNom(request.getParameter("nom"));
                    e.setDescription(request.getParameter("description"));
                    equipeService.create(e);
                    break;
                }

                case "modifier": {
                    Equipe e = new Equipe();
                    e.setId(Long.parseLong(request.getParameter("id")));
                    e.setNom(request.getParameter("nom"));
                    e.setDescription(request.getParameter("description"));
                    equipeService.update(e);
                    break;
                }

                case "archiver":
                    equipeService.archive(Long.parseLong(request.getParameter("id")));
                    break;

                case "assignerMembre": {
                    Long equipeId = Long.parseLong(request.getParameter("equipeId"));
                    Long membreId = Long.parseLong(request.getParameter("membreId"));
                    equipeService.assignerMembre(equipeId, membreId);
                    redirect += "?action=membres&id=" + equipeId;
                    break;
                }

                case "retirerMembre": {
                    Long equipeId = Long.parseLong(request.getParameter("equipeId"));
                    Long membreId = Long.parseLong(request.getParameter("membreId"));
                    equipeService.retirerMembre(equipeId, membreId);
                    redirect += "?action=membres&id=" + equipeId;
                    break;
                }

                case "assignerChef": {
                    Long equipeId = Long.parseLong(request.getParameter("equipeId"));
                    Long membreId = Long.parseLong(request.getParameter("membreId"));
                    equipeService.assignerChef(equipeId, membreId);
                    redirect += "?action=membres&id=" + equipeId;
                    break;
                }

                default:
                    request.getSession().setAttribute("error", "Action inconnue.");
            }

            request.getSession().setAttribute("success", "Opération effectuée avec succès.");

        } catch (Exception e) {
            request.getSession().setAttribute("error", e.getMessage());
        }

        response.sendRedirect(redirect);
    }

    /** Parse "id", ou redirige proprement vers la liste si absent/invalide. */
    private Long parseIdOrRedirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idParam = request.getParameter("id");
        if (idParam == null || !idParam.matches("\\d+")) {
            response.sendRedirect(request.getContextPath() + "/directeur/equipes");
            return null;
        }
        return Long.parseLong(idParam);
    }
}