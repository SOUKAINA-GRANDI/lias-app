package ma.lias.app.dao;

import ma.lias.app.model.DemandeMateriel;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DemandeMaterielDAO {

    public void create(Long membreId, String description) {

        String sql = """
            INSERT INTO demande_materiel
            (membre_id, description, statut, date_demande)
            VALUES (?, ?, 'EN_ATTENTE', ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, membreId);
            ps.setString(2, description);
            ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public DemandeMateriel findById(Long id) {

        String sql = "SELECT * FROM demande_materiel WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                DemandeMateriel d = new DemandeMateriel();
                d.setId(rs.getLong("id"));
                d.setMembreId(rs.getLong("membre_id"));
                d.setDescription(rs.getString("description"));
                d.setStatut(rs.getString("statut"));
                d.setDateDemande(rs.getTimestamp("date_demande").toLocalDateTime());
                return d;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateStatut(Long id, String statut) {

        String sql = "UPDATE demande_materiel\r\n"
        		+ "SET statut=?\r\n"
        		+ "WHERE id=? AND statut='EN_ATTENTE'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, statut);
            ps.setLong(2, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<DemandeMateriel> findAllEnAttente() {

        List<DemandeMateriel> list = new ArrayList<>();

        String sql = """
            SELECT * FROM demande_materiel
            WHERE statut='EN_ATTENTE'
            ORDER BY date_demande DESC
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                DemandeMateriel d = new DemandeMateriel();

                d.setId(rs.getLong("id"));
                d.setMembreId(rs.getLong("membre_id"));
                d.setDescription(rs.getString("description"));
                d.setStatut(rs.getString("statut"));
                d.setDateDemande(
                        rs.getTimestamp("date_demande")
                          .toLocalDateTime()
                );

                list.add(d);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}