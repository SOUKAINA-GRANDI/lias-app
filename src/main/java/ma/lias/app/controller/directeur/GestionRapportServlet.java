package ma.lias.app.controller.directeur;

import ma.lias.app.model.RapportAnnuelData;
import ma.lias.app.service.RapportService;
import ma.lias.app.util.PDFGenerator;
import ma.lias.app.util.ExcelGenerator;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/directeur/rapport")
public class GestionRapportServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final RapportService service = new RapportService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        int year = lireAnnee(request);

        RapportAnnuelData data = service.generer(year);

        // ✅ PDF
        if ("pdf".equals(action)) {

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition",
                    "attachment; filename=rapport_" + year + ".pdf");

            PDFGenerator.generateRapport(
                    response.getOutputStream(),
                    data.getYear(),
                    data.getPrevYear(),
                    data.getPubCurrent(),
                    data.getPubPrev(),
                    data.getEventsCurrent(),
                    data.getEventsPrev(),
                    data.getConvCurrent(),
                    data.getConvPrev(),
                    data.getMembresActifs(),
                    data.getEvenements()
            );

            return;
        }

        // ✅ Excel
        if ("excel".equals(action)) {

            response.setContentType(
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            response.setHeader("Content-Disposition",
                    "attachment; filename=rapport_" + year + ".xlsx");

            try {
                ExcelGenerator.generate(response.getOutputStream(), data);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            return;
        }

        // ✅ Vue normale
        request.setAttribute("rapport", data);
        request.setAttribute("historique", service.findHistorique());
        request.setAttribute("contentPage",
                "/WEB-INF/views/directeur/rapport-content.jsp");

        request.getRequestDispatcher(
                "/WEB-INF/views/directeur/layout.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {

        int year = lireAnnee(request);

        try {
            boolean cree = service.genererEtArchiver(year);
            request.getSession().setAttribute(cree ? "success" : "error",
                    cree ? "Le rapport " + year + " a été archivé avec succès."
                         : "Un rapport pour " + year + " existe déjà dans l'historique.");
        } catch (Exception e) {
            request.getSession().setAttribute("error", "Erreur lors de l'archivage : " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/directeur/rapport?annee=" + year);
    }

    /** Année sécurisée : jamais d'exception sur une saisie invalide. */
    private int lireAnnee(HttpServletRequest request) {
        String yearParam = request.getParameter("annee");
        if (yearParam != null && yearParam.matches("\\d{4}")) {
            return Integer.parseInt(yearParam);
        }
        return LocalDate.now().getYear();
    }
}