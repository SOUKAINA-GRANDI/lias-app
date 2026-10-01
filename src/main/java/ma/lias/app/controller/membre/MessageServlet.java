package ma.lias.app.controller.membre;

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

@WebServlet("/messages") 
public class MessageServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final MessageService messageService = new MessageService();
    private final ConversationService conversationService = new ConversationService();
    private final MembreService membreService = new MembreService();
    private final EvenementService evenementService = new EvenementService();
    private final EquipeService equipeService = new EquipeService(); 


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Utilisateur user = checkUser(request, response);
        if (user == null) return;

        String action = request.getParameter("action");
        if (action == null) action = "prive";

        try {
            // 💡 Indexation double pour sécuriser les appels JSP : par ID de membre et ID d'utilisateur
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
            e.printStackTrace();
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
            e.printStackTrace();
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
                    response.sendRedirect(request.getContextPath() + "/messages?action=prive&id=" + targetUserId);
                    return;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        if (idParam == null || idParam.isBlank()) {
            request.setAttribute("contentPage", "/WEB-INF/views/membre/messages.jsp");
            forward(request, response, user);
            return; 
        }

        try {
            Long autreUserId = Long.parseLong(idParam);
            Membre destinataireMembre = membreService.findByUtilisateurId(autreUserId);

            if (destinataireMembre != null) {
                Conversation conversation = conversationService.findOrCreate(expéditeurMembre.getId(), destinataireMembre.getId());
                
                // 💡 SÉCURITÉ : On vérifie si la conversation n'est pas nulle avant de lire ses données
                if (conversation != null) {
                    request.setAttribute("messages", messageService.getConversation(conversation.getId(), user.getId()));
                    request.setAttribute("conversationId", conversation.getId());
                    request.setAttribute("destinataireId", autreUserId);
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        
        request.setAttribute("contentPage", "/WEB-INF/views/membre/chat-content.jsp");
        forward(request, response, user);
    }

    private void envoyerPrive(HttpServletRequest request, HttpServletResponse response, Utilisateur user)
            throws IOException {
        
        Long conversationId = Long.parseLong(request.getParameter("conversationId"));
        Long destinataireId = Long.parseLong(request.getParameter("destinataireId"));
        String contenu = request.getParameter("contenu");

        if (contenu != null && !contenu.isBlank()) {
            messageService.envoyerPrive(conversationId, user.getId(), destinataireId, contenu);
        }
        response.sendRedirect(request.getContextPath() + "/messages?action=prive&id=" + destinataireId);
    }

    // ----------- EQUIPE -----------
    // ----------- EQUIPE -----------
    private void afficherEquipe(HttpServletRequest request, HttpServletResponse response, Utilisateur user)
            throws ServletException, IOException {

        Membre membre = membreService.findByUserId(user.getId());
        String idParam = request.getParameter("id");
        boolean aTousLesDroits = user.isAdmin() || (membre != null && "DIRECTEUR".equalsIgnoreCase(membre.getRole()));

        Long equipeId;

        if (idParam != null && !idParam.isBlank()) {
            // Une équipe précise est demandée (lien direct) : on vérifie vraiment les droits.
            equipeId = Long.parseLong(idParam);

            if (!aTousLesDroits && (membre == null || !equipeId.equals(membre.getEquipeId()))) {
                request.getSession().setAttribute("error", "Vous n'avez pas accès au chat de cette équipe.");
                response.sendRedirect(request.getContextPath() + "/membre/dashboard");
                return;
            }

        } else {
            // Pas d'id précisé : on montre SA PROPRE équipe, aucun contrôle supplémentaire nécessaire.
            equipeId = (membre != null) ? membre.getEquipeId() : null;

            if (equipeId == null && !aTousLesDroits) {
                // Le membre n'a aucune équipe assignée : on l'affiche clairement, pas de redirection dans le vide.
                request.setAttribute("pasDequipe", true);
                request.setAttribute("contentPage", "/WEB-INF/views/membre/chat-equipe.jsp");
                forward(request, response, user);
                return;
            }

            if (equipeId == null) {
                equipeId = 1L; // repli uniquement pour un admin/directeur qui consulte sans équipe personnelle
            }
        }

        Equipe equipe = equipeService.findById(equipeId);
        String nomEquipe = (equipe != null) ? equipe.getNom() : "Équipe N° " + equipeId;
        request.setAttribute("nomEquipeUtilisateur", nomEquipe);

        List<?> messages = messageService.getMessagesEquipe(equipeId);
        request.setAttribute("messages", messages);
        request.setAttribute("equipeId", equipeId);
        request.setAttribute("contentPage", "/WEB-INF/views/membre/chat-equipe.jsp");

        forward(request, response, user);
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
                response.sendRedirect(request.getContextPath() + "/messages?action=equipe&id=" + equipeId);
                return;
                
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        response.sendRedirect(request.getContextPath() + "/messages");
    }

    // ----------- EVENEMENT -----------
    private void afficherEvenement(HttpServletRequest request, HttpServletResponse response, Utilisateur user)
            throws ServletException, IOException {
        
        Long evenementId = Long.parseLong(request.getParameter("id"));

        if (evenementService.findById(evenementId) == null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        List<?> messages = messageService.getMessagesEvenement(evenementId);
        request.setAttribute("messages", messages);
        request.setAttribute("evenementId", evenementId);
        request.setAttribute("contentPage", "/WEB-INF/views/membre/chat-evenement.jsp");

        forward(request, response, user);
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
        response.sendRedirect(request.getContextPath() + "/messages?action=evenement&id=" + evenementId);
    }

    private void forward(HttpServletRequest request, HttpServletResponse response, Utilisateur user)
            throws ServletException, IOException {
        
        String layoutPath = "/WEB-INF/views/membre/layout.jsp";

        if (user.isAdmin()) {
            layoutPath = "/WEB-INF/views/admin/layout.jsp";
        } else {
            Membre membre = membreService.findByUserId(user.getId());
            if (membre != null && "DIRECTEUR".equalsIgnoreCase(membre.getRole())) {
                layoutPath = "/WEB-INF/views/directeur/layout.jsp"; 
            }
        }

        request.getRequestDispatcher(layoutPath).forward(request, response);
    }
}