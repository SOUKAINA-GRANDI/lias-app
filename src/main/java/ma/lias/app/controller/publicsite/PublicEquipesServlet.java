package ma.lias.app.controller.publicsite;

import ma.lias.app.service.EquipeService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/public/equipes")
public class PublicEquipesServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final EquipeService equipeService = new EquipeService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        var equipes = equipeService.findAll();

        Map<Long, Integer> effectifs = new HashMap<>();
        for (var e : equipes) {
            effectifs.put(e.getId(), equipeService.countMembres(e.getId()));
        }

        request.setAttribute("equipes", equipes);
        request.setAttribute("effectifs", effectifs);
        request.setAttribute("pageTitle", "Équipes");
        request.setAttribute("contentPage", "/WEB-INF/views/public/equipes.jsp");

        request.getRequestDispatcher("/WEB-INF/views/public/layout.jsp")
                .forward(request, response);
    }
}