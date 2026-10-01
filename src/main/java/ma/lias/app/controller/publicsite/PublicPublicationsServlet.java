package ma.lias.app.controller.publicsite;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ma.lias.app.dao.PublicationDAO;
import ma.lias.app.model.Publication;

@WebServlet("/public/publications")
public class PublicPublicationsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private PublicationDAO dao = new PublicationDAO();

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

    	List<Publication> publications = dao.findAllPublic();
    	if (publications == null) publications = new ArrayList<>();

        List<String> typesDistincts = publications.stream()
                .map(Publication::getType)
                .filter(t -> t != null && !t.isBlank())
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        request.setAttribute("publications", publications);
        request.setAttribute("typesDistincts", typesDistincts);
        request.setAttribute("pageTitle", "Publications");
        request.setAttribute("contentPage",
                "/WEB-INF/views/public/publications.jsp");

        request.getRequestDispatcher(
                "/WEB-INF/views/public/layout.jsp")
                .forward(request, response);
    }
}

