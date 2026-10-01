package ma.lias.app.controller.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@WebServlet("/admin/profil")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,      // 1 Mo en mémoire
    maxFileSize       = 2 * 1024 * 1024,  // 2 Mo max par fichier
    maxRequestSize    = 5 * 1024 * 1024   // 5 Mo max total
)
public class GestionProfilServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(GestionProfilServlet.class);

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        ma.lias.app.model.Utilisateur userConnecte = (ma.lias.app.model.Utilisateur) request.getSession().getAttribute("user");
        
        if (userConnecte == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Map<String, Object> membreData = new HashMap<>();
        String sql = "SELECT * FROM membre WHERE utilisateur_id = ?";
        
        try (Connection conn = ma.lias.app.dao.DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userConnecte.getId());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    membreData.put("nom", rs.getString("nom"));
                    membreData.put("prenom", rs.getString("prenom"));
                    membreData.put("email", rs.getString("email"));
                    membreData.put("telephone", rs.getString("telephone"));
                    membreData.put("biographie", rs.getString("biographie"));
                    membreData.put("centres_interet", rs.getString("centres_interet"));
                    membreData.put("date_naissance", rs.getDate("date_naissance"));
                    membreData.put("photo", rs.getString("photo"));
                    membreData.put("statut", rs.getString("statut"));
                    membreData.put("etablissement_origine", rs.getString("etablissement_origine"));
                    membreData.put("laboratoire_origine", rs.getString("laboratoire_origine"));
                    membreData.put("date_embauche", rs.getDate("date_embauche"));
                    membreData.put("date_affiliation", rs.getDate("date_affiliation"));
                    membreData.put("date_depart", rs.getDate("date_depart"));
                    membreData.put("role", rs.getString("role"));
                    membreData.put("actif", rs.getInt("actif"));
                } else {
                    membreData.put("email", userConnecte.getEmail());
                    membreData.put("nom", "");
                    membreData.put("prenom", "");
                    membreData.put("statut", "PERMANENT");
                    membreData.put("actif", 1);
                }
            }
        } catch (Exception e) {
            logger.error("Erreur technique", e);
        }

        request.setAttribute("membre", membreData);
        request.setAttribute("pageTitle", "Mon Profil");
        request.setAttribute("activeMenu", "profil");
        request.setAttribute("contentPage", "/WEB-INF/views/admin/profil.jsp");

        request.getRequestDispatcher("/WEB-INF/views/admin/layout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        
        ma.lias.app.model.Utilisateur userConnecte = (ma.lias.app.model.Utilisateur) session.getAttribute("user");

        if (userConnecte == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");

        if ("updateInfos".equals(action)) {
            String nom = request.getParameter("nom");
            String prenom = request.getParameter("prenom");
            String email = request.getParameter("email");
            String telephone = request.getParameter("telephone");
            String dateNaissance = request.getParameter("date_naissance");
            String biographie = request.getParameter("biographie");
            String centresInteret = request.getParameter("centres_interet");
            String etablissementOrigine = request.getParameter("etablissement_origine");
            String laboratoryOrigine = request.getParameter("laboratoire_origine");
            String dateEmbauche = request.getParameter("date_embauche");
            String dateAffiliation = request.getParameter("date_affiliation");
            String dateDepart = request.getParameter("date_depart");

            String photoValueInDB = request.getParameter("current_photo"); 
            Part filePart = request.getPart("photoFile"); 
            
            if (filePart != null && filePart.getSize() > 0) {
                String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                String extension = fileName.substring(fileName.lastIndexOf("."));
                String nomFichierUnique = UUID.randomUUID().toString() + extension;
                
                // Alignement strict sur la racine lias_uploads demandée par FileServlet
                String dossierUpload = ma.lias.app.config.Constantes.UPLOAD_DIR; 
                File uploadDir = new File(dossierUpload);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                
                // Sauvegarde physique directe dans le dossier d'upload configuré
                filePart.write(dossierUpload + nomFichierUnique);
                
                // Valeur en BDD identique au comportement du membre standard : "photos/nom.jpg"
                photoValueInDB = "photos/" + nomFichierUnique; 
            }

            try (Connection conn = ma.lias.app.dao.DBConnection.getConnection()) {
                conn.setAutoCommit(false);

                try {
                    String sqlCheck = "SELECT COUNT(*) FROM membre WHERE utilisateur_id = ?";
                    boolean existe = false;
                    try (PreparedStatement psCheck = conn.prepareStatement(sqlCheck)) {
                        psCheck.setLong(1, userConnecte.getId());
                        try (ResultSet rsCheck = psCheck.executeQuery()) {
                            if (rsCheck.next() && rsCheck.getInt(1) > 0) {
                                existe = true;
                            }
                        }
                    }

                    if (existe) {
                        String sqlUpdate = "UPDATE membre SET nom=?, prenom=?, email=?, telephone=?, date_naissance=?, biographie=?, centres_interet=?, etablissement_origine=?, laboratoire_origine=?, date_embauche=?, date_affiliation=?, date_depart=?, photo=?, date_modification=NOW() WHERE utilisateur_id=?";
                        try (PreparedStatement ps = conn.prepareStatement(sqlUpdate)) {
                            ps.setString(1, nom);
                            ps.setString(2, prenom);
                            ps.setString(3, email);
                            ps.setString(4, telephone);
                            ps.setDate(5, (dateNaissance != null && !dateNaissance.isEmpty()) ? java.sql.Date.valueOf(dateNaissance) : null);
                            ps.setString(6, biographie);
                            ps.setString(7, centresInteret);
                            ps.setString(8, etablissementOrigine);
                            ps.setString(9, laboratoryOrigine);
                            ps.setDate(10, (dateEmbauche != null && !dateEmbauche.isEmpty()) ? java.sql.Date.valueOf(dateEmbauche) : null);
                            ps.setDate(11, (dateAffiliation != null && !dateAffiliation.isEmpty()) ? java.sql.Date.valueOf(dateAffiliation) : null);
                            ps.setDate(12, (dateDepart != null && !dateDepart.isEmpty()) ? java.sql.Date.valueOf(dateDepart) : null);
                            ps.setString(13, photoValueInDB);
                            ps.setLong(14, userConnecte.getId());
                            ps.executeUpdate();
                        }
                    } else {
                        String sqlInsert = "INSERT INTO membre (utilisateur_id, nom, prenom, email, telephone, date_naissance, biographie, centres_interet, etablissement_origine, laboratoire_origine, date_embauche, date_affiliation, date_depart, photo, statut, actif, date_creation) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'PERMANENT', 1, NOW())";
                        try (PreparedStatement ps = conn.prepareStatement(sqlInsert)) {
                            ps.setLong(1, userConnecte.getId());
                            ps.setString(2, nom);
                            ps.setString(3, prenom);
                            ps.setString(4, email);
                            ps.setString(5, telephone);
                            ps.setDate(6, (dateNaissance != null && !dateNaissance.isEmpty()) ? java.sql.Date.valueOf(dateNaissance) : null);
                            ps.setString(7, biographie);
                            ps.setString(8, centresInteret);
                            ps.setString(9, etablissementOrigine);
                            ps.setString(10, laboratoryOrigine);
                            ps.setDate(11, (dateEmbauche != null && !dateEmbauche.isEmpty()) ? java.sql.Date.valueOf(dateEmbauche) : null);
                            ps.setDate(12, (dateAffiliation != null && !dateAffiliation.isEmpty()) ? java.sql.Date.valueOf(dateAffiliation) : null);
                            ps.setDate(13, (dateDepart != null && !dateDepart.isEmpty()) ? java.sql.Date.valueOf(dateDepart) : null);
                            ps.setString(14, photoValueInDB);
                            ps.executeUpdate();
                        }
                    }

                    String sqlUtilisateur = "UPDATE utilisateur SET email=? WHERE id=?";
                    try (PreparedStatement ps2 = conn.prepareStatement(sqlUtilisateur)) {
                        ps2.setString(1, email);
                        ps2.setLong(2, userConnecte.getId());
                        ps2.executeUpdate();
                    }

                    conn.commit();
                    userConnecte.setEmail(email);
                    session.setAttribute("user", userConnecte);
                    session.setAttribute("success", "Profil et photo enregistrés avec succès !");

                } catch (Exception innerException) {
                    conn.rollback();
                    logger.error("Erreur technique", innerException);
                    session.setAttribute("error", "Erreur lors de l'enregistrement : " + innerException.getMessage());
                }
            } catch (Exception e) {
                logger.error("Erreur technique", e);
                session.setAttribute("error", "Erreur réseau base de données.");
            }

        } else if ("updatePassword".equals(action)) {
            String newPass = request.getParameter("newPassword");
            String confirmPass = request.getParameter("confirmPassword");

            if (newPass == null || newPass.trim().isEmpty() || !newPass.equals(confirmPass)) {
                session.setAttribute("error", "Les mots de passe ne correspondent pas.");
            } else {
                String sqlPass = "UPDATE utilisateur SET mot_de_passe=? WHERE id=?";
                try (Connection conn = ma.lias.app.dao.DBConnection.getConnection();
                     PreparedStatement ps = conn.prepareStatement(sqlPass)) {
                    ps.setString(1, newPass.trim());
                    ps.setLong(2, userConnecte.getId());
                    ps.executeUpdate();
                    session.setAttribute("success", "Mot de passe modifié avec succès.");
                } catch (Exception e) {
                    logger.error("Erreur technique", e);
                    session.setAttribute("error", "Impossible de changer le mot de passe.");
                }
            }
        }

        response.sendRedirect(request.getContextPath() + "/admin/profil");
    }
}