package ma.lias.app.controller.shared;

import ma.lias.app.model.Evenement;
import ma.lias.app.service.EvenementService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/public/calendrier")
public class CalendrierServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final EvenementService service = new EvenementService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        int currentYear = LocalDate.now().getYear();
        int year = currentYear;

        String yearParam = request.getParameter("annee");
        if (yearParam != null && !yearParam.isBlank()) {
            try {
                year = Integer.parseInt(yearParam);
            } catch (NumberFormatException ignored) {}
        }

        List<Evenement> evenements = service.findByYear(year);
        if (evenements == null) {
            evenements = new ArrayList<>();
        }

        request.setAttribute("evenements", evenements);
        request.setAttribute("annee", year);
        request.setAttribute("anneeCourante", currentYear);
        request.setAttribute("pageTitle", "Calendrier");
        request.setAttribute("contentPage",
                "/WEB-INF/views/public/calendrier.jsp");

        request.getRequestDispatcher(
                "/WEB-INF/views/public/layout.jsp")
                .forward(request, response);
    }
}