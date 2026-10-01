package ma.lias.app.dao;

import ma.lias.app.model.Notification;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {

    public void create(Long userId, String message) {

        String sql = "INSERT INTO notification (utilisateur_id, message) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ps.setString(2, message);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Notification> findByUser(Long userId) {

        List<Notification> list = new ArrayList<>();

        String sql = "SELECT * FROM notification WHERE utilisateur_id=? ORDER BY date_creation DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Notification n = new Notification();
                n.setId(rs.getLong("id"));
                n.setUtilisateurId(rs.getLong("utilisateur_id"));
                n.setMessage(rs.getString("message"));
                n.setContenu(rs.getString("contenu"));
                n.setType(rs.getString("type"));
                n.setLu(rs.getBoolean("lu"));
                n.setDateCreation(rs.getTimestamp("date_creation").toLocalDateTime());

                list.add(n);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public void markAsRead(Long id) {

        String sql = "UPDATE notification SET lu=true WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int countUnread(Long userId) {

        String sql = "SELECT COUNT(*) FROM notification WHERE utilisateur_id=? AND lu=false";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
                return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
    
    public void markAsReadSecure(Long id, Long userId) {

        String sql = """
            UPDATE notification
            SET lu = true
            WHERE id = ? AND utilisateur_id = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.setLong(2, userId);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public List<Notification> findAll() {

        List<Notification> list = new ArrayList<>();

        String sql = """
            SELECT * FROM notification
            ORDER BY date_creation DESC
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Notification n = new Notification();

                n.setId(rs.getLong("id"));
                n.setUtilisateurId(rs.getLong("utilisateur_id"));
                n.setMessage(rs.getString("message"));
                n.setLu(rs.getBoolean("lu"));
                n.setDateCreation(
                        rs.getTimestamp("date_creation")
                          .toLocalDateTime()
                );

                list.add(n);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}