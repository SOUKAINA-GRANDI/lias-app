package ma.lias.app.controller.auth;

import ma.lias.app.dao.UtilisateurDAO;
import ma.lias.app.model.Utilisateur;
import ma.lias.app.util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Finalise l'inscription : le nouveau membre choisit lui-même son mot de passe
 * via un lien à durée de vie limitée (72h), reçu par email après validation
 * de sa demande d'adhésion. Remplace l'ancien envoi de mot de passe en clair.
 */
@WebServlet("/activation")
public class ActivationServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String token = request.getParameter("token");
        Utilisateur utilisateur = (token != null) ? utilisateurDAO.findByActivationToken(token) : null;

        if (utilisateur == null) {
            request.setAttribute("error", "Ce lien d'activation est invalide ou a expiré.");
            request.setAttribute("token", null);
        } else {
            request.setAttribute("token", token);
            request.setAttribute("email", utilisateur.getEmail());
        }

        request.getRequestDispatcher("/WEB-INF/views/auth/activation.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String token = request.getParameter("token");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        Utilisateur utilisateur = (token != null) ? utilisateurDAO.findByActivationToken(token) : null;

        if (utilisateur == null) {
            request.setAttribute("error", "Ce lien d'activation est invalide ou a expiré.");
            request.getRequestDispatcher("/WEB-INF/views/auth/activation.jsp")
                   .forward(request, response);
            return;
        }

        if (password == null || password.length() < 8) {
            request.setAttribute("error", "Le mot de passe doit contenir au moins 8 caractères.");
            request.setAttribute("token", token);
            request.setAttribute("email", utilisateur.getEmail());
            request.getRequestDispatcher("/WEB-INF/views/auth/activation.jsp")
                   .forward(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Les deux mots de passe ne correspondent pas.");
            request.setAttribute("token", token);
            request.setAttribute("email", utilisateur.getEmail());
            request.getRequestDispatcher("/WEB-INF/views/auth/activation.jsp")
                   .forward(request, response);
            return;
        }

        utilisateurDAO.activerCompte(utilisateur.getId(), PasswordUtil.hash(password));

        response.sendRedirect(request.getContextPath() + "/login?activated=1");
    }
}