package ma.lias.app.dao;

import ma.lias.app.model.RapportAnnuel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RapportAnnuelDAO {

    public void save(RapportAnnuel r) {

        String sql = """
            INSERT INTO rapport_annuel
            (annee, date_generation, nb_publications, nb_evenements, nb_conventions, membres_actifs)
            VALUES (?, NOW(), ?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, r.getAnnee());
            ps.setInt(2, r.getNbPublications());
            ps.setInt(3, r.getNbEvenements());
            ps.setInt(4, r.getNbConventions());
            ps.setInt(5, r.getMembresActifs());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<RapportAnnuel> findAll() {

        List<RapportAnnuel> list = new ArrayList<>();

        String sql = "SELECT * FROM rapport_annuel ORDER BY annee DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                RapportAnnuel r = new RapportAnnuel();
                r.setId(rs.getLong("id"));
                r.setAnnee(rs.getInt("annee"));
                r.setDateGeneration(rs.getTimestamp("date_generation").toLocalDateTime());
                r.setNbPublications(rs.getInt("nb_publications"));
                r.setNbEvenements(rs.getInt("nb_evenements"));
                r.setNbConventions(rs.getInt("nb_conventions"));
                r.setMembresActifs(rs.getInt("membres_actifs"));

                list.add(r);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /** Un rapport existe-t-il déjà pour cette année ? Évite les doublons. */
    public boolean existsForYear(int annee) {

        String sql = "SELECT 1 FROM rapport_annuel WHERE annee = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, annee);
            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}