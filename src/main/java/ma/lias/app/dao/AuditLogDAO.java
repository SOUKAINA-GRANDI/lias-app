package ma.lias.app.dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import ma.lias.app.model.AuditLog;

public class AuditLogDAO {

    // ✅ Correction : On n'insère que utilisateur_id qui existe dans ta table
    public void save(AuditLog log) {
        String sql = """
            INSERT INTO audit_log
            (utilisateur_id, action, entity_name, entity_id, date_action)
            VALUES (?, ?, ?, ?, NOW())
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, log.getUtilisateurId());
            ps.setString(2, log.getAction());
            ps.setString(3, log.getEntityName());

            if (log.getEntityId() != null)
                ps.setLong(4, log.getEntityId());
            else
                ps.setNull(4, Types.BIGINT);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ Helper unique de mapping avec récupération sécurisée de l'email
    private AuditLog mapRow(ResultSet rs) throws SQLException {
        AuditLog log = new AuditLog();
        log.setId(rs.getLong("id"));
        log.setUtilisateurId(rs.getLong("utilisateur_id"));
        
        // Tente de récupérer l'email depuis la jointure, sinon fallback
        try {
            log.setEmailUtilisateur(rs.getString("email_utilisateur"));
        } catch (Exception e) {
            log.setEmailUtilisateur("ID: " + log.getUtilisateurId());
        }
        
        log.setAction(rs.getString("action"));
        log.setEntityName(rs.getString("entity_name"));
        log.setEntityId(rs.getLong("entity_id"));
        
        Timestamp ts = rs.getTimestamp("date_action");
        if (ts != null) {
            log.setDateAction(ts.toLocalDateTime());
        }
        return log;
    }

    public List<AuditLog> findPaginated(int offset, int limit) {
        List<AuditLog> list = new ArrayList<>();
        String sql = """
            SELECT a.*, u.email AS email_utilisateur 
            FROM audit_log a
            LEFT JOIN utilisateur u ON a.utilisateur_id = u.id
            ORDER BY a.date_action DESC LIMIT ?, ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, offset);
            ps.setInt(2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<AuditLog> findByEmailPaginated(String email, int offset, int limit) {
        List<AuditLog> list = new ArrayList<>();
        String sql = """
            SELECT a.*, u.email AS email_utilisateur 
            FROM audit_log a
            INNER JOIN utilisateur u ON a.utilisateur_id = u.id
            WHERE u.email LIKE ?
            ORDER BY a.date_action DESC LIMIT ?, ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + email + "%");
            ps.setInt(2, offset);
            ps.setInt(3, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<AuditLog> findByActionPaginated(String action, int offset, int limit) {
        List<AuditLog> list = new ArrayList<>();
        String sql = """
            SELECT a.*, u.email AS email_utilisateur 
            FROM audit_log a
            LEFT JOIN utilisateur u ON a.utilisateur_id = u.id
            WHERE a.action LIKE ?
            ORDER BY a.date_action DESC LIMIT ?, ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + action + "%");
            ps.setInt(2, offset);
            ps.setInt(3, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<AuditLog> findByEntityPaginated(String entity, int offset, int limit) {
        List<AuditLog> list = new ArrayList<>();
        String sql = """
            SELECT a.*, u.email AS email_utilisateur 
            FROM audit_log a
            LEFT JOIN utilisateur u ON a.utilisateur_id = u.id
            WHERE a.entity_name LIKE ?
            ORDER BY a.date_action DESC LIMIT ?, ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + entity + "%");
            ps.setInt(2, offset);
            ps.setInt(3, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<AuditLog> findAll() {
        List<AuditLog> list = new ArrayList<>();
        String sql = """
            SELECT a.*, u.email AS email_utilisateur 
            FROM audit_log a
            LEFT JOIN utilisateur u ON a.utilisateur_id = u.id
            ORDER BY a.date_action DESC
        """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int count(String email, LocalDateTime dateFrom, LocalDateTime dateTo) {
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) FROM audit_log a " +
                "LEFT JOIN utilisateur u ON a.utilisateur_id = u.id " +
                "WHERE 1=1 ");

        if (email != null && !email.isEmpty()) {
            sql.append("AND u.email LIKE ? ");
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            if (email != null && !email.isEmpty()) {
                ps.setString(1, "%" + email + "%");
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}