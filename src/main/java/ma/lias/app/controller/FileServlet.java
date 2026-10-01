package ma.lias.app.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ma.lias.app.config.Constantes;
import ma.lias.app.model.Mandat;
import ma.lias.app.model.Membre;
import ma.lias.app.model.Utilisateur;
import ma.lias.app.service.MandatService;
import ma.lias.app.service.MembreService;

@WebServlet(urlPatterns = {"/uploads/*", "/photos/*"})
public class FileServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final MembreService membreService = new MembreService();
    private final MandatService mandatService = new MandatService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String relativePath = request.getPathInfo();
        if (relativePath == null || relativePath.equals("/")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // 1. Anti path-traversal : le fichier doit rester DANS le dossier d'upload
        File baseDir = new File(Constantes.UPLOAD_DIR).getCanonicalFile();
        File file = new File(baseDir, relativePath).getCanonicalFile();

        if (!file.toPath().startsWith(baseDir.toPath())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        if (!file.isFile()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // 2. Contrôle d'accès selon le sous-dossier
        String relatif = baseDir.toPath().relativize(file.toPath()).toString().replace('\\', '/');
        String dossier = relatif.contains("/") ? relatif.substring(0, relatif.indexOf('/')) : "";
        boolean publique = "photos".equals(dossier);

        if (!publique) {
            Utilisateur user = utilisateurConnecte(request);
            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }
            if ("cv".equals(dossier) && !estDirectionOuAdmin(user)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            if (("pv".equals(dossier) || "conventions".equals(dossier))
                    && !(estDirectionOuAdmin(user) || estMembrePermanent(user))) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            response.setHeader("Cache-Control", "private, no-store");
        }

        // 3. Type MIME + affichage sûr (seuls PDF et images s'affichent dans le navigateur)
        String contentType = getServletContext().getMimeType(file.getName());
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        boolean affichableEnLigne = contentType.equals("application/pdf")
                || (contentType.startsWith("image/") && !contentType.contains("svg"));

        response.setContentType(contentType);
        response.setHeader("X-Content-Type-Options", "nosniff");
        if (!affichableEnLigne) {
            response.setHeader("Content-Disposition",
                    "attachment; filename=\"" + file.getName() + "\"");
        }
        response.setContentLengthLong(file.length());

        try (OutputStream out = response.getOutputStream()) {
            Files.copy(file.toPath(), out);
        }
    }

    private Utilisateur utilisateurConnecte(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object o = (session != null) ? session.getAttribute("user") : null;
        return (o instanceof Utilisateur u) ? u : null;
    }

    private boolean estDirectionOuAdmin(Utilisateur user) {
        if (user.isAdmin()) return true;
        Membre membre = membreService.findByUserId(user.getId());
        Mandat mandat = mandatService.getMandatActif();
        return membre != null && mandat != null
                && mandat.getDirecteurId() != null
                && mandat.getDirecteurId().equals(membre.getId());
    }
    private boolean estMembrePermanent(Utilisateur user) {
        Membre membre = membreService.findByUserId(user.getId());
        return membre != null && "PERMANENT".equals(membre.getStatut());
    }
}