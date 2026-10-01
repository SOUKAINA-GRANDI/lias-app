package ma.lias.app.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ma.lias.app.model.Evenement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EvenementDAO {

    private static final Logger logger = LoggerFactory.getLogger(EvenementDAO.class);

    public List<Evenement> findAll() {

        List<Evenement> list = new ArrayList<>();

        String sql = """
            SELECT * FROM evenement
            WHERE archive = 0
            ORDER BY date_debut DESC
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (Exception ex) {
            logger.error("Erreur technique", ex);
        }

        return list;
    }

    public void save(Evenement e) {

        String sql = """
            INSERT INTO evenement
            (titre, description, type, date_debut, date_fin, lieu, ouvert_aux_associes)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, e.getTitre());
            ps.setString(2, e.getDescription());
            ps.setString(3, e.getType());
            ps.setTimestamp(4, Timestamp.valueOf(e.getDateDebut()));

            if (e.getDateFin() != null)
                ps.setTimestamp(5, Timestamp.valueOf(e.getDateFin()));
            else
                ps.setNull(5, Types.TIMESTAMP);

            ps.setString(6, e.getLieu());
            ps.setBoolean(7, e.isOuvertAuxAssocies());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next())
                e.setId(rs.getLong(1));

        } catch (Exception ex) {
            logger.error("Erreur technique", ex);
        }
    }

    public void update(Evenement e) {

        String sql = """
            UPDATE evenement SET
            titre=?, description=?, type=?,
            date_debut=?, date_fin=?, lieu=?, ouvert_aux_associes=?
            WHERE id=?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, e.getTitre());
            ps.setString(2, e.getDescription());
            ps.setString(3, e.getType());
            ps.setTimestamp(4, Timestamp.valueOf(e.getDateDebut()));

            if (e.getDateFin() != null)
                ps.setTimestamp(5, Timestamp.valueOf(e.getDateFin()));
            else
                ps.setNull(5, Types.TIMESTAMP);

            ps.setString(6, e.getLieu());
            ps.setBoolean(7, e.isOuvertAuxAssocies());
            ps.setLong(8, e.getId());

            ps.executeUpdate();

        } catch (Exception ex) {
            logger.error("Erreur technique", ex);
        }
    }

    public void archive(Long id) {

        String sql = "UPDATE evenement SET archive=true WHERE id=? AND archive=false";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (Exception ex) {
            logger.error("Erreur technique", ex);
        }
    }

    public List<Evenement> findByYear(int year) {

        List<Evenement> list = new ArrayList<>();

        String sql = """
            SELECT * FROM evenement
            WHERE YEAR(date_debut) = ? AND archive = 0
            ORDER BY date_debut ASC
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, year);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (Exception e) {
            logger.error("Erreur technique", e);
        }

        return list;
    }

    private Evenement mapRow(ResultSet rs) throws SQLException {

        Evenement e = new Evenement();

        e.setId(rs.getLong("id"));
        e.setTitre(rs.getString("titre"));
        e.setDescription(rs.getString("description"));
        e.setType(rs.getString("type"));

        Timestamp td = rs.getTimestamp("date_debut");
        if (td != null) {
            e.setDateDebut(td.toLocalDateTime());
        }

        Timestamp tf = rs.getTimestamp("date_fin");
        if (tf != null)
            e.setDateFin(tf.toLocalDateTime());

        e.setLieu(rs.getString("lieu"));

        Timestamp tc = rs.getTimestamp("date_creation");
        if (tc != null)
            e.setDateCreation(tc.toLocalDateTime());

        e.setArchive(rs.getBoolean("archive"));
        e.setOuvertAuxAssocies(rs.getBoolean("ouvert_aux_associes"));

        return e;
    }

    public List<Evenement> search(String keyword) {

        List<Evenement> list = new ArrayList<>();

        String sql = """
            SELECT * FROM evenement
            WHERE archive=false
            AND (titre LIKE ? OR description LIKE ? OR lieu LIKE ?)
            ORDER BY date_debut DESC
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String kw = "%" + keyword + "%";

            ps.setString(1, kw);
            ps.setString(2, kw);
            ps.setString(3, kw);

            ResultSet rs = ps.executeQuery();

            while (rs.next())
                list.add(mapRow(rs));

        } catch (Exception e) {
            logger.error("Erreur technique", e);
        }

        return list;
    }

    public Evenement findById(Long id) {

        String sql = "SELECT * FROM evenement WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (Exception e) {
            logger.error("Erreur technique", e);
        }

        return null;
    }

    public Evenement findActiveById(Long id) {

        String sql = """
            SELECT * FROM evenement
            WHERE id=? AND archive=false
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return mapRow(rs);
            }

        } catch (Exception e) {
            logger.error("Erreur technique", e);
        }

        return null;
    }

    public int countAll() {

        String sql = "SELECT COUNT(*) FROM evenement WHERE archive=false";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next())
                return rs.getInt(1);

        } catch (Exception e) {
            logger.error("Erreur technique", e);
        }

        return 0;
    }
}