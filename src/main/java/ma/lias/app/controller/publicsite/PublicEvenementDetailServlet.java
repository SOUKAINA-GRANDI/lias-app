package ma.lias.app.controller.publicsite;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ma.lias.app.model.Evenement;
import ma.lias.app.service.EvenementService;

@WebServlet("/public/evenement-detail")
public class PublicEvenementDetailServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final EvenementService service = new EvenementService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");

        if (idParam == null || !idParam.matches("\\d+")) {
            response.sendRedirect(request.getContextPath() + "/public/evenements");
            return;
        }

        Evenement evenement = service.findById(Long.parseLong(idParam));

        if (evenement == null) {
            response.sendRedirect(request.getContextPath() + "/public/evenements");
            return;
        }

        request.setAttribute("evenement", evenement);
        request.setAttribute("organisateurs", service.findOrganisateursTexte(evenement.getId()));
        request.setAttribute("pageTitle", evenement.getTitre());
        request.setAttribute("contentPage",
                "/WEB-INF/views/public/evenement-detail.jsp");

        request.getRequestDispatcher(
                "/WEB-INF/views/public/layout.jsp")
                .forward(request, response);
    }
}
