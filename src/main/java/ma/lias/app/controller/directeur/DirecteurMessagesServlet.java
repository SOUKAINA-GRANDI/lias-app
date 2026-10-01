package ma.lias.app.controller.directeur; 

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ma.lias.app.model.*;
import ma.lias.app.service.*;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/directeur/messages") 
public class DirecteurMessagesServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(DirecteurMessagesServlet.class);

    private static final long serialVersionUID = 1L;
    private final MessageService messageService = new MessageService();
    private final ConversationService conversationService = new ConversationService();
    private final MembreService membreService = new MembreService();
    private final EvenementService evenementService = new EvenementService();
    private final EquipeService equipeService = new EquipeService(); // <-- Ajout du service Equipe

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Utilisateur user = checkUser(request, response);
        if (user == null) return;

        String action = request.getParameter("action");
        if (action == null) action = "prive";

        try {
            // 1. Indexation des membres pour sécuriser les appels JSP
            Map<Long, Membre> membresMap = new HashMap<>();
            List<Membre> tousLesMembres = membreService.findAll();
            if (tousLesMembres != null) {
                for (Membre m : tousLesMembres) {
                    membresMap.put(m.getId(), m);
                    membresMap.put(m.getUtilisateurId(), m);
                }
            }
            request.setAttribute("membresMap", membresMap);

            Membre expéditeurMembre = membreService.findByUserId(user.getId());
            if (expéditeurMembre != null) {
                List<Conversation> conversations = conversationService.findByUserId(expéditeurMembre.getId());
                if (conversations == null) conversations = new ArrayList<>();
                request.setAttribute("conversations", conversations);
            }

            // 2. Aiguillage selon l'action demandée
            switch (action) {
                case "equipe":
                    afficherEquipe(request, response, user);
                    break;
                case "evenement":
                    afficherEvenement(request, response, user);
                    break;
                default:
                    afficherPrive(request, response, user);
            }
        } catch (Exception e) {
            logger.error("Erreur technique", e);
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        Utilisateur user = checkUser(request, response);
        if (user == null) return;

        String action = request.getParameter("action");
        if (action == null) return;

        try {
            switch (action) {
                case "envoyerPrive":
                    envoyerPrive(request, response, user);
                    break;
                case "envoyerEquipe":
                    envoyerEquipe(request, response, user);
                    break;
                case "envoyerEvenement":
                    envoyerEvenement(request, response, user);
                    break;
            }
        } catch (Exception e) {
            logger.error("Erreur technique", e);
        }
    }

    private Utilisateur checkUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return null;
        }
        return (Utilisateur) session.getAttribute("user");
    }

    // ----------- PRIVE -----------
    private void afficherPrive(HttpServletRequest request, HttpServletResponse response, Utilisateur user)
            throws ServletException, IOException {
        
        String idParam = request.getParameter("id");
        String withParam = request.getParameter("with");
        String actionParam = request.getParameter("action");

        Membre expéditeurMembre = membreService.findByUserId(user.getId());
        if (expéditeurMembre == null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        if ("start".equals(actionParam) && withParam != null && !withParam.isBlank()) {
            try {
                Long targetUserId = Long.parseLong(withParam);
                Membre destinataireMembre = membreService.findByUtilisateurId(targetUserId);

                if (destinataireMembre != null && !destinataireMembre.getId().equals(expéditeurMembre.getId())) {
                    conversationService.findOrCreate(expéditeurMembre.getId(), destinataireMembre.getId());
                    response.sendRedirect(request.getContextPath() + "/directeur/messages?action=prive&id=" + targetUserId);
                    return;
                }
            } catch (NumberFormatException e) {
                logger.error("Erreur technique", e);
            }
        }

        if (idParam == null || idParam.isBlank()) {
            request.setAttribute("pageTitle", "Chat Interne - Messagerie Privée");
            request.setAttribute("contentPage", "/WEB-INF/views/directeur/messages.jsp");
            forwardToDirecteurLayout(request, response);
            return; 
        }

        try {
            Long autreUserId = Long.parseLong(idParam);
            Membre destinataireMembre = membreService.findByUtilisateurId(autreUserId);

            if (destinataireMembre != null) {
                Conversation conversation = conversationService.findOrCreate(expéditeurMembre.getId(), destinataireMembre.getId());
                if (conversation != null) {
                    request.setAttribute("messages", messageService.getConversation(conversation.getId(), user.getId()));
                    request.setAttribute("conversationId", conversation.getId());
                    request.setAttribute("destinataireId", autreUserId);
                }
            }
        } catch (NumberFormatException e) {
            logger.error("Erreur technique", e);
        }
        
        request.setAttribute("pageTitle", "Chat Interne - Messagerie Privée");
        request.setAttribute("contentPage", "/WEB-INF/views/directeur/chat-content.jsp");
        forwardToDirecteurLayout(request, response);
    }

    private void envoyerPrive(HttpServletRequest request, HttpServletResponse response, Utilisateur user)
            throws IOException {
        
        Long conversationId = Long.parseLong(request.getParameter("conversationId"));
        Long destinataireId = Long.parseLong(request.getParameter("destinataireId"));
        String contenu = request.getParameter("contenu");

        if (contenu != null && !contenu.isBlank()) {
            messageService.envoyerPrive(conversationId, user.getId(), destinataireId, contenu);
        }
        response.sendRedirect(request.getContextPath() + "/directeur/messages?action=prive&id=" + destinataireId);
    }

    // ----------- EQUIPE -----------
    private void afficherEquipe(HttpServletRequest request, HttpServletResponse response, Utilisateur user)
            throws ServletException, IOException {
        
        Membre membre = membreService.findByUserId(user.getId());
        String idParam = request.getParameter("id");
        Long equipeId = null;
        
        if (idParam == null || idParam.isBlank()) {
            if (membre != null && membre.getEquipeId() != null) {
                equipeId = membre.getEquipeId();
            } else {
                equipeId = 1L; 
            }
        } else {
            equipeId = Long.parseLong(idParam);
        }

        // ✅ CORRECTION APPLIQUÉE ICI : Chargement dynamique depuis la base de données
        Equipe equipe = equipeService.findById(equipeId);
        String nomEquipe = (equipe != null) ? equipe.getNom() : "Équipe N° " + equipeId;
        
        request.setAttribute("nomEquipeUtilisateur", nomEquipe);

        List<?> messages = messageService.getMessagesEquipe(equipeId);
        request.setAttribute("messages", messages);
        request.setAttribute("equipeId", equipeId);
        
        request.setAttribute("pageTitle", "Chat Interne - Équipes");
        request.setAttribute("contentPage", "/WEB-INF/views/directeur/chat-equipe.jsp");
        
        forwardToDirecteurLayout(request, response);
    }

    private void envoyerEquipe(HttpServletRequest request, HttpServletResponse response, Utilisateur user)
            throws ServletException, IOException {
        
        String equipeIdParam = request.getParameter("equipeId");
        String contenu = request.getParameter("contenu");

        if (equipeIdParam != null && contenu != null && !contenu.isBlank()) {
            try {
                Long equipeId = Long.parseLong(equipeIdParam);
                
                List<Long> membresIds = new java.util.ArrayList<>();
                List<Membre> tousLesMembres = membreService.findAll();
                if (tousLesMembres != null) {
                    for (Membre m : tousLesMembres) {
                        if (m.getEquipeId() != null && m.getEquipeId().equals(equipeId)) {
                            membresIds.add(m.getUtilisateurId());
                        }
                    }
                }
                
                messageService.envoyerEquipe(equipeId, user.getId(), contenu, membresIds);
                response.sendRedirect(request.getContextPath() + "/directeur/messages?action=equipe&id=" + equipeId);
                return;
                
            } catch (NumberFormatException e) {
                logger.error("Erreur technique", e);
            }
        }

        response.sendRedirect(request.getContextPath() + "/directeur/messages");
    }

    // ----------- EVENEMENT -----------
    private void afficherEvenement(HttpServletRequest request, HttpServletResponse response, Utilisateur user)
            throws ServletException, IOException {
        
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        Long evenementId = Long.parseLong(idParam);

        if (evenementService.findById(evenementId) == null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        List<?> messages = messageService.getMessagesEvenement(evenementId);
        request.setAttribute("messages", messages);
        request.setAttribute("evenementId", evenementId);
        
        request.setAttribute("pageTitle", "Chat Interne - Événements");
        request.setAttribute("contentPage", "/WEB-INF/views/directeur/chat-evenement.jsp");

        forwardToDirecteurLayout(request, response);
    }

    private void envoyerEvenement(HttpServletRequest request, HttpServletResponse response, Utilisateur user)
            throws IOException {
        
        Membre membre = membreService.findByUserId(user.getId());
        Long evenementId = Long.parseLong(request.getParameter("evenementId"));
        String contenu = request.getParameter("contenu");

        if (contenu != null && !contenu.isBlank() && evenementService.findById(evenementId) != null) {
            List<Long> participantsNotification = new ArrayList<>();
            Long senderId = (membre != null) ? membre.getId() : user.getId();
            messageService.envoyerEvenement(evenementId, senderId, contenu, participantsNotification);
        }
        response.sendRedirect(request.getContextPath() + "/directeur/messages?action=evenement&id=" + evenementId);
    }

    // Forces l'envoi unique vers le Layout du Directeur
    private void forwardToDirecteurLayout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/directeur/layout.jsp").forward(request, response);
    }
}