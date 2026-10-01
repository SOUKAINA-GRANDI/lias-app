package ma.lias.app.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import ma.lias.app.model.Utilisateur;
import ma.lias.app.service.NotificationService;

public class NotificationCountFilter implements Filter {

    private final NotificationService notificationService = new NotificationService();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession(false);

        if (session != null && session.getAttribute("user") != null) {
            Utilisateur user = (Utilisateur) session.getAttribute("user");
            
            // On récupère le compte exact des notifications non lues
            int nbNonLuesGlobal = notificationService.countUnread(user.getId());
            
            // On le stocke en requête pour qu'il soit accessible par la Topbar et Sidebar
            request.setAttribute("globalNbNonLues", nbNonLuesGlobal);
        }

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}