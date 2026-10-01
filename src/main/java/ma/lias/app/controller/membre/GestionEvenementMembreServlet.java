package ma.lias.app.controller.membre;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import ma.lias.app.model.Evenement;
import ma.lias.app.model.Membre;
import ma.lias.app.model.Utilisateur;
import ma.lias.app.service.EvenementService;
import ma.lias.app.service.MembreService;
import ma.lias.app.dao.ParticipationEvenementDAO;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/membre/evenements")
public class GestionEvenementMembreServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final EvenementService evenementService = new EvenementService();
    private final MembreService membreService = new MembreService();
    private final ParticipationEvenementDAO participationDAO = new ParticipationEvenementDAO();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) action = "liste";

        switch (action) {

            case "detail":
                afficherDetail(request, response);
                break;

            case "calendrier":
                afficherCalendrier(request, response);
                break;

            default:
                afficherListe(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Utilisateur user = (Utilisateur) session.getAttribute("user");
        String action = request.getParameter("action");
        String idParam = request.getParameter("id");

        if (action != null && idParam != null) {
            try {
                Long evenementId = Long.parseLong(idParam);

                if ("participer".equals(action)) {
                    evenementService.participer(user.getId(), evenementId);
                    session.setAttribute("success", "Votre participation a été enregistrée !");
                } else if ("annuler".equals(action)) {
                    evenementService.annulerParticipation(user.getId(), evenementId);
                    session.setAttribute("success", "Votre participation a été annulée.");
                }

            } catch (NumberFormatException e) {
                session.setAttribute("error", "Identifiant d'événement invalide.");
            } catch (Exception e) {
                session.setAttribute("error", e.getMessage());
            }
        }

        response.sendRedirect(request.getContextPath() + "/membre/evenements");
    }

    /** Un associé ne voit que les événements marqués "ouvert aux associés". */
    private boolean estVisible(Evenement e, Membre membre) {
        if (membre == null || !"ASSOCIE".equals(membre.getStatut())) return true;
        return e.isOuvertAuxAssocies();
    }

    private void afficherListe(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Utilisateur user = (Utilisateur) request.getSession().getAttribute("user");
        Membre membre = membreService.findByUserId(user.getId());

        List<Evenement> evenements = evenementService.findAll().stream()
                .filter(e -> estVisible(e, membre))
                .toList();

        Map<String, Boolean> inscriptionMap = new HashMap<>();
        Map<String, Integer> compteurMap = new HashMap<>();

        for (Evenement e : evenements) {
            String key = e.getId().toString();

            inscriptionMap.put(key,
                    participationDAO.isInscritActif(user.getId(), e.getId()));

            compteurMap.put(key,
                    participationDAO.countParticipantsActifs(e.getId()));
        }

        request.setAttribute("evenements", evenements);
        request.setAttribute("inscriptions", inscriptionMap);
        request.setAttribute("compteurs", compteurMap);

        request.setAttribute("pageTitle", "Événements");
        request.setAttribute("contentPage", "/WEB-INF/views/membre/evenements-content.jsp");

        request.getRequestDispatcher("/WEB-INF/views/membre/layout.jsp")
               .forward(request, response);
    }
   
    private void afficherDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String idParam = request.getParameter("id");

        if (idParam == null || !idParam.matches("\\d+")) {
            request.setAttribute("evenement", null);
            request.setAttribute("pageTitle", "Détail Événement");
            request.setAttribute("contentPage",
                    "/WEB-INF/views/membre/evenement-detail.jsp");
            request.getRequestDispatcher("/WEB-INF/views/membre/layout.jsp")
            .forward(request, response);
            return;
        }

        Long id = Long.parseLong(idParam);
        Evenement evenement = evenementService.findById(id);

        Utilisateur user = (Utilisateur) request.getSession().getAttribute("user");
        Membre membre = membreService.findByUserId(user.getId());

        // ⚠️ Un associé ne peut pas accéder au détail d'un événement qui ne lui est pas ouvert,
        // même en tapant l'adresse directement.
        if (evenement != null && !estVisible(evenement, membre)) {
            request.getSession().setAttribute("error",
                    "Cet événement n'est pas accessible aux membres associés.");
            response.sendRedirect(request.getContextPath() + "/membre/evenements");
            return;
        }

        request.setAttribute("evenement", evenement);
        request.setAttribute("organisateurs", evenementService.findOrganisateursTexte(id));

        request.setAttribute("pageTitle", "Détail Événement");
        request.setAttribute("contentPage",
                "/WEB-INF/views/membre/evenement-detail.jsp");

        request.getRequestDispatcher(
                "/WEB-INF/views/membre/layout.jsp")
        .forward(request, response);
    }

    private void afficherCalendrier(HttpServletRequest request,
                                     HttpServletResponse response)
            throws ServletException, IOException {

        String yearParam = request.getParameter("year");
        int year;

        if (yearParam != null && !yearParam.isEmpty()) {
            try {
                year = Integer.parseInt(yearParam);
            } catch (NumberFormatException e) {
                year = LocalDate.now().getYear();
            }
        } else {
            year = LocalDate.now().getYear();
        }

        Utilisateur user = (Utilisateur) request.getSession().getAttribute("user");
        Membre membre = membreService.findByUserId(user.getId());

        List<Evenement> evenements = evenementService.findByYear(year).stream()
                .filter(e -> estVisible(e, membre))
                .toList();

        request.setAttribute("evenements", evenements);
        request.setAttribute("currentYear", year);
        request.setAttribute("pageTitle", "Calendrier");
        request.setAttribute("contentPage",
                "/WEB-INF/views/membre/calendrier-content.jsp");

        request.getRequestDispatcher(
                "/WEB-INF/views/membre/layout.jsp")
               .forward(request, response);
    }
}