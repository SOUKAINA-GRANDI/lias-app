package ma.lias.app.filter;

import jakarta.servlet.*;
import ma.lias.app.exception.BusinessException;

import java.io.IOException;

public class GlobalExceptionFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        try {
            chain.doFilter(request, response);
        } catch (BusinessException e) {

            request.setAttribute("errorMessage", e.getMessage());

            request.getRequestDispatcher(
                    "/WEB-INF/views/errors/400.jsp")
                    .forward(request, response);

        } catch (Exception e) {

            request.setAttribute("errorMessage",
                    "Erreur interne du système");

            request.getRequestDispatcher(
                    "/WEB-INF/views/errors/500.jsp")
                    .forward(request, response);
        }
    }
}