package ma.lias.app.service;

import java.util.List;

import ma.lias.app.dao.ConversationDAO;
import ma.lias.app.model.Conversation;

public class ConversationService {

    private final ConversationDAO dao = new ConversationDAO();

    // ✅ Trouver ou créer conversation
public Conversation findOrCreate(Long userId1, Long userId2) {
        
        // 🛑 SÉCURITÉ : Si l'utilisateur essaie de discuter avec lui-même, on bloque !
        if (userId1 == null || userId2 == null || userId1.equals(userId2)) {
            System.out.println("[Chat] Tentative de conversation invalide avec soi-même ignorée.");
            return null; 
            // Alternative : tu peux lever une exception si tu préfères
            // throw new IllegalArgumentException("Impossible de créer une conversation avec soi-même.");
        }

        // Recherche d'une conversation existante
        Conversation existing = dao.findBetweenUsers(userId1, userId2);

        if (existing != null) {
            return existing;
        }

        // Création d'une nouvelle conversation
        Conversation nouvelle = new Conversation();

        // ✅ Ordre cohérent pour éviter les doublons en base (ex: conv entre 1 et 3 sera toujours stockée user1=1, user2=3)
        nouvelle.setUser1Id(Math.min(userId1, userId2));
        nouvelle.setUser2Id(Math.max(userId1, userId2));

        return dao.save(nouvelle);
    }

    public Conversation findById(Long id) {
        return dao.findById(id);
    }
    public List<Conversation> findByUserId(Long userId) {
        // Appelle la méthode correspondante dans ton DAO
        // (Vérifie dans ton ConversationDAO si elle s'appelle findByUserId ou getConversationsByUtilisateur)
        return dao.findByUserId(userId); 
    }
}