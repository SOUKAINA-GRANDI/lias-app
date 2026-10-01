package ma.lias.app.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import ma.lias.app.model.Utilisateur;
import ma.lias.app.service.AuditService;



public class AuditFilter implements Filter {

    private final AuditService auditService =
            new AuditService();

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        try {

            // ✅ Laisser la requête s'exécuter d'abord
            chain.doFilter(request, response);

            // ✅ Récupérer la session après traitement
            HttpSession session = null;
            try {
                session = req.getSession(false);
            } catch (IllegalStateException ignored) {
                session = null;
            }

            // ✅ Vérifier sécurité
            if (session != null &&
                "POST".equalsIgnoreCase(req.getMethod())) {

                Object userObj = null;

                try {
                    userObj = session.getAttribute("user");
                } catch (IllegalStateException ignored) {
                    userObj = null;
                }

                if (userObj instanceof Utilisateur user) {

                    String uri = req.getRequestURI();
                    String contextPath = req.getContextPath();
                    String path = uri.startsWith(contextPath)
                            ? uri.substring(contextPath.length())
                            : uri;

                    // ex: /directeur/mandats -> action="mandats", entity="directeur/mandats"
                    String action = request.getParameter("action");
                    if (action == null || action.isBlank()) {
                        action = "POST";
                    }

                    Long entityId = null;
                    try {
                        String idParam = request.getParameter("id");
                        if (idParam != null && idParam.matches("\\d+")) {
                            entityId = Long.parseLong(idParam);
                        }
                    } catch (Exception ignored) {
                        entityId = null;
                    }

                    try {
                        auditService.log(user, action.toUpperCase(), path, entityId);
                    } catch (Exception auditEx) {
                        // L'audit ne doit jamais faire planter la requête utilisateur
                        auditEx.printStackTrace();
                    }
                }
            }

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}