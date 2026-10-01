package ma.lias.app.controller.directeur;

import ma.lias.app.enums.StatutMembre;
import ma.lias.app.exception.BusinessException;
import ma.lias.app.model.Mandat;
import ma.lias.app.service.MembreService;
import ma.lias.app.service.MandatService;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/directeur/membres")
public class GestionMembreDirecteurServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final MembreService service =
            new MembreService();

    private final MandatService mandatService =
            new MandatService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("historique".equals(action)) {
            String idParam = request.getParameter("id");
            if (idParam == null || !idParam.matches("\\d+")) {
                response.sendRedirect(request.getContextPath() + "/directeur/membres");
                return;
            }
            Long id = Long.parseLong(idParam);
            request.setAttribute("membre", service.findById(id));
            request.setAttribute("historique", service.findHistorique(id));
            request.setAttribute("contentPage",
                    "/WEB-INF/views/directeur/membre-historique-content.jsp");

            request.getRequestDispatcher(
                    "/WEB-INF/views/directeur/layout.jsp")
                    .forward(request, response);
            return;
        }

        String filtre = request.getParameter("filtre");
        if (!"inactifs".equals(filtre) && !"tous".equals(filtre)) {
            filtre = "actifs";
        }

        int page = 1;
        try {
            String pageParam = request.getParameter("page");
            if (pageParam != null) page = Math.max(1, Integer.parseInt(pageParam));
        } catch (NumberFormatException ignored) { }

        int taillePage = 20;
        int total = service.count(filtre);
        int totalPages = Math.max(1, (int) Math.ceil(total / (double) taillePage));
        page = Math.min(page, totalPages);

        request.setAttribute("membres", service.findPaginated(page, taillePage, filtre));
        request.setAttribute("filtre", filtre);
        request.setAttribute("countActifs", service.count("actifs"));
        request.setAttribute("countInactifs", service.count("inactifs"));
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

        request.setAttribute("contentPage",
                "/WEB-INF/views/directeur/membres-content.jsp");

        request.getRequestDispatcher(
                "/WEB-INF/views/directeur/layout.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {

        String action = request.getParameter("action");
        String idParam = request.getParameter("id");

        if (action == null || idParam == null || !idParam.matches("\\d+")) {
            request.getSession().setAttribute("error", "Requête invalide.");
            response.sendRedirect(request.getContextPath() + "/directeur/membres");
            return;
        }

        Long id = Long.parseLong(idParam);

        try {

            Mandat mandatActif =
                    mandatService.getMandatActif();

            if ("desactiver".equals(action)
                    && mandatActif != null
                    && mandatActif.getDirecteurId().equals(id)) {

                throw new BusinessException(
                        "Le directeur actif ne peut pas se désactiver.");
            }

            switch (action) {

                case "changerStatut":
                	StatutMembre statut =StatutMembre.valueOf( request.getParameter("statut"));

            service.changeStatut(id, statut);
                    break;

                case "reactiver":
                    StatutMembre statutReactivation =
                            StatutMembre.valueOf(request.getParameter("statut"));
                    service.reactivate(id, statutReactivation);
                    break;
                case "toggleDroitPublication":
                    service.toggleDroitPublication(id);
                    break;
                case "desactiverAvecMotif":
                    StatutMembre motif = StatutMembre.valueOf(request.getParameter("statutDepart"));
                    service.changeStatut(id, motif);   // change le statut ET désactive, en une seule opération
                    break;
                case "desactiver":
                    service.deactivate(id);
                    break;
            }
            request.getSession().setAttribute("success", "Opération effectuée avec succès.");

        } catch (Exception e) {
            request.getSession()
                    .setAttribute("error",
                            e.getMessage());
        }

        response.sendRedirect(
                request.getContextPath()
                        + "/directeur/membres");
    }
}