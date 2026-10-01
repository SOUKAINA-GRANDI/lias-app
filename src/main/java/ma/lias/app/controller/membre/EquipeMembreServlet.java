package ma.lias.app.controller.membre;

import ma.lias.app.model.Membre;
import ma.lias.app.service.MembreService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/membre/equipe")
public class EquipeMembreServlet extends HttpServlet {
   
	private static final long serialVersionUID = 1L;
	private final MembreService membreService = new MembreService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Récupération de la liste de tous les membres actifs via ton service
    	List<Membre> listeMembres = membreService.findAllAvecEquipe();
        request.setAttribute("membres", listeMembres);
        
        // 2. Configuration des variables pour ton layout dynamique
        request.setAttribute("pageTitle", "Membres du lab");
        request.setAttribute("contentPage", "/WEB-INF/views/membre/equipe-content.jsp");
        
        // 3. Redirection vers ton layout principal
        request.getRequestDispatcher("/WEB-INF/views/membre/layout.jsp").forward(request, response);
    }
}