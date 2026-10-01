package ma.lias.app.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Protection CSRF minimale mais efficace :
 *  - Un token aléatoire est généré une fois par session et stocké côté serveur.
 *  - Exposé aux JSP via l'attribut de requête "csrfToken" (utilisé dans une
 *    balise <meta> par les layouts, puis injecté automatiquement dans tous
 *    les formulaires POST par assets/js/csrf.js).
 *  - Toute requête POST vers les zones authentifiées doit renvoyer ce même
 *    token (paramètre "csrfToken"), sinon la requête est rejetée (403).
 *
 * Mappé sur /admin/*, /directeur/*, /membre/*, /messages dans web.xml,
 * après AuthFilter.
 */
public class CsrfFilter implements Filter {

    private static final String SESSION_KEY = "csrfToken";
    private final SecureRandom random = new SecureRandom();

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        HttpSession session = request.getSession(true);
        String token = (String) session.getAttribute(SESSION_KEY);
        if (token == null) {
            token = genererToken();
            session.setAttribute(SESSION_KEY, token);
        }

        // Toujours exposé aux JSP, y compris sur GET (pour remplir la balise <meta>)
        request.setAttribute("csrfToken", token);

        if ("POST".equalsIgnoreCase(request.getMethod())) {
            String soumis = request.getParameter("csrfToken");
            if (soumis == null || !constantTimeEquals(soumis, token)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "Jeton de sécurité invalide ou expiré. Merci de recharger la page et réessayer.");
                return;
            }
        }

        chain.doFilter(req, res);
    }

    private String genererToken() {
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    /** Comparaison en temps constant pour éviter les attaques par timing. */
    private boolean constantTimeEquals(String a, String b) {
        if (a.length() != b.length()) return false;
        int result = 0;
        for (int i = 0; i < a.length(); i++) {
            result |= a.charAt(i) ^ b.charAt(i);
        }
        return result == 0;
    }
}
