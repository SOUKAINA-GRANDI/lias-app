package ma.lias.app.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ma.lias.app.enums.StatutMembre;
import ma.lias.app.model.Membre;
import ma.lias.app.model.Utilisateur;
import ma.lias.app.service.MembreService;

import java.io.IOException;
import java.util.List;

/**
 * CDC §2 - Typologie des utilisateurs :
 *  - Permanent  : accès complet selon rôle (aucune restriction ici).
 *  - Associé    : accès limité (consultation, participation à certains événements,
 *                 publications selon droits) -> pas d'accès matériel/conventions/
 *                 réunions/gestion d'équipe (modules de gouvernance interne).
 *  - Doctorant  : accès limité aux publications (ajout/consultation) uniquement,
 *                 aucun accès aux autres modules internes.
 *  - Retraité / Ancien : déjà bloqués en amont (compte désactivé -> LoginServlet
 *                 refuse la connexion), ce filtre ne les concerne donc pas en pratique.
 *
 * Ne s'applique qu'aux membres (pas aux admins), sur les routes /membre/* et /messages.
 * Ne remplace pas AuthFilter/RoleFilter : à placer après eux dans web.xml.
 */
public class StatutAccessFilter implements Filter {

    private final MembreService membreService = new MembreService();

    // Modules réservés aux membres pleinement intégrés (gouvernance/collaboration interne)
    private static final List<String> MODULES_INTERNES = List.of(
            "/membre/materiel", "/membre/conventions", "/membre/reunions",
            "/membre/pv", "/membre/equipe"
    );

    // Ce que le Doctorant peut consulter (CDC §2.2.C : uniquement les publications, + son profil)
    private static final List<String> AUTORISE_DOCTORANT = List.of(
            "/membre/dashboard", "/membre/profil", "/membre/profil/modifier",
            "/membre/publications",
            "/membre/notifications", "/membre/notifications/read"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String contextPath = req.getContextPath();
        String path = req.getRequestURI().substring(contextPath.length());

        HttpSession session = req.getSession(false);
        Object userObj = (session != null) ? session.getAttribute("user") : null;

        if (!(userObj instanceof Utilisateur user) || user.isAdmin()) {
            // Pas de membre connecté ou admin -> pas concerné par cette règle
            chain.doFilter(request, response);
            return;
        }

        Membre membre = membreService.findByUserId(user.getId());
        if (membre == null || membre.getStatut() == null) {
            chain.doFilter(request, response);
            return;
        }

        StatutMembre statut;
        try {
            statut = StatutMembre.valueOf(membre.getStatut());
        } catch (IllegalArgumentException e) {
            chain.doFilter(request, response);
            return;
        }

        // Exposé aux JSP (sidebar) pour masquer les liens vers des modules non autorisés
        req.setAttribute("statutMembreConnecte", statut.name());

        switch (statut) {

            case DOCTORANT:
                // ❌ Aucun accès aux modules internes : liste blanche stricte
                if (AUTORISE_DOCTORANT.stream().noneMatch(path::equals)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
                break;

            case ASSOCIE:
                // Accès limité : pas de matériel/conventions/réunions/gestion d'équipe
                if (MODULES_INTERNES.stream().anyMatch(path::startsWith)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
                break;

            default:
                // PERMANENT (et autres) : accès complet selon rôle
                break;
        }

        chain.doFilter(request, response);
    }
}