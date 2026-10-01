package ma.lias.app.controller.membre;

import ma.lias.app.model.Reunion;
import ma.lias.app.model.Utilisateur;
import ma.lias.app.model.Membre;
import ma.lias.app.service.ReunionService;
import ma.lias.app.service.MembreService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/membre/reunions", "/membre/pv"})
public class ReunionMembreServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final ReunionService reunionService = new ReunionService();
    private final MembreService membreService = new MembreService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. Sécurité : Vérification de la session
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        Utilisateur user = (Utilisateur) session.getAttribute("user");
        
        try {
            List<Reunion> reunions;
            String searchKeyword = request.getParameter("search");
            
            // 2. Gestion de la recherche éventuelle
            if (searchKeyword != null && !searchKeyword.isBlank()) {
                reunions = new ma.lias.app.dao.ReunionDAO().search(searchKeyword.trim());
                request.setAttribute("searchKeyword", searchKeyword);
            } else {
                reunions = reunionService.findAll();
            }
            
            // 3. Passage des données à la JSP
            request.setAttribute("reunions", reunions);
            request.setAttribute("contentPage", "/WEB-INF/views/membre/reunions-liste.jsp");
            
            // ✅ AJOUT : Titre de la page pour activer l'onglet dans le sidebar
            request.setAttribute("pageTitle", "Réunions");
            
            // 4. Dispatch selon le Layout utilisateur
            forwardToLayout(request, response, user);
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Erreur lors du chargement des réunions", e);
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