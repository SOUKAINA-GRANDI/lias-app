package ma.lias.app.controller.directeur;

import ma.lias.app.enums.TypeDocument;
import ma.lias.app.model.Document;
import ma.lias.app.service.DocumentService;
import ma.lias.app.service.EvenementService;
import ma.lias.app.service.NotificationService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.util.List;

/**
 * CDC §10 - Archivage documentaire ⭐
 * Upload, consultation, recherche, archivage et historisation des versions.
 */
@WebServlet("/directeur/documents")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,       // 1 Mo
    maxFileSize       = 10 * 1024 * 1024,  // 10 Mo
    maxRequestSize    = 15 * 1024 * 1024   // 15 Mo
)
public class GestionDocumentServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final DocumentService documentService = new DocumentService();
    private final EvenementService evenementService = new EvenementService();
    private final NotificationService notificationService = new NotificationService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "liste";

        if ("nouveau".equals(action)) {

            request.setAttribute("evenements", evenementService.findAll());
            request.setAttribute("typesDocument", TypeDocument.values());
            request.setAttribute("contentPage", "/WEB-INF/views/directeur/document-form.jsp");

        } else if ("historique".equals(action)) {

            String idParam = request.getParameter("id");
            if (idParam == null || !idParam.matches("\\d+")) {
                response.sendRedirect(request.getContextPath() + "/directeur/documents");
                return;
            }

            Long id = Long.parseLong(idParam);
            List<Document> versions = documentService.findVersions(id);

            request.setAttribute("versions", versions);
            request.setAttribute("contentPage", "/WEB-INF/views/directeur/document-historique.jsp");

        } else {
            String q = request.getParameter("q");
            String type = request.getParameter("type");

            String filtre = request.getParameter("filtre");
            if (!"archives".equals(filtre) && !"tous".equals(filtre)) {
                filtre = "actifs";
            }

            if (q != null && !q.isBlank()) {
                request.setAttribute("documents", documentService.search(q, filtre));
            } else {
                request.setAttribute("documents", documentService.findByType(type, filtre));
            }

            request.setAttribute("typesDocument", TypeDocument.values());
            request.setAttribute("filtreType", type);
            request.setAttribute("filtreQ", q);
            request.setAttribute("filtre", filtre);
            request.setAttribute("countActifs", documentService.count("actifs"));
            request.setAttribute("countArchives", documentService.count("archives"));
            request.setAttribute("contentPage", "/WEB-INF/views/directeur/documents-content.jsp");
        }

        request.setAttribute("pageTitle", "Documents");
        request.getRequestDispatcher("/WEB-INF/views/directeur/layout.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String action = request.getParameter("action");

        try {
            switch (action) {

                case "ajouter": {
                    String titre = request.getParameter("titre");
                    String type = request.getParameter("type");
                    String evenementIdParam = request.getParameter("evenementId");
                    Long evenementId = (evenementIdParam != null && evenementIdParam.matches("\\d+"))
                            ? Long.parseLong(evenementIdParam) : null;
                    Part fichier = request.getPart("fichier");

                    documentService.upload(titre, type, evenementId, fichier);
                    notificationService.notifierStatuts(
                            java.util.List.of("PERMANENT", "ASSOCIE", "DOCTORANT"),
                            "Un nouveau document a été ajouté : « " + titre + " ».");
                    break;
                }

                case "remplacer": {
                    Long id = Long.parseLong(request.getParameter("id"));
                    Part fichier = request.getPart("nouveauFichier");

                    documentService.remplacer(id, fichier);

                    notificationService.notifierStatuts(
                            java.util.List.of("PERMANENT", "ASSOCIE", "DOCTORANT"),
                            "Une nouvelle version d'un document a été déposée.");
                    break;
                }

                case "archiver":
                    documentService.archive(Long.parseLong(request.getParameter("id")));
                    break;

                case "desarchiver":
                    documentService.desarchiver(Long.parseLong(request.getParameter("id")));
                    break;

                default:
                    request.getSession().setAttribute("error", "Action inconnue.");
            }

            request.getSession().setAttribute("success", "Opération effectuée avec succès.");

        } catch (Exception e) {
            request.getSession().setAttribute("error", e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/directeur/documents");
    }
}