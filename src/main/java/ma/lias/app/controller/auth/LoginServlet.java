package ma.lias.app.controller.auth;

import ma.lias.app.model.Utilisateur;
import ma.lias.app.model.Membre;
import ma.lias.app.model.Mandat;
import ma.lias.app.config.Constantes;
import ma.lias.app.service.AuthService;
import ma.lias.app.service.MembreService;
import ma.lias.app.service.MandatService;
import ma.lias.app.util.LoginAttemptTracker;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final AuthService authService = new AuthService();
    private final MembreService membreService = new MembreService();
    private final MandatService mandatService = new MandatService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

    	request.getRequestDispatcher(
    	        "/WEB-INF/views/auth/layout-auth.jsp")
    	        .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // ✅ Validation
        if (email == null || password == null ||
            email.isBlank() || password.isBlank()) {

            request.setAttribute("error",
                    "Veuillez remplir tous les champs");

            request.getRequestDispatcher(
                    "/WEB-INF/views/auth/layout-auth.jsp")
                    .forward(request, response);
            return;
        }

        email = email.trim();

        if (LoginAttemptTracker.estBloque(email)) {
            request.setAttribute("error",
                    "Trop de tentatives échouées. Réessayez dans "
                            + LoginAttemptTracker.minutesRestantes(email) + " minute(s).");

            request.getRequestDispatcher(
                    "/WEB-INF/views/auth/layout-auth.jsp")
                    .forward(request, response);
            return;
        }

        Utilisateur user = authService.authenticate(email, password);

        if (user == null) {

            LoginAttemptTracker.enregistrerEchec(email);

            String messageErreur = "Email ou mot de passe incorrect";

            try {
                ma.lias.app.dao.UtilisateurDAO utilisateurDAO = new ma.lias.app.dao.UtilisateurDAO();

                if (utilisateurDAO.activationEnAttente(email)) {
                    messageErreur = "Ce compte n'est pas encore activé. "
                            + "Vérifiez l'email d'activation envoyé lors de la validation de votre adhésion.";

                } else {
                    // Le mot de passe est-il correct sur un compte désactivé ?
                    // (on ne le vérifie qu'APRÈS un échec normal, jamais avant : pas de fuite d'info supplémentaire)
                    Utilisateur compteInactif = authService.authenticateInclusInactif(email, password);

                    if (compteInactif != null && !compteInactif.isActif()) {

                        Membre membreInactif = membreService.findByUtilisateurId(compteInactif.getId());

                        if (membreInactif != null && "RETRAITE".equals(membreInactif.getStatut())) {
                            messageErreur = "Votre compte est gelé (statut retraité). "
                                    + "Contactez le directeur du laboratoire pour plus d'informations.";

                        } else if (membreInactif != null && "ANCIEN".equals(membreInactif.getStatut())) {
                            messageErreur = "Ce compte a été désactivé (ancien membre). "
                                    + "Contactez le directeur si vous pensez qu'il s'agit d'une erreur.";

                        } else {
                            messageErreur = "Ce compte est désactivé. Contactez l'administrateur.";
                        }
                    }
                }

            } catch (Exception ignored) {
                // on garde le message générique par défaut
            }

            request.setAttribute("error", messageErreur);

            request.getRequestDispatcher(
                    "/WEB-INF/views/auth/layout-auth.jsp")
                    .forward(request, response);
            return;
        }

        // ✅ Protection session fixation
        LoginAttemptTracker.reinitialiser(email);
        request.getSession().invalidate();
        HttpSession session = request.getSession(true);

        session.setAttribute("user", user);
        session.setMaxInactiveInterval(Constantes.SESSION_TIMEOUT);

        response.setHeader("Cache-Control",
                "no-cache, no-store, must-revalidate");

        // ✅ ADMIN PRIORITAIRE
        if (user.isAdmin()) {
            response.sendRedirect(
                    request.getContextPath()
                            + "/admin/dashboard");
            return;
        }

        // ✅ RÉCUPÉRER MEMBRE ICI
        Membre membre = membreService.findByUserId(user.getId());

        if (membre == null) {
            request.setAttribute("error",
                "Aucun profil membre associé à ce compte");

            request.getRequestDispatcher(
                "/WEB-INF/views/auth/layout-auth.jsp")
                .forward(request, response);
            return;
        }

        // ✅ VÉRIFIER DIRECTEUR
        Mandat mandatActif = mandatService.getMandatActif();

        if (mandatActif != null &&
            mandatActif.getDirecteurId() != null &&
            mandatActif.getDirecteurId().equals(membre.getId())) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/directeur/dashboard");
            return;
        }

        // ✅ MEMBRE STANDARD
        response.sendRedirect(
                request.getContextPath()
                        + "/membre/dashboard");
    }
    
   }