package ma.lias.app.controller.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import ma.lias.app.dao.ParametrageDAO;
import ma.lias.app.dao.UtilisateurDAO;
import ma.lias.app.model.Utilisateur;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/admin/parametrage")
public class GestionParametrageServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(GestionParametrageServlet.class);


    private static final long serialVersionUID = 1L;
    
    private ParametrageDAO parametrageDAO;
    private UtilisateurDAO utilisateurDAO;

    @Override
    public void init() throws ServletException {
        this.parametrageDAO = new ParametrageDAO(); 
        this.utilisateurDAO = new UtilisateurDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Charger les paramètres généraux
        Map<String, String> params = parametrageDAO.findAll();
        request.setAttribute("params", params);

        // 2. Charger les utilisateurs pour le sélecteur de Direction
        List<Utilisateur> membres = utilisateurDAO.findAll();
        request.setAttribute("listeMembres", membres);

        // 3. EN COMPLÉMENT : Charger la liste des Équipes de recherche
        List<Map<String, Object>> equipes = new ArrayList<>();
        String sqlEquipes = "SELECT id, nom FROM equipe ORDER BY id ASC"; 
        try (Connection conn = ma.lias.app.dao.DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlEquipes);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> eq = new HashMap<>();
                eq.put("id", rs.getLong("id"));
                eq.put("nom", rs.getString("nom"));
                equipes.add(eq);
            }
        } catch (Exception e) {
            logger.error("Erreur technique", e);
        }
        request.setAttribute("listeEquipes", equipes);

        // Render structure layout
        request.setAttribute("pageTitle", "Paramétrage Global");
        request.setAttribute("activeMenu", "parametrage");
        request.setAttribute("contentPage", "/WEB-INF/views/admin/parametrage.jsp");

        request.getRequestDispatcher("/WEB-INF/views/admin/layout.jsp").forward(request, response);
    }
      
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        
        if ("sauvegarderParams".equals(action)) {
            try {
                String siteName = request.getParameter("site_name");
                String smtpHost = request.getParameter("smtp_host");
                String smtpPort = request.getParameter("smtp_port");
                String smtpUsername = request.getParameter("smtp_username");
                String smtpPassword = request.getParameter("smtp_password");
                
                parametrageDAO.update("site_name", siteName);
                parametrageDAO.update("smtp_host", smtpHost);
                parametrageDAO.update("smtp_port", smtpPort);
                parametrageDAO.update("smtp_username", smtpUsername);
                
                if (smtpPassword != null && !smtpPassword.trim().isEmpty()) {
                    parametrageDAO.update("smtp_password", smtpPassword);
                }
                request.getSession().setAttribute("success", "Configurations globales enregistrées avec succès.");
            } catch (Exception e) {
                request.getSession().setAttribute("error", "Erreur : " + e.getMessage());
            }
            
        } else if ("attribuerMandat".equals(action)) {
            String nouveauDirecteurIdStr = request.getParameter("directeurId");
            if (nouveauDirecteurIdStr != null && !nouveauDirecteurIdStr.trim().isEmpty()) {
                String sqlDestituerAncien = "UPDATE utilisateur SET type = 'ENSEIGNANT' WHERE type = 'DIRECTEUR'";
                String sqlNommerNouveau = "UPDATE utilisateur SET type = 'DIRECTEUR' WHERE id = ?";
                
                try (Connection conn = ma.lias.app.dao.DBConnection.getConnection()) {
                    conn.setAutoCommit(false); 
                    try (PreparedStatement ps1 = conn.prepareStatement(sqlDestituerAncien)) { ps1.executeUpdate(); }
                    try (PreparedStatement ps2 = conn.prepareStatement(sqlNommerNouveau)) {
                        ps2.setLong(1, Long.parseLong(nouveauDirecteurIdStr));
                        ps2.executeUpdate();
                    }
                    conn.commit();
                    request.getSession().setAttribute("success", "Le mandat de direction a été mis à jour avec succès.");
                } catch (Exception e) {
                    request.getSession().setAttribute("error", "Erreur lors du changement de gouvernance.");
                }
            }
            
        } else if ("ajouterEquipe".equals(action)) {
            String nomEquipe = request.getParameter("nomEquipe");
            if (nomEquipe != null && !nomEquipe.trim().isEmpty()) {
                String sqlInsereEquipe = "INSERT INTO equipe (nom) VALUES (?)";
                try (Connection conn = ma.lias.app.dao.DBConnection.getConnection();
                     PreparedStatement ps = conn.prepareStatement(sqlInsereEquipe)) {
                    ps.setString(1, nomEquipe.trim());
                    ps.executeUpdate();
                    request.getSession().setAttribute("success", "Nouvelle équipe ajoutée avec succès.");
                } catch (Exception e) {
                    logger.error("Erreur technique", e);
                    request.getSession().setAttribute("error", "Impossible d'ajouter l'équipe.");
                }
            }
            
        } else if ("updateProfilAdmin".equals(action)) {
            String adminEmail = request.getParameter("adminEmail");
            String newPassword = request.getParameter("newPassword");
            String confirmPassword = request.getParameter("confirmPassword");
            
            ma.lias.app.model.Utilisateur adminConnecte = (ma.lias.app.model.Utilisateur) request.getSession().getAttribute("utilisateur");
            
            if (adminConnecte != null) {
                try (Connection conn = ma.lias.app.dao.DBConnection.getConnection()) {
                    
                    if (newPassword != null && !newPassword.trim().isEmpty()) {
                        if (!newPassword.equals(confirmPassword)) {
                            request.getSession().setAttribute("error", "Les deux mots de passe ne correspondent pas.");
                            response.sendRedirect(request.getContextPath() + "/admin/parametrage");
                            return;
                        }
                        
                        String sql = "UPDATE utilisateur SET email = ?, mot_de_passe = ? WHERE id = ?";
                        try (PreparedStatement ps = conn.prepareStatement(sql)) {
                            ps.setString(1, adminEmail.trim());
                            ps.setString(2, newPassword.trim()); 
                            ps.setLong(3, adminConnecte.getId());
                            ps.executeUpdate();
                        }
                    } else {
                        String sql = "UPDATE utilisateur SET email = ? WHERE id = ?";
                        try (PreparedStatement ps = conn.prepareStatement(sql)) {
                            ps.setString(1, adminEmail.trim());
                            ps.setLong(2, adminConnecte.getId());
                            ps.executeUpdate();
                        }
                    }
                    
                    adminConnecte.setEmail(adminEmail.trim());
                    request.getSession().setAttribute("utilisateur", adminConnecte);
                    request.getSession().setAttribute("success", "Votre profil administrateur a été mis à jour.");
                    
                } catch (Exception e) {
                    logger.error("Erreur technique", e);
                    request.getSession().setAttribute("error", "Erreur lors de la mise à jour du profil.");
                }
            }
        }
        
        // Redirection unique et propre à la toute fin de la méthode doPost
        response.sendRedirect(request.getContextPath() + "/admin/parametrage");
    }
}