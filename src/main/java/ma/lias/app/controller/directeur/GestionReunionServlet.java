package ma.lias.app.controller.directeur;

import ma.lias.app.model.Reunion;
import ma.lias.app.service.ReunionService;
import ma.lias.app.config.Constantes;
import ma.lias.app.exception.BusinessException;
import ma.lias.app.service.NotificationService;
import ma.lias.app.util.FileUploadUtil;

import jakarta.servlet.annotation.MultipartConfig;

import java.util.List;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDate;


@WebServlet("/directeur/reunions")
@MultipartConfig(
	    fileSizeThreshold = 1024 * 1024,       // 1 Mo
	    maxFileSize       = 10 * 1024 * 1024,  // 10 Mo
	    maxRequestSize    = 12 * 1024 * 1024   // 12 Mo
	)
public class GestionReunionServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final ReunionService service = new ReunionService();
    private final NotificationService notificationService = new NotificationService();
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "liste";

        switch (action) {

            case "nouveau":
                request.setAttribute("contentPage",
                        "/WEB-INF/views/directeur/reunion-form.jsp");
                break;

            case "edit":
                Long id =
                        Long.parseLong(request.getParameter("id"));
                request.setAttribute("reunion",
                        service.findById(id));
                request.setAttribute("contentPage",
                        "/WEB-INF/views/directeur/reunion-form.jsp");
                break;

            default:
                request.setAttribute("reunions",
                        service.findAll());
                request.setAttribute("contentPage",
                        "/WEB-INF/views/directeur/reunions-content.jsp");
        }

        request.getRequestDispatcher(
                "/WEB-INF/views/directeur/layout.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {

        String action = request.getParameter("action");

        try {

            // ── AJOUTER une réunion (PV facultatif à la création) ──
            if ("ajouter".equals(action)) {
                Reunion r = new Reunion();
                r.setTitre(request.getParameter("titre"));
                r.setOrdreDuJour(request.getParameter("ordreDuJour"));
                r.setDateReunion(LocalDate.parse(request.getParameter("dateReunion")));

                String pv = lirePv(request);
                r.setPvPath(pv != null ? pv : "");
                service.create(r);

                if (pv != null) notifierPv(r);
            }

            // ── MODIFIER (un nouveau fichier remplace le PV, sinon on garde l'ancien) ──
            if ("modifier".equals(action)) {
                Long id = Long.parseLong(request.getParameter("id"));
                Reunion r = service.findById(id);

                if (r != null) {
                    r.setTitre(request.getParameter("titre"));
                    r.setOrdreDuJour(request.getParameter("ordreDuJour"));
                    r.setDateReunion(LocalDate.parse(request.getParameter("dateReunion")));

                    String pv = lirePv(request);
                    if (pv != null) r.setPvPath(pv);

                    service.update(r);

                    if (pv != null) notifierPv(r);
                }
            }

            // ── ARCHIVER ──
            if ("archiver".equals(action)) {
                Long id = Long.parseLong(request.getParameter("id"));
                service.archive(id);
            }

        } catch (Exception e) {
            request.getSession().setAttribute("error", e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/directeur/reunions");
    }

    /** Enregistre le fichier PV envoyé. Retourne son chemin, ou null si aucun fichier n'a été choisi. */
    private String lirePv(HttpServletRequest request) throws IOException, ServletException {

        Part fichier = request.getPart("pv");
        if (fichier == null || fichier.getSize() == 0) return null;

        String chemin = FileUploadUtil.sauvegarderDocument(fichier, Constantes.UPLOAD_DIR, "pv");
        if (chemin == null) {
            throw new BusinessException(
                    "PV refusé : formats acceptés PDF, Word, Excel, JPG ou PNG (10 Mo maximum).");
        }
        return chemin;
    }

    private void notifierPv(Reunion r) {
        notificationService.notifierStatuts(List.of("PERMANENT"),
                "Le procès-verbal de la réunion « " + r.getTitre() + " » est disponible.");
    }
}