package ma.lias.app.dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ma.lias.app.model.AttributionMateriel;

public class AttributionMaterielDAO {

    public void attribuer(Long materielId, Long membreId) {

        String sql = "INSERT INTO attribution_materiel (materiel_id, membre_id, date_attribution) VALUES (?,?,?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, materielId);
            ps.setLong(2, membreId);
            ps.setDate(3, Date.valueOf(LocalDate.now()));

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void retourner(Long id) {

    	String sql = """
    		    UPDATE attribution_materiel
    		    SET date_retour=?
    		    WHERE id=? AND date_retour IS NULL
    		""";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(LocalDate.now()));
            ps.setLong(2, id);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public List<AttributionMateriel> findAll() {
        List<AttributionMateriel> list = new ArrayList<>();

        String sql = "SELECT * FROM attribution_materiel ORDER BY date_attribution DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                AttributionMateriel a = new AttributionMateriel();
                a.setId(rs.getLong("id"));
                a.setMaterielId(rs.getLong("materiel_id"));
                a.setMembreId(rs.getLong("membre_id"));
                a.setDateAttribution(rs.getDate("date_attribution").toLocalDate());

                if (rs.getDate("date_retour") != null)
                    a.setDateRetour(rs.getDate("date_retour").toLocalDate());

                list.add(a);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    public List<AttributionMateriel> findByMembre(Long membreId) {

        List<AttributionMateriel> list = new ArrayList<>();

        String sql = """
            SELECT * FROM attribution_materiel
            WHERE membre_id=? AND date_retour IS NULL
            ORDER BY date_attribution DESC
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, membreId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                AttributionMateriel a = new AttributionMateriel();
                a.setId(rs.getLong("id"));
                a.setMaterielId(rs.getLong("materiel_id"));
                a.setMembreId(rs.getLong("membre_id"));
                a.setDateAttribution(rs.getDate("date_attribution").toLocalDate());

                list.add(a);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    public List<AttributionMateriel> findAllByMembre(Long membreId) {

        List<AttributionMateriel> list = new ArrayList<>();

        String sql = """
            SELECT a.*, m.nom AS materiel_nom, m.type AS materiel_type
            FROM attribution_materiel a
            JOIN materiel m ON m.id = a.materiel_id
            WHERE a.membre_id = ?
            ORDER BY a.date_attribution DESC
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, membreId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                AttributionMateriel a = new AttributionMateriel();
                a.setId(rs.getLong("id"));
                a.setMaterielId(rs.getLong("materiel_id"));
                a.setMembreId(rs.getLong("membre_id"));
                a.setDateAttribution(rs.getDate("date_attribution").toLocalDate());

                Date retour = rs.getDate("date_retour");
                if (retour != null) a.setDateRetour(retour.toLocalDate());

                a.setNom(rs.getString("materiel_nom"));
                a.setType(rs.getString("materiel_type"));

                list.add(a);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public void attribuer(Connection conn, Long materielId, Long membreId) throws SQLException {
        String sql = "INSERT INTO attribution_materiel (materiel_id, membre_id, date_attribution) VALUES (?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, materielId);
            ps.setLong(2, membreId);
            ps.setDate(3, Date.valueOf(LocalDate.now()));
            ps.executeUpdate();
        }
    }
    /** Nécessaire pour retrouver le matériel concerné avant de réapprovisionner le stock. */
    public AttributionMateriel findById(Long id) {

        String sql = "SELECT * FROM attribution_materiel WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                AttributionMateriel a = new AttributionMateriel();
                a.setId(rs.getLong("id"));
                a.setMaterielId(rs.getLong("materiel_id"));
                a.setMembreId(rs.getLong("membre_id"));
                a.setDateAttribution(rs.getDate("date_attribution").toLocalDate());

                Date retour = rs.getDate("date_retour");
                if (retour != null) a.setDateRetour(retour.toLocalDate());

                return a;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /** Version transactionnelle : utilisée avec le réapprovisionnement du stock. */
    public boolean retourner(Connection conn, Long id) throws SQLException {

        String sql = """
            UPDATE attribution_materiel
            SET date_retour = ?
            WHERE id = ? AND date_retour IS NULL
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(LocalDate.now()));
            ps.setLong(2, id);
            return ps.executeUpdate() > 0;   // false si déjà retourné entre-temps
        }
    }
}