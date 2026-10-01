package ma.lias.app.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import ma.lias.app.config.Constantes;
import ma.lias.app.dao.AuditLogDAO;
import ma.lias.app.model.AuditLog;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/audit")
public class GestionAuditServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final AuditLogDAO dao = new AuditLogDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String action = request.getParameter("action");
        String entity = request.getParameter("entity");

        int page = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;
        int limit = 10;
        try { limit = Constantes.AUDIT_PAGE_SIZE; } catch (Exception e) {}
        
        int offset = (page - 1) * limit;
        List<AuditLog> logs;
        int totalRecords = 0;

        if (email != null && !email.isBlank()) {
            logs = dao.findByEmailPaginated(email.trim(), offset, limit);
            totalRecords = dao.count(email.trim(), null, null);
        } else if (action != null && !action.isBlank()) {
            logs = dao.findByActionPaginated(action.trim(), offset, limit);
            totalRecords = dao.findAll().size();
        } else if (entity != null && !entity.isBlank()) {
            logs = dao.findByEntityPaginated(entity.trim(), offset, limit);
            totalRecords = dao.findAll().size();
        } else {
            logs = dao.findPaginated(offset, limit);
            totalRecords = dao.count(null, null, null);
        }

        int totalPages = (int) Math.ceil((double) totalRecords / limit);

        request.setAttribute("logs", logs);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages > 0 ? totalPages : 1);
        request.setAttribute("searchEmail", email);
        request.setAttribute("searchAction", action);
        request.setAttribute("searchEntity", entity);

        request.setAttribute("pageTitle", "Journal d'Audit & Sécurité");
        request.setAttribute("activeMenu", "audit");
        request.setAttribute("contentPage", "/WEB-INF/views/admin/audit.jsp");

        request.getRequestDispatcher("/WEB-INF/views/admin/layout.jsp").forward(request, response);
    }
}