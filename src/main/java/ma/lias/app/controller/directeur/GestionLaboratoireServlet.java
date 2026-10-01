package ma.lias.app.controller.directeur;

import ma.lias.app.model.Laboratoire;
import ma.lias.app.service.LaboratoireService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;

/**
 * CDC §4.1 - Informations générales du laboratoire (nom, date de création...).
 */
@WebServlet("/directeur/laboratoire")
public class GestionLaboratoireServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final LaboratoireService laboratoireService = new LaboratoireService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("laboratoire", laboratoireService.get());
        request.setAttribute("pageTitle", "Laboratoire");
        request.setAttribute("contentPage", "/WEB-INF/views/directeur/laboratoire-content.jsp");

        request.getRequestDispatcher("/WEB-INF/views/directeur/layout.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        try {
            Laboratoire l = new Laboratoire();
            l.setNom(request.getParameter("nom"));
            l.setDescription(request.getParameter("description"));
            l.setAdresse(request.getParameter("adresse"));
            l.setEmailContact(request.getParameter("emailContact"));
            l.setTelephone(request.getParameter("telephone"));
            l.setSiteWeb(request.getParameter("siteWeb"));

            String dateCreationParam = request.getParameter("dateCreation");
            if (dateCreationParam != null && !dateCreationParam.isBlank()) {
                l.setDateCreation(LocalDate.parse(dateCreationParam));
            }

            laboratoireService.update(l);

            request.getSession().setAttribute("success", "Informations du laboratoire mises à jour.");

        } catch (Exception e) {
            request.getSession().setAttribute("error", e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/directeur/laboratoire");
    }
}
