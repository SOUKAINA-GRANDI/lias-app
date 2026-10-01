package ma.lias.app.dao;

import ma.lias.app.model.Message;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageEquipeDAO {

    private Connection getConnection() throws Exception {
        return ma.lias.app.dao.DBConnection.getConnection(); 
    }

    /**
     * ✅ CORRIGÉ : Envoie un message d'équipe dans la VRAIE table 'message_equipe'
     */
    public void envoyer(Long equipeId, Long expediteurId, String contenu) {
        String sql = "INSERT INTO message_equipe (expediteur_id, contenu, equipe_id) VALUES (?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setLong(1, expediteurId);
            ps.setString(2, contenu);
            ps.setLong(3, equipeId);
            ps.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ✅ CORRIGÉ : Récupère les messages depuis 'message_equipe' avec le nom de l'expéditeur
     */
    public List<Message> findByEquipe(Long equipeId) {
        List<Message> list = new ArrayList<>();
        
        // Jointure modifiée pour cibler la table message_equipe
        String sql = "SELECT me.*, memb.nom AS nom_expediteur FROM message_equipe me " +
                     "JOIN membre memb ON me.expediteur_id = memb.utilisateur_id " +
                     "WHERE me.equipe_id = ? ORDER BY me.date_envoi ASC";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setLong(1, equipeId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Message m = new Message();
                    m.setId(rs.getLong("id"));
                    m.setExpediteurId(rs.getLong("expediteur_id"));
                    m.setNomExpediteur(rs.getString("nom_expediteur"));
                    m.setContenu(rs.getString("contenu"));
                    
                    if (rs.getTimestamp("date_envoi") != null) {
                        m.setDateEnvoi(rs.getTimestamp("date_envoi").toLocalDateTime());
                    }
                    
                    // On mappe equipe_id vers le champ de ton modèle (ou setConversationId si tu réutilises le même objet)
                    m.setConversationId(rs.getLong("equipe_id"));
                    
                    list.add(m);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}