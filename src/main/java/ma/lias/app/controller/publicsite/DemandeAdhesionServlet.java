package ma.lias.app.controller.publicsite;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import ma.lias.app.config.Constantes;
import ma.lias.app.dao.DemandeAdhesionDAO;
import ma.lias.app.dao.EquipeDAO;
import ma.lias.app.enums.StatutDemande;
import ma.lias.app.model.DemandeAdhesion;
import ma.lias.app.util.FileUploadUtil;
import ma.lias.app.util.ValidationUtil;
import ma.lias.app.util.EmailUtil;
import ma.lias.app.service.DemandeAdhesionService;

@WebServlet("/public/demande-adhesion")
@MultipartConfig
public class DemandeAdhesionServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String ADMIN_EMAIL = "lias.club.app@gmail.com";

    private final DemandeAdhesionDAO dao = new DemandeAdhesionDAO();
    private final EquipeDAO equipeDAO = new EquipeDAO();
    private final DemandeAdhesionService demandeService = new DemandeAdhesionService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ✅ Charger les équipes pour le select
        request.setAttribute("equipes", equipeDAO.findAll());

        request.setAttribute("pageTitle", "Demande d'adhésion");
        request.setAttribute("contentPage", "/WEB-INF/views/public/demande-adhesion.jsp");

        request.getRequestDispatcher("/WEB-INF/views/public/layout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String email = request.getParameter("email");
        String motivation = request.getParameter("motivation");
        String statut = request.getParameter("statut");
        String etablissement = request.getParameter("etablissement");
        String equipeId = request.getParameter("equipeId");

        // ✅ Validation des champs
        if (ValidationUtil.isEmpty(nom)
                || ValidationUtil.isEmpty(prenom)
                || !ValidationUtil.isValidEmail(email)
                || ValidationUtil.isEmpty(motivation)
                || ValidationUtil.isEmpty(statut)) {

            response.sendRedirect(request.getContextPath() + "/public/demande-adhesion?error=invalid");
            return;
        }

        // ✅ Statut visé : liste blanche (jamais RETRAITE / ANCIEN)
        if (!java.util.List.of("PERMANENT", "ASSOCIE", "DOCTORANT").contains(statut)) {
            response.sendRedirect(request.getContextPath() + "/public/demande-adhesion?error=invalid");
            return;
        }

        DemandeAdhesion d = new DemandeAdhesion();
        d.setNom(nom.trim());
        d.setPrenom(prenom.trim());
        d.setEmail(email.trim());
        d.setMotivation(motivation.trim());
        d.setStatut(StatutDemande.EN_ATTENTE.name());
        d.setDateDemande(LocalDateTime.now());

        d.setStatutVise(statut);
        d.setEtablissement(ValidationUtil.isEmpty(etablissement) ? null : etablissement.trim());

        if (!ValidationUtil.isEmpty(equipeId) && equipeId.trim().matches("\\d+")) {
            Long eqId = Long.parseLong(equipeId.trim());
            if (equipeDAO.findById(eqId) != null) {   // l'équipe doit exister
                d.setEquipeId(eqId);
            }
        }

        // ✅ UPLOAD CV PHYSIQUE SUR C:/lias_uploads/cv/
        Part cvPart = request.getPart("cv");

        if (cvPart != null && cvPart.getSize() > 0) {

            if (cvPart.getSize() > Constantes.MAX_FILE_SIZE) {
                response.sendRedirect(request.getContextPath() + "/public/demande-adhesion?error=fileTooLarge");
                return;
            }

            if (!"application/pdf".equals(cvPart.getContentType())) {
                response.sendRedirect(request.getContextPath() + "/public/demande-adhesion?error=invalidFile");
                return;
            }

            if (!FileUploadUtil.isValidPDF(cvPart)) {
                response.sendRedirect(request.getContextPath() + "/public/demande-adhesion?error=invalidFile");
                return;
            }

            // 📂 Dossier d'upload centralisé et portable (voir Constantes.UPLOAD_DIR)
            String uploadPath = ma.lias.app.config.Constantes.UPLOAD_DIR + "cv/";

            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs(); // Crée le dossier cv/ s'il n'existe pas
            }

            String safeFileName = UUID.randomUUID() + ".pdf";

            // Écriture directe sur le disque dur Windows
            cvPart.write(uploadPath + safeFileName);

            // On sauvegarde le chemin relatif uniforme en BDD
            d.setCvPath("uploads/cv/" + safeFileName);
        }

        // ✅ Sauvegarde en Base de données
        dao.save(d);

        // ✅ Notifier le directeur en mandat qu'une nouvelle demande est arrivée
        demandeService.notifierNouvelleDemande(d);

        // ✅ Lien propre généré pour l'email du Directeur/Admin
        String lienCvHtml = "";
        if (d.getCvPath() != null && !d.getCvPath().isEmpty()) {
            String parts[] = d.getCvPath().split("/");
            String filename = parts[parts.length - 1];
            String fullUrl = Constantes.APP_BASE_URL + "/uploads/cv/" + filename;

            lienCvHtml = "<p style='margin-top: 15px;'>"
                       + "<strong>Document joint :</strong><br/>"
                       + "<a href='" + fullUrl + "' target='_blank' style='display: inline-block; background-color: #00965e; color: #ffffff; padding: 10px 18px; font-size: 13px; font-weight: 600; text-decoration: none; border-radius: 6px; margin-top: 5px;'>"
                       + "📄 Consulter le CV PDF en ligne"
                       + "</a>"
                       + "</p>";
        } else {
            lienCvHtml = "<p style='color: #94a3b8; font-style: italic;'>Aucun CV n'a été déposé.</p>";
        }

        // ✅ Envoi des emails de notification
        EmailUtil.sendHtmlEmail(
                ADMIN_EMAIL,
                "Nouvelle demande d'adhésion - LIAS",
                "<div style='font-family: Arial, sans-serif; color: #1e293b; max-width: 600px; padding: 20px; border: 1px solid #e2e8f0; border-radius: 8px;'>"
                        + "<h3 style='color: #0f172a;'>Une nouvelle demande d'adhésion a été reçue</h3>"
                        + "<hr style='border: 0; border-top: 1px solid #e2e8f0; margin-bottom: 15px;' />"
                        + "<p><strong>Nom :</strong> " + nom + "</p>"
                        + "<p><strong>Prénom :</strong> " + prenom + "</p>"
                        + "<p><strong>Email :</strong> " + email + "</p>"
                        + "<p><strong>Motivation :</strong> " + motivation + "</p>"
                        + lienCvHtml
                        + "</div>"
        );

        EmailUtil.sendHtmlEmail(
                email,
                "Votre demande d'adhésion - LIAS",
                "<p>Bonjour " + prenom + ",</p>"
                        + "<p>Votre demande d'adhésion a bien été reçue et sera traitée prochainement.</p>"
                        + "<br/>Cordialement,<br/>Laboratoire LIAS"
        );

        response.sendRedirect(request.getContextPath() + "/public/demande-adhesion?success=true");
    }
}