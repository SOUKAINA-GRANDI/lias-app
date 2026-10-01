package ma.lias.app.controller.membre;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import ma.lias.app.model.Membre;
import ma.lias.app.model.Utilisateur;
import ma.lias.app.service.MembreService;
import ma.lias.app.util.FileUploadUtil;
import ma.lias.app.service.AuditService;

import java.io.IOException;

@WebServlet("/membre/profil/modifier")
@MultipartConfig(
	    fileSizeThreshold = 1024 * 1024,      // 1 Mo en mémoire
	    maxFileSize       = 2 * 1024 * 1024,  // 2 Mo max par fichier
	    maxRequestSize    = 5 * 1024 * 1024   // 5 Mo max total
	)
public class ModifierProfilServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final MembreService membreService = new MembreService();
    private final AuditService auditService = new AuditService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Utilisateur user = (Utilisateur) session.getAttribute("user");

        // On recharge le membre complet depuis la BDD pour conserver ses infos intactes (Rôle, Équipe, etc.)
        Membre membre = membreService.findByUserId(user.getId());

        if (membre == null) {
            response.sendRedirect(request.getContextPath() + "/membre/dashboard");
            return;
        }

        // ✅ Validation des champs obligatoires
        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");

        if (nom == null || nom.isBlank() || prenom == null || prenom.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/membre/profil?error=invalid");
            return;
        }
        
        Part photoPart = request.getPart("photoFile");
        if (photoPart != null && photoPart.getSize() > 0) {
            // Dossier d'upload centralisé et portable (voir Constantes.UPLOAD_DIR)
            String dossierUpload = ma.lias.app.config.Constantes.UPLOAD_DIR; 
            
            String nomFichier = FileUploadUtil.sauvegarderPhoto(photoPart, dossierUpload);
            if (nomFichier != null) {
                membre.setPhoto("photos/" + nomFichier);
            }
        }

        // ✅ Récupération des données éditables depuis le formulaire
        String telephone = request.getParameter("telephone");
        String biographie = request.getParameter("biographie");
        String centresInteret = request.getParameter("centresInteret");

        // ✅ Application des modifications sur l'objet chargé
        membre.setNom(nom.trim());
        membre.setPrenom(prenom.trim());
        membre.setTelephone(telephone != null ? telephone.trim() : null);
        membre.setBiographie(biographie != null ? biographie.trim() : null);
        membre.setCentresInteret(centresInteret != null ? centresInteret.trim() : null);

        // ✅ Sauvegarde sécurisée (aucune donnée système n'est écrasée)
        membreService.update(membre);

        // ✅ Audit
        auditService.log(user, "UPDATE", "PROFIL_MEMBRE", membre.getId());
        
        // Notification de succès via session pour éviter de perdre le message au rechargement
        session.setAttribute("success", "Profil mis à jour avec succès !");
        
        response.sendRedirect(request.getContextPath() + "/membre/profil");
    }
}