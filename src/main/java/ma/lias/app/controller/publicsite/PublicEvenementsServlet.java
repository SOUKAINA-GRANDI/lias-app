package ma.lias.app.controller.publicsite;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ma.lias.app.dao.EvenementDAO;
import ma.lias.app.model.Evenement;

@WebServlet("/public/evenements")
public class PublicEvenementsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private EvenementDAO dao = new EvenementDAO();

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        List<Evenement> evenements = dao.findAll();
        if (evenements == null) evenements = new ArrayList<>();

        request.setAttribute("evenements", evenements);
        request.setAttribute("pageTitle", "Événements");
        request.setAttribute("contentPage",
                "/WEB-INF/views/public/evenements.jsp");

        request.getRequestDispatcher(
                "/WEB-INF/views/public/layout.jsp")
                .forward(request, response);
    }
}