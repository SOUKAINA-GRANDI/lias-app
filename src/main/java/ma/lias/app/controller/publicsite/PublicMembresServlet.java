package ma.lias.app.controller.publicsite;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ma.lias.app.dao.MembreDAO;
import ma.lias.app.model.Membre;

@WebServlet("/public/membres")
public class PublicMembresServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private MembreDAO dao = new MembreDAO();

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        List<Membre> membres = dao.findAllPublic();

        // Liste des équipes distinctes présentes parmi les membres affichés,
        // pour les puces de filtre (pas de doublon, triée alphabétiquement)
        List<String> equipesDistinctes = membres.stream()
                .map(Membre::getEquipeNom)
                .filter(nom -> nom != null && !nom.isBlank())
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        request.setAttribute("membres", membres);
        request.setAttribute("equipesDistinctes", equipesDistinctes);
        request.setAttribute("pageTitle", "Membres");
        request.setAttribute("contentPage",
                "/WEB-INF/views/public/membres.jsp");

        request.getRequestDispatcher(
                "/WEB-INF/views/public/layout.jsp")
                .forward(request, response);
    }
}
