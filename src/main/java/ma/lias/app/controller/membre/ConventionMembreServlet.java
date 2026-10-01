package ma.lias.app.controller.membre;

import ma.lias.app.model.Convention;
import ma.lias.app.model.Utilisateur;
import ma.lias.app.model.Membre;
import ma.lias.app.service.ConventionService;
import ma.lias.app.service.MembreService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/membre/conventions")
public class ConventionMembreServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final ConventionService conventionService = new ConventionService();
    private final MembreService membreService = new MembreService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. Sécurité : Vérification de l'utilisateur connecté
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        Utilisateur user = (Utilisateur) session.getAttribute("user");
        
        try {
            // 2. Récupérer toutes les conventions
            List<Convention> toutesLesConventions = conventionService.findAll();
            
            // 3. Sécurité / Filtre : On ne montre aux membres QUE les conventions non archivées
            List<Convention> conventionsActives = toutesLesConventions.stream()
                    .filter(c -> !c.getArchive())
                    .collect(Collectors.toList());
            
            // 4. Envoi des données à la page JSP
            request.setAttribute("conventions", conventionsActives);
            request.setAttribute("contentPage", "/WEB-INF/views/membre/conventions-liste.jsp");
            
            // ✅ AJOUT : Titre de la page pour activer l'onglet dans le sidebar
            request.setAttribute("pageTitle", "Conventions");
            
            // 5. Redirection vers le bon layout selon le rôle
            forwardToLayout(request, response, user);
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Erreur lors du chargement des conventions", e);
        }
    }

    private void forwardToLayout(HttpServletRequest request, HttpServletResponse response, Utilisateur user)
            throws ServletException, IOException {
        
        String layoutPath = "/WEB-INF/views/membre/layout.jsp";

        if (user.isAdmin()) {
            layoutPath = "/WEB-INF/views/admin/layout.jsp";
        } else {
            Membre membre = membreService.findByUserId(user.getId());
            if (membre != null && "DIRECTEUR".equalsIgnoreCase(membre.getRole())) {
                layoutPath = "/WEB-INF/views/directeur/layout.jsp"; 
            }
        }

        request.getRequestDispatcher(layoutPath).forward(request, response);
    }
}