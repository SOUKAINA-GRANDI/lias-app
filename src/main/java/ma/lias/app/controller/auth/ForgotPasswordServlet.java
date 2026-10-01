package ma.lias.app.controller.auth;

import ma.lias.app.config.Constantes;
import ma.lias.app.dao.UtilisateurDAO;
import ma.lias.app.model.Utilisateur;
import ma.lias.app.util.EmailUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@WebServlet("/mot-de-passe-oublie")
public class ForgotPasswordServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/WEB-INF/views/auth/mot-de-passe-oublie.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");

        if (email != null && !email.isBlank()) {

            Utilisateur utilisateur = utilisateurDAO.findByEmail(email.trim());

            // On envoie le lien seulement si le compte existe ET est actif
            // (un compte désactivé/retraité ne doit pas pouvoir se réactiver seul).
            if (utilisateur != null && utilisateur.isActif()) {

                String token = UUID.randomUUID().toString().replace("-", "");
                utilisateurDAO.definirTokenReset(utilisateur.getId(), token,
                        LocalDateTime.now().plusHours(1));

                String lien = Constantes.APP_BASE_URL + "/reinitialiser-mot-de-passe?token=" + token;

                String sujet = "Réinitialisation de votre mot de passe - LIAS";
                String html = "<p>Bonjour,</p>"
                        + "<p>Une demande de réinitialisation de mot de passe a été faite pour ce compte.</p>"
                        + "<p><a href='" + lien + "' style='display:inline-block; background-color:#0F172A; "
                        + "color:#fff; padding:10px 18px; border-radius:6px; text-decoration:none;'>"
                        + "Réinitialiser mon mot de passe</a></p>"
                        + "<p style='color:#94a3b8; font-size:13px;'>Ce lien expire dans 1 heure. "
                        + "Si vous n'êtes pas à l'origine de cette demande, ignorez simplement cet email.</p>";

                try {
                    EmailUtil.sendHtmlEmail(utilisateur.getEmail(), sujet, html);
                } catch (Exception ignored) {
                    // On ne révèle jamais un échec technique à l'utilisateur ici,
                    // le message reste générique dans tous les cas (voir plus bas).
                }
            }
        }

        // ⚠️ Message IDENTIQUE que l'email existe ou non : on ne confirme jamais
        // à un visiteur qu'une adresse est enregistrée dans la base.
        request.setAttribute("messageEnvoye", true);
        request.getRequestDispatcher("/WEB-INF/views/auth/mot-de-passe-oublie.jsp")
               .forward(request, response);
    }
}