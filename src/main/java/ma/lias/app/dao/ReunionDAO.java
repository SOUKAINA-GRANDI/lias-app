package ma.lias.app.dao;

import ma.lias.app.model.Reunion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReunionDAO {

    public void save(Reunion r) {

        String sql = """
            INSERT INTO reunion
            (titre, date_reunion, ordre_du_jour, pv_path)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, r.getTitre());
            ps.setDate(2, Date.valueOf(r.getDateReunion()));
            ps.setString(3, r.getOrdreDuJour());
            ps.setString(4, r.getPvPath());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Reunion> findAll() {

        List<Reunion> list = new ArrayList<>();

        String sql = "SELECT * FROM reunion WHERE archive=false ORDER BY date_reunion DESC";

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

    public void archive(Long id) {

        String sql = """
            UPDATE reunion
            SET archive=true
            WHERE id=? AND archive=false
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Reunion mapRow(ResultSet rs) throws SQLException {

        Reunion r = new Reunion();

        r.setId(rs.getLong("id"));
        r.setTitre(rs.getString("titre"));

        Date d = rs.getDate("date_reunion");
        if (d != null)
            r.setDateReunion(d.toLocalDate());

        r.setOrdreDuJour(rs.getString("ordre_du_jour"));
        r.setPvPath(rs.getString("pv_path"));
        r.setArchive(rs.getBoolean("archive"));

        Timestamp t = rs.getTimestamp("date_creation");
        if (t != null)
            r.setDateCreation(t.toLocalDateTime());

        return r;
    }
    public List<Reunion> search(String keyword) {

        List<Reunion> list = new ArrayList<>();

        String sql = """
            SELECT * FROM reunion
            WHERE archive=false
            AND (titre LIKE ? OR ordre_du_jour LIKE ?)
            ORDER BY date_reunion DESC
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String kw = "%" + keyword + "%";

            ps.setString(1, kw);
            ps.setString(2, kw);

            ResultSet rs = ps.executeQuery();

            while (rs.next())
                list.add(mapRow(rs));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    public Reunion findById(Long id) {

        String sql = "SELECT * FROM reunion WHERE id=?";

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
    
    public void update(Reunion r) {
        String sql = "UPDATE reunion SET titre = ?, date_reunion = ?, ordre_du_jour = ?, pv_path = ? WHERE id = ?";
        try (java.sql.Connection conn = ma.lias.app.dao.DBConnection.getConnection(); // Mets ta classe de connexion ici
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, r.getTitre());
            ps.setDate(2, java.sql.Date.valueOf(r.getDateReunion()));
            ps.setString(3, r.getOrdreDuJour());
            ps.setString(4, r.getPvPath()); // On utilise pvPath à la place de procesVerbal
            ps.setLong(5, r.getId());
            
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la mise à jour de la réunion : " + e.getMessage());
        }
    }
}