package ma.lias.app.dao;

import ma.lias.app.model.Message;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    // ✅ Envoyer message
    public void envoyer(Long conversationId,
                        Long expediteurId,
                        String contenu) {

        String sql = """
            INSERT INTO message
            (conversation_id, expediteur_id, contenu)
            VALUES (?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, conversationId);
            ps.setLong(2, expediteurId);
            ps.setString(3, contenu);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ Récupérer messages d'une conversation
    public List<Message> findByConversation(Long conversationId) {

        List<Message> list = new ArrayList<>();

        String sql = "SELECT m.*, memb.nom AS nom_expediteur FROM message m " +
                "JOIN membre memb ON m.expediteur_id = memb.utilisateur_id " +
                "WHERE m.conversation_id = ? ORDER BY m.date_envoi ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, conversationId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ✅ Marquer un message comme lu
    public void marquerLu(Long id) {

        String sql = """
            UPDATE message
            SET lu = 1
            WHERE id = ? AND lu = 0
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ Marquer tous les messages d'une conversation comme lus
    public void marquerTousLus(Long conversationId,
                               Long membreId) {

        String sql = """
            UPDATE message
            SET lu = 1
            WHERE conversation_id = ?
            AND expediteur_id != ?
            AND lu = 0
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, conversationId);
            ps.setLong(2, membreId);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ Compter messages non lus d'un membre
    public int countNonLus(Long conversationId,
                           Long membreId) {

        String sql = """
            SELECT COUNT(*)
            FROM message
            WHERE conversation_id = ?
            AND expediteur_id != ?
            AND lu = 0
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, conversationId);
            ps.setLong(2, membreId);

            ResultSet rs = ps.executeQuery();

            if (rs.next())
                return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    // ✅ Mapping centralisé
    private Message mapRow(ResultSet rs) throws SQLException {

        Message m = new Message();

        m.setId(rs.getLong("id"));
        m.setConversationId(rs.getLong("conversation_id"));
        m.setExpediteurId(rs.getLong("expediteur_id"));
        m.setContenu(rs.getString("contenu"));
        m.setLu(rs.getBoolean("lu"));

        Timestamp t = rs.getTimestamp("date_envoi");
        if (t != null)
            m.setDateEnvoi(t.toLocalDateTime());

        return m;
    }
}