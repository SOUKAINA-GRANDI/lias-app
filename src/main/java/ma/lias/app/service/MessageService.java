package ma.lias.app.service;

import ma.lias.app.dao.*;
import ma.lias.app.model.*;
import java.util.List;

public class MessageService {

    private final MessageDAO          messageDAO          = new MessageDAO();
    private final MessageEquipeDAO    messageEquipeDAO    = new MessageEquipeDAO();
    private final MessageEvenementDAO messageEvenementDAO = new MessageEvenementDAO();
    private final NotificationDAO     notificationDAO     = new NotificationDAO();

    // ─────────────────────────────────────────────
    // MESSAGE PRIVÉ
    // expediteurId  = user.getId()  (id utilisateur)
    // destinataireId = user.getId() du destinataire
    // ─────────────────────────────────────────────
    public void envoyerPrive(Long conversationId,
                             Long expediteurId,
                             Long destinataireId,
                             String contenu) {

        // 1. Persister le message
        messageDAO.envoyer(conversationId, expediteurId, contenu);

        // 2. Notifier le destinataire (id utilisateur direct)
        notificationDAO.create(destinataireId,
                "💬 Vous avez reçu un nouveau message privé.");
    }

    // ─────────────────────────────────────────────
    // MESSAGE ÉQUIPE
    // expediteurUserId = user.getId()
    // membresUtilisateurIds = liste des user.getId() des membres
    // ─────────────────────────────────────────────
    public void envoyerEquipe(Long equipeId,
                              Long expediteurUserId,
                              String contenu,
                              List<Long> membresUtilisateurIds) {

        // 1. Persister dans message_equipe
        messageEquipeDAO.envoyer(equipeId, expediteurUserId, contenu);

        // 2. Notifier chaque membre de l'équipe (sauf l'expéditeur)
        for (Long userId : membresUtilisateurIds) {
            if (!userId.equals(expediteurUserId)) {
                notificationDAO.create(userId,
                        "👥 Nouveau message dans votre équipe.");
            }
        }
    }

    // ─────────────────────────────────────────────
    // MESSAGE ÉVÉNEMENT
    // expediteurUserId = user.getId()
    // participantsUserIds = liste des user.getId() des participants
    // ─────────────────────────────────────────────
    public void envoyerEvenement(Long evenementId,
                                 Long expediteurUserId,
                                 String contenu,
                                 List<Long> participantsUserIds) {

        // 1. Persister dans message_evenement
        messageEvenementDAO.envoyer(evenementId, expediteurUserId, contenu);

        // 2. Notifier les participants (sauf l'expéditeur)
        for (Long userId : participantsUserIds) {
            if (!userId.equals(expediteurUserId)) {
                notificationDAO.create(userId,
                        "📅 Nouveau message lié à un événement.");
            }
        }
    }

    // ─────────────────────────────────────────────
    // LECTURE — PRIVÉ
    // Marque les messages reçus comme lus à l'ouverture
    // ─────────────────────────────────────────────
    public List<Message> getConversation(Long conversationId,
                                         Long currentUserId) {
        messageDAO.marquerTousLus(conversationId, currentUserId);
        return messageDAO.findByConversation(conversationId);
    }

    // ─────────────────────────────────────────────
    // LECTURE — ÉQUIPE
    // MessageEquipeDAO.findByEquipe retourne List<Message>
    // (champ conversationId réutilisé pour equipeId)
    // ─────────────────────────────────────────────
    public List<Message> getMessagesEquipe(Long equipeId) {
        return messageEquipeDAO.findByEquipe(equipeId);
    }

    // ─────────────────────────────────────────────
    // LECTURE — ÉVÉNEMENT
    // On convertit MessageEvenement → Message pour
    // que la JSP utilise toujours la même structure
    // ─────────────────────────────────────────────
    public List<Message> getMessagesEvenement(Long evenementId) {

        List<MessageEvenement> raw = messageEvenementDAO.findByEvenement(evenementId);
        List<Message> result = new java.util.ArrayList<>();

        for (MessageEvenement me : raw) {
            Message m = new Message();
            m.setId(me.getId());
            m.setExpediteurId(me.getExpediteurId());
            m.setNomExpediteur(me.getNomExpediteur());
            m.setContenu(me.getContenu());
            m.setDateEnvoi(me.getDateEnvoi());
            m.setConversationId(me.getEvenementId()); // réutilisé pour l'id événement
            result.add(m);
        }

        return result;
    }
}