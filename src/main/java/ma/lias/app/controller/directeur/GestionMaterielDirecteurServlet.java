package ma.lias.app.controller.directeur;

import ma.lias.app.model.Materiel;
import ma.lias.app.service.MaterielService;
import ma.lias.app.service.DemandeMaterielService;
import ma.lias.app.service.AttributionService;
import ma.lias.app.service.MembreService; 
import ma.lias.app.enums.StatutDemande;
import ma.lias.app.dao.SuiviMaterielDAO;
import java.util.List;
import java.util.Map;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Date;

@WebServlet("/directeur/materiel")
public class GestionMaterielDirecteurServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final MaterielService materielService = new MaterielService();
    private final DemandeMaterielService demandeService = new DemandeMaterielService();
    private final AttributionService attributionService = new AttributionService();
    private final MembreService membreService = new MembreService(); 
    private final SuiviMaterielDAO suiviDAO = new SuiviMaterielDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "liste";

        switch (action) {
            case "nouveau":
                request.setAttribute("contentPage", "/WEB-INF/views/directeur/materiel-form.jsp");
                break;
                
            case "demandes":
                request.setAttribute("demandes", demandeService.findAllEnAttente());
                request.setAttribute("contentPage", "/WEB-INF/views/directeur/demandes-materiel.jsp");
                break;
            case "suivi":
                List<Map<String, Object>> suivi = suiviDAO.findSuiviMembres();
                double moyenne = suivi.stream()
                        .mapToInt(r -> (Integer) r.get("total"))
                        .average().orElse(0);

                request.setAttribute("suiviMembres", suivi);
                request.setAttribute("moyenne", Math.round(moyenne * 10) / 10.0);
                request.setAttribute("materiels", materielService.findAll());

                String mid = request.getParameter("materielId");
                if (mid != null && mid.matches("\\d+")) {
                    Long materielChoisiId = Long.parseLong(mid);
                    request.setAttribute("materielChoisi", materielService.findById(materielChoisiId));
                    request.setAttribute("recus", suiviDAO.findRecus(materielChoisiId));
                    request.setAttribute("nonServis", suiviDAO.findNonServis(materielChoisiId));
                }
                request.setAttribute("contentPage", "/WEB-INF/views/directeur/materiel-suivi.jsp");
                break;
            default:
                request.setAttribute("materiels", materielService.findAll());
                request.setAttribute("suiviMembres", suiviDAO.findSuiviMembres());
                // ⚠️ Le formulaire poste "membreId" vers AttributionService.attribuer(materielId, membreId)
                // qui écrit dans attribution_materiel.membre_id : il faut donc bien la liste des MEMBRES,
                // pas des UTILISATEURS (deux tables/clés différentes).
                request.setAttribute("membres", membreService.findAll());
                request.setAttribute("contentPage", "/WEB-INF/views/directeur/materiel-content.jsp");
        }

        request.getRequestDispatcher("/WEB-INF/views/directeur/layout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String action = request.getParameter("action");
        String materielIdRedirect = request.getParameter("materielId"); // pour revenir sur le bon matériel après un retour

        try {
            if ("ajouter".equals(action)) {
                Materiel m = new Materiel();
                m.setNom(request.getParameter("nom"));
                m.setType(request.getParameter("type"));
                m.setMarque(request.getParameter("marque"));
                
                // Correspondance exacte avec l'attribut name="quantite_totale" du formulaire
                String quantiteParam = request.getParameter("quantite_totale");
                if (quantiteParam != null && !quantiteParam.isEmpty()) {
                    m.setQuantiteTotale(Integer.parseInt(quantiteParam));
                }

                // Récupération et conversion de la date
                String dateParam = request.getParameter("date_achat");
                if (dateParam != null && !dateParam.isEmpty()) {
                    m.setDateAchat(Date.valueOf(dateParam));
                }

                materielService.create(m);
            }

            if ("attribuer".equals(action)) {
                Long materielId = Long.parseLong(request.getParameter("materielId"));
                Long membreId = Long.parseLong(request.getParameter("membreId"));
                attributionService.attribuer(materielId, membreId);
            }

            if ("retourner".equals(action)) {
                Long attributionId = Long.parseLong(request.getParameter("attributionId"));
                attributionService.retourner(attributionId);
            }

            if ("validerDemande".equals(action)) {
                Long demandeId = Long.parseLong(request.getParameter("demandeId"));
                demandeService.updateStatut(demandeId, StatutDemande.VALIDEE);
            }

            if ("refuserDemande".equals(action)) {
                Long demandeId = Long.parseLong(request.getParameter("demandeId"));
                demandeService.updateStatut(demandeId, StatutDemande.REFUSEE);
            }

        } catch (Exception e) {
            // Permet de voir le message exact de l'erreur en cas d'échec SQL ou logique
            request.getSession().setAttribute("error", e.getMessage());
        }

        // Après un retour, on revient directement sur le suivi du même matériel
        if ("retourner".equals(action) && materielIdRedirect != null) {
            response.sendRedirect(request.getContextPath()
                    + "/directeur/materiel?action=suivi&materielId=" + materielIdRedirect);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/directeur/materiel");
    }
}