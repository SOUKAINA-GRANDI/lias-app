package ma.lias.app.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import ma.lias.app.model.ParticipationEvenement;

public class ParticipationEvenementDAO {

    // ✅ Vérifier inscription active uniquement
    public boolean isInscritActif(Long membreId, Long evenementId) {

        String sql = "SELECT COUNT(*) FROM participation_evenement WHERE membre_id=? AND evenement_id=? AND annule=false";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, membreId);
            ps.setLong(2, evenementId);

            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return rs.getInt(1) > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ✅ Participer (avec gestion réactivation)
    public void participer(Long membreId, Long evenementId) {

        String checkSql = "SELECT id, annule FROM participation_evenement WHERE membre_id=? AND evenement_id=?";
        String insertSql = "INSERT INTO participation_evenement (membre_id, evenement_id, date_participation, annule) VALUES (?, ?, NOW(), false)";
        String reactivateSql = "UPDATE participation_evenement SET annule=false, date_participation=NOW() WHERE id=?";

        try (Connection conn = DBConnection.getConnection()) {

            PreparedStatement checkPs = conn.prepareStatement(checkSql);
            checkPs.setLong(1, membreId);
            checkPs.setLong(2, evenementId);

            ResultSet rs = checkPs.executeQuery();

            if (rs.next()) {
                Long id = rs.getLong("id");
                boolean annule = rs.getBoolean("annule");

                if (annule) {
                    PreparedStatement updatePs = conn.prepareStatement(reactivateSql);
                    updatePs.setLong(1, id);
                    updatePs.executeUpdate();
                }

            } else {
                PreparedStatement insertPs = conn.prepareStatement(insertSql);
                insertPs.setLong(1, membreId);
                insertPs.setLong(2, evenementId);
                insertPs.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ Annulation (soft delete)
    public void annulerParticipation(Long membreId, Long evenementId) {

        String sql = "UPDATE participation_evenement SET annule=true WHERE membre_id=? AND evenement_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, membreId);
            ps.setLong(2, evenementId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ Compter participants actifs
    public int countParticipantsActifs(Long evenementId) {

        String sql = "SELECT COUNT(*) FROM participation_evenement WHERE evenement_id=? AND annule=false";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, evenementId);

            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    // ✅ Historique complet membre
    public List<ParticipationEvenement> findByMembre(Long membreId) {

        List<ParticipationEvenement> list = new ArrayList<>();

        String sql = "SELECT * FROM participation_evenement WHERE membre_id=? ORDER BY date_participation DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, membreId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                ParticipationEvenement p = new ParticipationEvenement();
                p.setId(rs.getLong("id"));
                p.setMembreId(rs.getLong("membre_id"));
                p.setEvenementId(rs.getLong("evenement_id"));
                p.setDateParticipation(rs.getTimestamp("date_participation").toLocalDateTime());
                p.setAnnule(rs.getBoolean("annule"));

                list.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}