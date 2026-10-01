package ma.lias.app.controller.directeur;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import ma.lias.app.model.Membre;
import ma.lias.app.model.Utilisateur;
import ma.lias.app.service.MembreService;
import ma.lias.app.util.FileUploadUtil;

import java.io.IOException;

@WebServlet("/directeur/profil")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,      // 1 Mo
    maxFileSize       = 2 * 1024 * 1024,  // 2 Mo
    maxRequestSize    = 5 * 1024 * 1024   // 5 Mo
)
public class DirecteurProfilServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final MembreService membreService = new MembreService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Utilisateur user = (Utilisateur) session.getAttribute("user");
        
        Membre directeur = membreService.findByUserId(user.getId());
        request.setAttribute("directeur", directeur);

        String mode = request.getParameter("mode");
        request.setAttribute("mode", mode);

        request.setAttribute("pageTitle", "Mon Profil - Directeur");
        request.setAttribute("contentPage", "/WEB-INF/views/directeur/profil.jsp");

        request.getRequestDispatcher("/WEB-INF/views/directeur/layout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Utilisateur user = (Utilisateur) session.getAttribute("user");
        Membre directeur = membreService.findByUserId(user.getId());

        if (directeur == null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String telephone = request.getParameter("telephone");
        String biographie = request.getParameter("biographie");
        String centresInteret = request.getParameter("centresInteret");

        if (nom == null || nom.isBlank() || prenom == null || prenom.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/directeur/profil?mode=edit&error=invalid");
            return;
        }

        // 💡 CORRECTION ICI : Utilisation du dossier centralisé absolu
        Part photoPart = request.getPart("photoFile");
        if (photoPart != null && photoPart.getSize() > 0) {
            String nomFichier = FileUploadUtil.sauvegarderPhoto(photoPart, ma.lias.app.config.Constantes.UPLOAD_DIR);
            if (nomFichier != null) {
                directeur.setPhoto("photos/" + nomFichier);
            }
        }

        directeur.setNom(nom.trim());
        directeur.setPrenom(prenom.trim());
        directeur.setTelephone(telephone != null ? telephone.trim() : null);
        directeur.setBiographie(biographie != null ? biographie.trim() : null);
        directeur.setCentresInteret(centresInteret != null ? centresInteret.trim() : null);

        membreService.update(directeur);

        session.setAttribute("success", "Profil Directeur mis à jour avec succès !");
        response.sendRedirect(request.getContextPath() + "/directeur/profil");
    }
}