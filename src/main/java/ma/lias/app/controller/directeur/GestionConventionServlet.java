package ma.lias.app.controller.directeur;

import java.io.IOException;
import java.time.LocalDate;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import ma.lias.app.config.Constantes;
import ma.lias.app.model.Convention;
import ma.lias.app.service.ConventionService;
import ma.lias.app.util.FileUploadUtil;
import ma.lias.app.service.EvenementService;
import ma.lias.app.service.DocumentService;

@WebServlet("/directeur/conventions")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,       // 1 Mo
    maxFileSize       = 10 * 1024 * 1024,  // 10 Mo
    maxRequestSize    = 15 * 1024 * 1024   // 15 Mo
)
public class GestionConventionServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final ConventionService service = new ConventionService();
    private final EvenementService evenementService = new EvenementService();
    private final DocumentService documentService = new DocumentService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        if (action == null) action = "liste";

        switch (action) {
            case "nouveau":
                req.setAttribute("contentPage",
                    "/WEB-INF/views/directeur/convention-form.jsp");
                break;
            case "edit":
                Long id = Long.parseLong(req.getParameter("id"));
                req.setAttribute("convention", service.findById(id));
                req.setAttribute("contentPage",
                    "/WEB-INF/views/directeur/convention-form.jsp");
                break;
            case "voir":
                Long convId = Long.parseLong(req.getParameter("id"));
                req.setAttribute("convention", service.findById(convId));
                req.setAttribute("evenementsAssocies", service.findEvenementsAssocies(convId));
                req.setAttribute("documentsAssocies", service.findDocumentsAssocies(convId));
                req.setAttribute("tousEvenements", evenementService.findAll());
                req.setAttribute("tousDocuments", documentService.findAll());
                req.setAttribute("contentPage",
                    "/WEB-INF/views/directeur/convention-detail.jsp");
                break;
            default:
                req.setAttribute("conventions", service.findAll());
                req.setAttribute("contentPage",
                    "/WEB-INF/views/directeur/conventions.jsp");
        }

        req.getRequestDispatcher("/WEB-INF/views/directeur/layout.jsp")
           .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String action = req.getParameter("action");

        try {
            if ("ajouter".equals(action)) {

                Part fichier = req.getPart("fichier");
                if (fichier == null || fichier.getSize() == 0) {
                    throw new IllegalArgumentException("Le fichier de la convention est obligatoire.");
                }

                String chemin = FileUploadUtil.sauvegarderDocument(fichier, Constantes.UPLOAD_DIR, "conventions");
                if (chemin == null) {
                    throw new IllegalArgumentException("Échec de l'enregistrement du fichier.");
                }

                Convention c = new Convention();
                c.setTitre(req.getParameter("titre"));
                c.setPartenaire(req.getParameter("partenaire"));
                c.setDateDebut(LocalDate.parse(req.getParameter("dateDebut")));
                String df = req.getParameter("dateFin");
                if (df != null && !df.isBlank())
                    c.setDateFin(LocalDate.parse(df));
                c.setDescription(req.getParameter("description"));
                c.setCheminFichier(chemin);

                service.create(c);
            }

            if ("modifier".equals(action)) {

                Long id = Long.parseLong(req.getParameter("id"));

                // On repart de l'existant pour ne jamais perdre une donnée non repostée
                // (ex: le fichier si l'utilisateur n'en choisit pas un nouveau)
                Convention existant = service.findById(id);
                if (existant == null) {
                    throw new IllegalArgumentException("Convention introuvable.");
                }

                Convention c = new Convention();
                c.setId(id);
                c.setTitre(req.getParameter("titre"));
                c.setPartenaire(req.getParameter("partenaire"));

                String dd = req.getParameter("dateDebut");
                c.setDateDebut(dd != null && !dd.isBlank()
                        ? LocalDate.parse(dd) : existant.getDateDebut());

                String df = req.getParameter("dateFin");
                c.setDateFin(df != null && !df.isBlank() ? LocalDate.parse(df) : null);

                c.setDescription(req.getParameter("description"));

                Part fichier = req.getPart("fichier");
                if (fichier != null && fichier.getSize() > 0) {
                    String chemin = FileUploadUtil.sauvegarderDocument(fichier, Constantes.UPLOAD_DIR, "conventions");
                    c.setCheminFichier(chemin != null ? chemin : existant.getCheminFichier());
                } else {
                    c.setCheminFichier(existant.getCheminFichier());
                }

                service.update(c);
            }

            if ("archiver".equals(action)) {
                service.archive(Long.parseLong(req.getParameter("id")));
            }
            if ("lierEvenement".equals(action)) {
                Long conventionId = Long.parseLong(req.getParameter("conventionId"));
                Long evenementId = Long.parseLong(req.getParameter("evenementId"));
                service.lierEvenement(conventionId, evenementId);
                resp.sendRedirect(req.getContextPath() + "/directeur/conventions?action=voir&id=" + conventionId);
                return;
            }

            if ("delierEvenement".equals(action)) {
                Long conventionId = Long.parseLong(req.getParameter("conventionId"));
                Long evenementId = Long.parseLong(req.getParameter("evenementId"));
                service.delierEvenement(conventionId, evenementId);
                resp.sendRedirect(req.getContextPath() + "/directeur/conventions?action=voir&id=" + conventionId);
                return;
            }

            if ("lierDocument".equals(action)) {
                Long conventionId = Long.parseLong(req.getParameter("conventionId"));
                Long documentId = Long.parseLong(req.getParameter("documentId"));
                service.lierDocument(conventionId, documentId);
                resp.sendRedirect(req.getContextPath() + "/directeur/conventions?action=voir&id=" + conventionId);
                return;
            }

            if ("delierDocument".equals(action)) {
                Long conventionId = Long.parseLong(req.getParameter("conventionId"));
                Long documentId = Long.parseLong(req.getParameter("documentId"));
                service.delierDocument(conventionId, documentId);
                resp.sendRedirect(req.getContextPath() + "/directeur/conventions?action=voir&id=" + conventionId);
                return;
            }
        } catch (Exception e) {
            req.getSession().setAttribute("error", e.getMessage());
        }

        resp.sendRedirect(req.getContextPath() + "/directeur/conventions");
    }
}
