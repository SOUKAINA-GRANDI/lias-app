package ma.lias.app.controller.membre;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import ma.lias.app.dao.AttributionMaterielDAO;
import ma.lias.app.model.Membre;
import ma.lias.app.model.Utilisateur;
import ma.lias.app.service.DemandeMaterielService;
import ma.lias.app.service.MembreService;

@WebServlet("/membre/materiel")
public class GestionMaterielMembreServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final AttributionMaterielDAO attributionDAO = new AttributionMaterielDAO();
    private final DemandeMaterielService demandeService = new DemandeMaterielService();
    private final MembreService membreService = new MembreService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Utilisateur user = checkUser(request, response);
        if (user == null) return;

        Membre membre = membreService.findByUserId(user.getId());
        if (membre == null) {
            response.sendRedirect(request.getContextPath() + "/membre/dashboard");
            return;
        }

        // Chargement des matériels possédés par le membre
        request.setAttribute("materiels", attributionDAO.findAllByMembre(membre.getId()));

        request.setAttribute("pageTitle", "Matériel de Calcul & Traçabilité");
        request.setAttribute("contentPage", "/WEB-INF/views/membre/materiel-content.jsp");

        request.getRequestDispatcher("/WEB-INF/views/membre/layout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        Utilisateur user = checkUser(request, response);
        if (user == null) return;

        Membre membre = membreService.findByUserId(user.getId());
        String action = request.getParameter("action");

        if ("demande".equals(action)) {
            String designation = request.getParameter("designation");
            String quantiteStr = request.getParameter("quantite");
            String urgence = request.getParameter("urgence");
            String justification = request.getParameter("justification");

            // Validation des champs reçus de l'interface graphique
            if (designation == null || designation.isBlank() || 
                quantiteStr == null || quantiteStr.isBlank() || 
                justification == null || justification.isBlank()) {
                
                response.sendRedirect(request.getContextPath() + "/membre/materiel?error=invalid");
                return;
            }

            try {
                int quantite = Integer.parseInt(quantiteStr);
                
                // Formater proprement les données pour le champ TEXT 'description'
                // Ce format est très lisible à la fois en base de données et sur l'espace Directeur !
                String descriptionStructuree = "MATÉRIEL : " + designation.trim() + "\n" +
                                               "QUANTITÉ : " + quantite + "\n" +
                                               "URGENCE : " + urgence + "\n" +
                                               "JUSTIFICATION : " + justification.trim();

                // Appel de ton service existant sans rien modifier !
                demandeService.soumettre(membre.getId(), descriptionStructuree);

                response.sendRedirect(request.getContextPath() + "/membre/materiel?success=demande");
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/membre/materiel?error=invalid");
            }
        }
    }

    private Utilisateur checkUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return null;
        }
        return (Utilisateur) session.getAttribute("user");
    }
}