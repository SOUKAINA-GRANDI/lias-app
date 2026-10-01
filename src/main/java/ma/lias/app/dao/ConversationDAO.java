package ma.lias.app.dao;

import ma.lias.app.model.Conversation;

import java.sql.*;

public class ConversationDAO {

    // ✅ Trouver conversation entre deux utilisateurs
	 public Conversation findBetweenUsers(Long user1Id, Long user2Id) {

	        String sql = """
	            SELECT * FROM conversation
	            WHERE (user1_id = ? AND user2_id = ?)
	               OR (user1_id = ? AND user2_id = ?)
	        """;

	        try (Connection conn = DBConnection.getConnection();
	             PreparedStatement ps = conn.prepareStatement(sql)) {

	            ps.setLong(1, user1Id);
	            ps.setLong(2, user2Id);
	            ps.setLong(3, user2Id);
	            ps.setLong(4, user1Id);

	            ResultSet rs = ps.executeQuery();

	            if (rs.next()) {
	                return mapRow(rs);
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return null;
	    }
    // ✅ Créer nouvelle conversation
    public Conversation save(Conversation c) {

        String sql = """
            INSERT INTO conversation (user1_id, user2_id)
            VALUES (?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     sql,
                     Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, c.getUser1Id());
            ps.setLong(2, c.getUser2Id());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                c.setId(rs.getLong(1));
            }

            return c;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // ✅ Trouver par ID
    public Conversation findById(Long id) {

        String sql = "SELECT * FROM conversation WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRow(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    // ✅ Mapping
    private Conversation mapRow(ResultSet rs) throws SQLException {

        Conversation c = new Conversation();

        c.setId(rs.getLong("id"));
        c.setUser1Id(rs.getLong("user1_id"));
        c.setUser2Id(rs.getLong("user2_id"));

        Timestamp t = rs.getTimestamp("date_creation");
        if (t != null)
            c.setDateCreation(t.toLocalDateTime());

        return c;
    }
 // 🚀 AJOUT : Trouver toutes les conversations d'un utilisateur
    public java.util.List<Conversation> findByUserId(Long userId) {
        java.util.List<Conversation> liste = new java.util.ArrayList<>();
        
        // On cherche si l'utilisateur est soit l'user1, soit l'user2
        String sql = "SELECT * FROM conversation WHERE user1_id = ? OR user2_id = ? ORDER BY date_creation DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ps.setLong(2, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    liste.add(mapRow(rs));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return liste;
    }
}