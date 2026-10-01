package ma.lias.app.dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ma.lias.app.model.AffiliationHistorique;

public class AffiliationHistoriqueDAO {

    // ✅ Démarrer affiliation
    public void startAffiliation(Long membreId) {

        String sql = "INSERT INTO affiliation_historique (membre_id, date_debut) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, membreId);
            ps.setDate(2, Date.valueOf(LocalDate.now()));

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ Clôturer affiliation active
    public void endAffiliation(Long membreId, String motif) {

        String sql = """
            UPDATE affiliation_historique
            SET date_fin=?, motif_depart=?
            WHERE membre_id=? AND date_fin IS NULL
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(LocalDate.now()));
            ps.setString(2, motif);
            ps.setLong(3, membreId);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public List<AffiliationHistorique> findAll() {

        List<AffiliationHistorique> list = new ArrayList<>();

        String sql = "SELECT a.*, m.nom, m.prenom\r\n"
        		+ "FROM affiliation_historique a\r\n"
        		+ "LEFT JOIN membre m ON a.membre_id = m.id\r\n"
        		+ "ORDER BY a.date_debut DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                AffiliationHistorique a = new AffiliationHistorique();
                a.setId(rs.getLong("id"));
                a.setMembreId(rs.getLong("membre_id"));
                a.setMembreNom(rs.getString("nom") + " " + rs.getString("prenom"));
                a.setDateDebut(rs.getDate("date_debut").toLocalDate());

                if (rs.getDate("date_fin") != null)
                    a.setDateFin(rs.getDate("date_fin").toLocalDate());

                a.setMotifDepart(rs.getString("motif_depart"));

                list.add(a);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    
    public void startAffiliation(Connection conn,
            Long membreId) throws SQLException {

String sql = """
INSERT INTO affiliation_historique
(membre_id, date_debut)
VALUES (?, ?)
""";

try (PreparedStatement ps =
conn.prepareStatement(sql)) {

ps.setLong(1, membreId);
ps.setDate(2, Date.valueOf(LocalDate.now()));
ps.executeUpdate();
}
}
}