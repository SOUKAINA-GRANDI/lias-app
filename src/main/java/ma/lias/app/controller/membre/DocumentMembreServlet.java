package ma.lias.app.controller.membre;

import ma.lias.app.enums.TypeDocument;
import ma.lias.app.service.DocumentService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/** CDC §10 - Consultation des documents archivés (lecture seule pour les membres). */
@WebServlet("/membre/documents")
public class DocumentMembreServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final DocumentService documentService = new DocumentService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String q = request.getParameter("q");
        String type = request.getParameter("type");

        if (q != null && !q.isBlank()) {
            request.setAttribute("documents", documentService.search(q));
        } else {
            request.setAttribute("documents", documentService.findByType(type));
        }

        request.setAttribute("typesDocument", TypeDocument.values());
        request.setAttribute("filtreType", type);
        request.setAttribute("filtreQ", q);

        request.setAttribute("pageTitle", "Documents");
        request.setAttribute("contentPage", "/WEB-INF/views/membre/documents-content.jsp");

        request.getRequestDispatcher("/WEB-INF/views/membre/layout.jsp")
               .forward(request, response);
    }
}