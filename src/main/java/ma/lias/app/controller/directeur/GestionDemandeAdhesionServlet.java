package ma.lias.app.controller.directeur;
import ma.lias.app.enums.StatutMembre;
import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ma.lias.app.service.DemandeAdhesionService;

@WebServlet("/directeur/demandes")
public class GestionDemandeAdhesionServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final DemandeAdhesionService service = new DemandeAdhesionService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("demandes", service.findAllEnAttente());
        request.setAttribute("pageTitle", "Demandes d'adhésion");
        request.setAttribute("contentPage", "/WEB-INF/views/directeur/demandes-adhesion.jsp");

        request.getRequestDispatcher("/WEB-INF/views/directeur/layout.jsp")
               .forward(request, response);
    }

    @Override
   
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {

        String action = request.getParameter("action");
        String idParam = request.getParameter("id");

        // ✅ 1. Sécurisation et validation de l'ID reçu
        if (idParam == null || !idParam.matches("\\d+")) {
            response.sendRedirect(request.getContextPath() + "/directeur/demandes");
            return;
        }

        Long id = Long.parseLong(idParam);

        try {
            // ✅ 2. Action d'acceptation de la demande
            if ("valider".equals(action)) {
                
                // Le service s'occupe de TOUT : génération du MDP clair, 
                // insertion Utilisateur, insertion Membre, Historique, commit et envoi de l'email.
                String statutParam = request.getParameter("statut");
                StatutMembre statut = (statutParam == null || statutParam.isBlank())
                        ? StatutMembre.PERMANENT
                        : StatutMembre.valueOf(statutParam);
                service.valider(id, statut);
                
                request.getSession().setAttribute("success", 
                        "La demande a été validée avec succès et ses accès de connexion lui ont été envoyés par e-mail.");
            }

            // ✅ 3. Action de refus de la demande
            else if ("refuser".equals(action)) {
                
                service.refuser(id);
                
                request.getSession().setAttribute("success", 
                        "La demande a été refusée. Un e-mail de notification a été envoyé.");
            }

        } catch (Exception e) {
            // ✅ 4. Capture des BusinessException ou soucis techniques
            request.getSession().setAttribute("error", 
                    "Une erreur est survenue lors du traitement : " + e.getMessage());
        }

        // ✅ 5. Redirection vers la vue de gestion des demandes
     
        response.sendRedirect(request.getContextPath() + "/directeur/demandes");    }}