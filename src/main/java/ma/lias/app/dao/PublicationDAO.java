package ma.lias.app.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ma.lias.app.model.Publication;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PublicationDAO {

    private static final Logger logger = LoggerFactory.getLogger(PublicationDAO.class);


    // ✅ Liste globale (non supprimées)
   
	public List<Publication> findAll() {

	    List<Publication> list = new ArrayList<>();

	    String sql = """
	        SELECT * FROM publication
	        WHERE supprime = false
	        ORDER BY annee DESC
	    """;

	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {

	        while (rs.next()) {
	            list.add(mapRow(rs));
	        }

	    } catch (Exception e) {
	        logger.error("Erreur technique", e);
	    }

	    return list;
	}

    public List<Publication> findAllPublic() {

        List<Publication> list = new ArrayList<>();

        String sql = """
            SELECT * FROM publication
            WHERE supprime = false AND archive = false
            ORDER BY annee DESC
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (Exception e) {
            logger.error("Erreur technique", e);
        }

        return list;
    }

    // ✅ Sauvegarde
    public void save(Publication p) {

        String sql = """
            INSERT INTO publication 
            (titre, auteurs, annee, type, description, membre_id, supprime) 
            VALUES (?, ?, ?, ?, ?, ?, false)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getTitre());
            ps.setString(2, p.getAuteurs());
            ps.setInt(3, p.getAnnee());
            ps.setString(4, p.getType());
            ps.setString(5, p.getDescription());
            ps.setLong(6, p.getMembreId());

            ps.executeUpdate();

        } catch (Exception e) {
            logger.error("Erreur technique", e);
        }
    }

    // ✅ Modifier
    public void update(Publication p) {

        String sql = """
            UPDATE publication
            SET titre=?, auteurs=?, annee=?, type=?, description=?
            WHERE id=? AND supprime=false
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getTitre());
            ps.setString(2, p.getAuteurs());
            ps.setInt(3, p.getAnnee());
            ps.setString(4, p.getType());
            ps.setString(5, p.getDescription());
            ps.setLong(6, p.getId());

            ps.executeUpdate();

        } catch (Exception e) {
            logger.error("Erreur technique", e);
        }
    }

    // ✅ Soft delete
    public void softDelete(Long id) {

        String sql = "UPDATE publication SET supprime=true WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            logger.error("Erreur technique", e);
        }
    }

    // ✅ Trouver par ID (non supprimée)
    public Publication findById(Long id) {

        String sql = "SELECT * FROM publication WHERE id=? AND supprime=false";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
                return mapRow(rs);

        } catch (Exception e) {
            logger.error("Erreur technique", e);
        }

        return null;
    }

    // ✅ Par membre
    public List<Publication> findByMembre(Long membreId) {

        List<Publication> list = new ArrayList<>();

        String sql = """
            SELECT * FROM publication
            WHERE membre_id=? AND supprime=false
            ORDER BY annee DESC
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, membreId);
            ResultSet rs = ps.executeQuery();

            while (rs.next())
                list.add(mapRow(rs));

        } catch (Exception e) {
            logger.error("Erreur technique", e);
        }

        return list;
    }

    // ✅ Recherche
    public List<Publication> search(String keyword) {

        List<Publication> list = new ArrayList<>();

        String sql = """
            SELECT * FROM publication
            WHERE titre LIKE ? AND supprime=false
            ORDER BY annee DESC
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next())
                list.add(mapRow(rs));

        } catch (Exception e) {
            logger.error("Erreur technique", e);
        }

        return list;
    }

    public int countByYear(int year) {

        String sql = "SELECT COUNT(*) FROM publication WHERE annee=? AND supprime=false";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, year);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
                return rs.getInt(1);

        } catch (Exception e) {
            logger.error("Erreur technique", e);
        }

        return 0;
    }

    // ✅ Mapping centralisé
    private Publication mapRow(ResultSet rs) throws SQLException {

        Publication p = new Publication();

        p.setId(rs.getLong("id"));
        p.setTitre(rs.getString("titre"));
        p.setAuteurs(rs.getString("auteurs"));
        p.setAnnee(rs.getInt("annee"));
        p.setType(rs.getString("type"));
        p.setDescription(rs.getString("description"));

        long membreId = rs.getLong("membre_id");
        if (!rs.wasNull()) {
            p.setMembreId(membreId);
        }

        Timestamp ts = rs.getTimestamp("date_creation");
        if (ts != null) {
            p.setDateCreation(ts.toLocalDateTime());
        }

        return p;
    }
    public List<Publication> findByYear(int year) {

        List<Publication> list = new ArrayList<>();

        String sql = """
            SELECT * FROM publication
            WHERE annee=? AND supprime=false
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
    public int countAll() {

        String sql = """
            SELECT COUNT(*) 
            FROM publication 
            WHERE supprime = false
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            logger.error("Erreur technique", e);
        }

        return 0;
    }
}