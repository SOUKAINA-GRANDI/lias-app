package ma.lias.app.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ma.lias.app.model.Convention;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import ma.lias.app.model.Evenement;
import ma.lias.app.model.Document;
public class ConventionDAO {

    private static final Logger logger = LoggerFactory.getLogger(ConventionDAO.class);


    // ✅ Compter toutes conventions actives
    public int countAll() {

        String sql = "SELECT COUNT(*) FROM convention WHERE archive = false";

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

    // ✅ Liste conventions actives
    public List<Convention> findAll() {

        List<Convention> list = new ArrayList<>();

        String sql = "SELECT * FROM convention WHERE archive = false ORDER BY date_creation DESC";

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

    // ✅ Trouver par ID
    public Convention findById(Long id) {

        String sql = "SELECT * FROM convention WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Convention c = new Convention();
                c.setId(rs.getLong("id"));
                c.setTitre(rs.getString("titre"));
                c.setPartenaire(rs.getString("partenaire"));
                c.setDateDebut(rs.getDate("date_debut").toLocalDate());

                if (rs.getDate("date_fin") != null)
                    c.setDateFin(rs.getDate("date_fin").toLocalDate());

                c.setDescription(rs.getString("description"));
                c.setCheminFichier(rs.getString("chemin_fichier"));
                c.setArchive(rs.getBoolean("archive"));

                return c;
            }

        } catch (Exception e) {
            logger.error("Erreur technique", e);
        }

        return null;
    }
    // ✅ Sauvegarde
    public void save(Convention convention) {

        String sql = """
            INSERT INTO convention
            (titre, partenaire, date_debut, date_fin,
             description, chemin_fichier)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, convention.getTitre());
            ps.setString(2, convention.getPartenaire());

            ps.setDate(3, Date.valueOf(convention.getDateDebut()));

            if (convention.getDateFin() != null)
                ps.setDate(4, Date.valueOf(convention.getDateFin()));
            else
                ps.setNull(4, Types.DATE);

            ps.setString(5, convention.getDescription());
            ps.setString(6, convention.getCheminFichier());

            ps.executeUpdate();

        } catch (Exception e) {
            logger.error("Erreur technique", e);
        }
    }

    // ✅ Update
    public void update(Convention c) {

        String sql = """
            UPDATE convention
            SET titre=?, partenaire=?, date_debut=?, date_fin=?, description=?
            WHERE id=?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getTitre());
            ps.setString(2, c.getPartenaire());
            ps.setDate(3, java.sql.Date.valueOf(c.getDateDebut()));

            if (c.getDateFin() != null)
                ps.setDate(4, java.sql.Date.valueOf(c.getDateFin()));
            else
                ps.setNull(4, java.sql.Types.DATE);

            ps.setString(5, c.getDescription());
            ps.setLong(6, c.getId());

            ps.executeUpdate();

        } catch (Exception e) {
            logger.error("Erreur technique", e);
        }
    }

    // ✅ Archivage logique
    public void archive(Long id) {

        String sql = "UPDATE convention SET archive=true WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            logger.error("Erreur technique", e);
        }
    }

    // ✅ Recherche améliorée (titre + partenaire)
    public List<Convention> search(String keyword) {

        List<Convention> list = new ArrayList<>();

        String sql = """
            SELECT * FROM convention
            WHERE archive = false
            AND (titre LIKE ? OR partenaire LIKE ?)
            ORDER BY date_creation DESC
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String kw = "%" + keyword + "%";

            ps.setString(1, kw);
            ps.setString(2, kw);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (Exception e) {
            logger.error("Erreur technique", e);
        }

        return list;
    }

    // ✅ Compter par année (compatible rapport annuel)
    public int countByYear(int year) {

        String sql = """
            SELECT COUNT(*)
            FROM convention
            WHERE archive = false
            AND YEAR(date_debut) = ?
        """;

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

    // ✅ Méthode mapping centralisée
    private Convention mapRow(ResultSet rs) throws SQLException {

        Convention c = new Convention();

        c.setId(rs.getLong("id"));
        c.setTitre(rs.getString("titre"));
        c.setPartenaire(rs.getString("partenaire"));

        Date dDebut = rs.getDate("date_debut");
        if (dDebut != null)
            c.setDateDebut(dDebut.toLocalDate());

        Date dFin = rs.getDate("date_fin");
        if (dFin != null)
            c.setDateFin(dFin.toLocalDate());

        c.setDescription(rs.getString("description"));
        c.setCheminFichier(rs.getString("chemin_fichier"));

        Timestamp tc = rs.getTimestamp("date_creation");
        if (tc != null)
            c.setDateCreation(tc.toLocalDateTime());

        return c;
    }
    
    public void lierEvenement(Long conventionId, Long evenementId) {

        String sql = "INSERT IGNORE INTO convention_evenement (convention_id, evenement_id) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, conventionId);
            ps.setLong(2, evenementId);
            ps.executeUpdate();

        } catch (Exception e) {
            logger.error("Erreur technique", e);
        }
    }

    public void delierEvenement(Long conventionId, Long evenementId) {

        String sql = "DELETE FROM convention_evenement WHERE convention_id = ? AND evenement_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, conventionId);
            ps.setLong(2, evenementId);
            ps.executeUpdate();

        } catch (Exception e) {
            logger.error("Erreur technique", e);
        }
    }

    public void lierDocument(Long conventionId, Long documentId) {

        String sql = "INSERT IGNORE INTO convention_document (convention_id, document_id) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, conventionId);
            ps.setLong(2, documentId);
            ps.executeUpdate();

        } catch (Exception e) {
            logger.error("Erreur technique", e);
        }
    }

    public void delierDocument(Long conventionId, Long documentId) {

        String sql = "DELETE FROM convention_document WHERE convention_id = ? AND document_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, conventionId);
            ps.setLong(2, documentId);
            ps.executeUpdate();

        } catch (Exception e) {
            logger.error("Erreur technique", e);
        }
    }

    public List<Evenement> findEvenementsAssocies(Long conventionId) {

        List<Evenement> list = new ArrayList<>();

        String sql = "SELECT e.id, e.titre, e.date_debut, e.type "
                   + "FROM convention_evenement ce "
                   + "JOIN evenement e ON e.id = ce.evenement_id "
                   + "WHERE ce.convention_id = ? "
                   + "ORDER BY e.date_debut DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, conventionId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Evenement e = new Evenement();
                e.setId(rs.getLong("id"));
                e.setTitre(rs.getString("titre"));
                if (rs.getTimestamp("date_debut") != null) {
                    e.setDateDebut(rs.getTimestamp("date_debut").toLocalDateTime());
                }
                e.setType(rs.getString("type"));
                list.add(e);
            }

        } catch (Exception e) {
            logger.error("Erreur technique", e);
        }
        return list;
    }

    public List<Document> findDocumentsAssocies(Long conventionId) {

        List<Document> list = new ArrayList<>();

        String sql = "SELECT d.id, d.titre, d.type, d.chemin_fichier "
                   + "FROM convention_document cd "
                   + "JOIN document d ON d.id = cd.document_id "
                   + "WHERE cd.convention_id = ? "
                   + "ORDER BY d.titre";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, conventionId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Document d = new Document();
                d.setId(rs.getLong("id"));
                d.setTitre(rs.getString("titre"));
                d.setType(rs.getString("type"));
                d.setCheminFichier(rs.getString("chemin_fichier"));
                list.add(d);
            }

        } catch (Exception e) {
            logger.error("Erreur technique", e);
        }
        return list;
    }
}