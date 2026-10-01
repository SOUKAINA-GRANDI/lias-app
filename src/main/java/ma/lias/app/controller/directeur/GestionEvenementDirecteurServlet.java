package ma.lias.app.controller.directeur;

import ma.lias.app.model.Evenement;
import ma.lias.app.model.Membre;
import ma.lias.app.service.EvenementService;
import ma.lias.app.service.MembreService;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@WebServlet("/directeur/evenements")
public class GestionEvenementDirecteurServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final EvenementService service = new EvenementService();
    private final MembreService membreService = new MembreService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "liste";

        switch (action) {

            case "edit":
                Long id = Long.parseLong(request.getParameter("id"));
                request.setAttribute("evenement", service.findById(id));
                request.setAttribute("organisateurIds", service.findOrganisateurIds(id));
                request.setAttribute("candidatsOrganisateurs", candidatsOrganisateurs());
                request.setAttribute("contentPage",
                        "/WEB-INF/views/directeur/evenement-form.jsp");
                break;

            case "nouveau":
                request.setAttribute("organisateurIds", Collections.emptySet());
                request.setAttribute("candidatsOrganisateurs", candidatsOrganisateurs());
                request.setAttribute("contentPage",
                        "/WEB-INF/views/directeur/evenement-form.jsp");
                break;

            default:
                request.setAttribute("evenements", service.findAll());
                request.setAttribute("organisateursParEvenement",
                        service.findOrganisateursParEvenement());
                request.setAttribute("contentPage",
                        "/WEB-INF/views/directeur/evenements-content.jsp");
        }

        request.getRequestDispatcher("/WEB-INF/views/directeur/layout.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {

        String action = request.getParameter("action");

        try {

            List<Long> organisateurs = lireOrganisateurs(request);

            switch (action) {

                case "ajouter":
                    service.create(buildEvenement(request), organisateurs);
                    break;

                case "modifier":
                    Evenement e = buildEvenement(request);
                    e.setId(Long.parseLong(request.getParameter("id")));
                    service.update(e, organisateurs);
                    break;

                case "archiver":
                    Long id = Long.parseLong(request.getParameter("id"));
                    service.archive(id);
                    break;
            }

        } catch (Exception ex) {
            request.getSession().setAttribute("error", ex.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/directeur/evenements");
    }

    /** Membres permanents actifs : seuls candidats possibles comme organisateurs. */
    private List<Membre> candidatsOrganisateurs() {
        return membreService.findAll().stream()
                .filter(m -> "PERMANENT".equals(m.getStatut()))
                .toList();
    }

    /** Lit les cases cochées (valeurs numériques uniquement, sans doublons). */
    private List<Long> lireOrganisateurs(HttpServletRequest request) {

        List<Long> ids = new ArrayList<>();
        String[] values = request.getParameterValues("organisateurs");

        if (values != null) {
            for (String v : values) {
                if (v != null && v.matches("\\d+")) {
                    Long id = Long.parseLong(v);
                    if (!ids.contains(id)) ids.add(id);
                }
            }
        }
        return ids;
    }

    private Evenement buildEvenement(HttpServletRequest request) {

        Evenement e = new Evenement();

        e.setTitre(request.getParameter("titre"));
        e.setDescription(request.getParameter("description"));
        e.setType(request.getParameter("type"));
        e.setLieu(request.getParameter("lieu"));
        e.setDateDebut(LocalDateTime.parse(request.getParameter("dateDebut")));

        String df = request.getParameter("dateFin");
        if (df != null && !df.isBlank()) {
            e.setDateFin(LocalDateTime.parse(df));
        }

        // Case cochée par défaut : absente du POST seulement si l'utilisateur l'a décochée
        e.setOuvertAuxAssocies(request.getParameter("ouvertAuxAssocies") != null);

        return e;
    }
}