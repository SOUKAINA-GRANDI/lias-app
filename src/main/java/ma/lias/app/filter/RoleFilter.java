package ma.lias.app.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ma.lias.app.model.Mandat;
import ma.lias.app.model.Membre;
import ma.lias.app.model.Utilisateur;
import ma.lias.app.service.MandatService;
import ma.lias.app.service.MembreService;

public class RoleFilter implements Filter {

    private final MandatService mandatService = new MandatService();
    private final MembreService membreService = new MembreService();

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String contextPath = req.getContextPath();
        String path = req.getRequestURI();

        HttpSession session = req.getSession(false);

        // ✅ Sécurité session
        if (session == null) {
            resp.sendRedirect(contextPath + "/login");
            return;
        }

        Object userObj = session.getAttribute("user");

        if (!(userObj instanceof Utilisateur user)) {
            resp.sendRedirect(contextPath + "/login");
            return;
        }

        // ✅ ADMIN
        if (path.startsWith(contextPath + "/admin/")) {

            if (!user.isAdmin()) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }

        // ✅ DIRECTEUR
      
        if (path.startsWith(contextPath + "/directeur/")) {

            // L'admin peut toujours gérer les mandats, y compris quand personne
            // n'est directeur (amorçage ou remplacement d'urgence).
            if (user.isAdmin() && path.startsWith(contextPath + "/directeur/mandats")) {
                chain.doFilter(request, response);
                return;
            }

            Membre membre = membreService.findByUserId(user.getId());
            Mandat mandatActif = mandatService.getMandatActif();

            if (mandatActif == null ||
                membre == null ||
                mandatActif.getDirecteurId() == null ||
                membre.getId() == null ||
                !mandatActif.getDirecteurId().equals(membre.getId())) {

                resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }

        // ✅ Autorisé
        chain.doFilter(request, response);
    }}