package ma.lias.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import ma.lias.app.model.Document;

public class DocumentDAO {

    public void save(Document d) {

        String sql = """
            INSERT INTO document
            (titre, type, chemin_fichier, evenement_id, date_upload, archive,
             version, document_parent_id, version_courante)
            VALUES (?, ?, ?, ?, NOW(), false, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, d.getTitre());
            ps.setString(2, d.getType());
            ps.setString(3, d.getCheminFichier());

            if (d.getEvenementId() != null) ps.setLong(4, d.getEvenementId());
            else ps.setNull(4, Types.BIGINT);

            ps.setInt(5, d.getVersion());

            if (d.getDocumentParentId() != null) ps.setLong(6, d.getDocumentParentId());
            else ps.setNull(6, Types.BIGINT);

            ps.setBoolean(7, d.isVersionCourante());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Document findById(Long id) {

        String sql = """
            SELECT d.*, e.titre AS evenement_titre
            FROM document d
            LEFT JOIN evenement e ON e.id = d.evenement_id
            WHERE d.id=?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Document> findAll() {
        return findAll("actifs");
    }

    public List<Document> findAll(String filtre) {

        List<Document> list = new ArrayList<>();

        String sql = "SELECT d.*, e.titre AS evenement_titre "
                   + "FROM document d "
                   + "LEFT JOIN evenement e ON e.id = d.evenement_id "
                   + "WHERE d.version_courante = 1 AND " + clauseArchive(filtre) + " "
                   + "ORDER BY d.date_upload DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(mapRow(rs));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Document> findByType(String type) {
        return findByType(type, "actifs");
    }

    public List<Document> findByType(String type, String filtre) {

        List<Document> list = new ArrayList<>();

        String sql = "SELECT d.*, e.titre AS evenement_titre "
                   + "FROM document d "
                   + "LEFT JOIN evenement e ON e.id = d.evenement_id "
                   + "WHERE d.version_courante = 1 AND " + clauseArchive(filtre) + " "
                   + "AND d.type = ? "
                   + "ORDER BY d.date_upload DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, type);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Document> search(String keyword) {
        return search(keyword, "actifs");
    }

    public List<Document> search(String keyword, String filtre) {

        List<Document> list = new ArrayList<>();

        String sql = "SELECT d.*, e.titre AS evenement_titre "
                   + "FROM document d "
                   + "LEFT JOIN evenement e ON e.id = d.evenement_id "
                   + "WHERE d.version_courante = 1 AND " + clauseArchive(filtre) + " "
                   + "AND d.titre LIKE ? "
                   + "ORDER BY d.date_upload DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public void archive(Long id) {

        String sql = "UPDATE document SET archive=true WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void desarchiver(Long id) {

        String sql = "UPDATE document SET archive=false WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setVersionCourante(Long id, boolean valeur) {

        String sql = "UPDATE document SET version_courante = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, valeur);
            ps.setLong(2, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Toutes les versions d'un même document (la ligne d'origine + ses remplaçants). */
    public List<Document> findVersions(Long origineId) {

        List<Document> list = new ArrayList<>();

        String sql = """
            SELECT d.*, e.titre AS evenement_titre
            FROM document d
            LEFT JOIN evenement e ON e.id = d.evenement_id
            WHERE d.id = ? OR d.document_parent_id = ?
            ORDER BY d.version DESC
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, origineId);
            ps.setLong(2, origineId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) list.add(mapRow(rs));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private Document mapRow(ResultSet rs) throws SQLException {
        Document d = new Document();
        d.setId(rs.getLong("id"));
        d.setTitre(rs.getString("titre"));
        d.setType(rs.getString("type"));
        d.setCheminFichier(rs.getString("chemin_fichier"));

        long evId = rs.getLong("evenement_id");
        if (!rs.wasNull()) d.setEvenementId(evId);

        Timestamp up = rs.getTimestamp("date_upload");
        if (up != null) d.setDateUpload(up.toLocalDateTime());

        d.setArchive(rs.getBoolean("archive"));
        d.setEvenementTitre(rs.getString("evenement_titre"));
        d.setVersion(rs.getInt("version"));

        long parentId = rs.getLong("document_parent_id");
        if (!rs.wasNull()) d.setDocumentParentId(parentId);

        d.setVersionCourante(rs.getBoolean("version_courante"));
        return d;
    }
    /** valeurs fixes uniquement : jamais d'injection possible. */
    /** valeurs fixes uniquement : jamais d'injection possible. */
    private String clauseArchive(String filtre) {
        if ("archives".equals(filtre)) return "d.archive = 1";
        if ("tous".equals(filtre))     return "1=1";
        return "d.archive = 0"; // défaut : actifs
    }
    public int count(String filtre) {

        String sql = "SELECT COUNT(*) FROM document d "
                + "WHERE d.version_courante = 1 AND " + clauseArchive(filtre);

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}