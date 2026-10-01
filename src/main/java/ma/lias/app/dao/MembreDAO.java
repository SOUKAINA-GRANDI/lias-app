package ma.lias.app.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ma.lias.app.model.Membre;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MembreDAO {

    private static final Logger logger = LoggerFactory.getLogger(MembreDAO.class);


    // ════════════════════════════════════════════════════════
    // LECTURE
    // ════════════════════════════════════════════════════════

    public List<Membre> findAll() {

        List<Membre> list = new ArrayList<>();
        String sql = "SELECT * FROM membre WHERE actif = true ORDER BY nom ASC";

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
    /** Réactivation logique : remet le membre actif et efface sa date de départ. */
    public void enable(Long id) {

        String sql = "UPDATE membre SET actif=true, date_depart=NULL WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            logger.error("Erreur technique", e);
        }
    }
    /** Variante pour l'annuaire public : inclut le nom de l'équipe (jointure). */
    public List<Membre> findAllPublic() {

        List<Membre> list = new ArrayList<>();
        String sql = """
            SELECT m.*, e.nom AS equipe_nom
            FROM membre m
            LEFT JOIN equipe e ON e.id = m.equipe_id
            WHERE m.actif = true
            ORDER BY m.nom ASC
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Membre m = mapRow(rs);
                m.setEquipeNom(rs.getString("equipe_nom"));
                list.add(m);
            }

        } catch (Exception e) {
            logger.error("Erreur technique", e);
        }
        return list;
    }

    public Membre findById(Long id) {

        String sql = "SELECT * FROM membre WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);

        } catch (Exception e) {
            logger.error("Erreur technique", e);
        }
        return null;
    }

    public Membre findByUserId(Long utilisateurId) {

        String sql = "SELECT * FROM membre WHERE utilisateur_id = ? AND actif = true";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, utilisateurId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);

        } catch (Exception e) {
            logger.error("Erreur technique", e);
        }
        return null;
    }

    public List<Membre> search(String keyword) {

        List<Membre> list = new ArrayList<>();
        String sql = """
            SELECT * FROM membre
            WHERE actif = true
            AND (nom LIKE ? OR prenom LIKE ? OR email LIKE ?)
            ORDER BY nom ASC
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String kw = "%" + keyword + "%";
            ps.setString(1, kw);
            ps.setString(2, kw);
            ps.setString(3, kw);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));

        } catch (Exception e) {
            logger.error("Erreur technique", e);
        }
        return list;
    }
    public void save(Membre m) {
        try (Connection conn = DBConnection.getConnection()) {
            save(conn, m);
        } catch (Exception e) {
            logger.error("Erreur technique", e);
        }
    }
    public List<Membre> findPaginated(int start, int limit) {

        List<Membre> list = new ArrayList<>();
        String sql = "SELECT * FROM membre WHERE actif = true ORDER BY nom ASC LIMIT ?, ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, start);
            ps.setInt(2, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));

        } catch (Exception e) {
            logger.error("Erreur technique", e);
        }
        return list;
    }

    public int countAll() {

        String sql = "SELECT COUNT(*) FROM membre WHERE actif = true";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) {
            logger.error("Erreur technique", e);
        }
        return 0;
    }

    // ════════════════════════════════════════════════════════
    // ÉCRITURE
    // ════════════════════════════════════════════════════════

    public void save(Connection conn, Membre m) throws SQLException {

        String sql = """
            INSERT INTO membre
              (nom, prenom, email, telephone, date_naissance,
               statut, `role`, equipe_id, utilisateur_id,
               date_affiliation, photo, biographie,
               centres_interet, etablissement_origine,
               laboratoire_origine, actif)
            VALUES (?,?,?,?,?, ?,?,?,?, ?,?,?,?,?, ?,true)
        """;

        try (PreparedStatement ps =
                     conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, m.getNom());
            ps.setString(2, m.getPrenom());
            ps.setString(3, m.getEmail());
            ps.setString(4, m.getTelephone());

            if (m.getDateNaissance() != null)
                ps.setDate(5, Date.valueOf(m.getDateNaissance()));
            else
                ps.setNull(5, Types.DATE);

            ps.setString(6, m.getStatut());
            ps.setString(7, m.getRole());

            if (m.getEquipeId() != null)
                ps.setLong(8, m.getEquipeId());
            else
                ps.setNull(8, Types.BIGINT);

            if (m.getUtilisateurId() != null)
                ps.setLong(9, m.getUtilisateurId());
            else
                ps.setNull(9, Types.BIGINT);

            ps.setDate(10, Date.valueOf(
                    m.getDateAffiliation() != null ?
                            m.getDateAffiliation()
                            : java.time.LocalDate.now()
            ));

            ps.setString(11, m.getPhoto());
            ps.setString(12, m.getBiographie());
            ps.setString(13, m.getCentresInteret());
            ps.setString(14, m.getEtablissementOrigine());
            ps.setString(15, m.getLaboratoireOrigine());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                m.setId(rs.getLong(1));
            }
        }
    }
    public void update(Membre m) {

        String sql = """
            UPDATE membre SET
              nom=?, prenom=?, email=?, telephone=?,
              statut=?, `role`=?, equipe_id=?,
              photo=?, biographie=?, centres_interet=?,
              etablissement_origine=?, laboratoire_origine=?,
              date_modification=NOW()
            WHERE id=?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, m.getNom());
            ps.setString(2, m.getPrenom());
            ps.setString(3, m.getEmail());
            ps.setString(4, m.getTelephone());
            ps.setString(5, m.getStatut());
            ps.setString(6, m.getRole());

            if (m.getEquipeId() != null)
                ps.setLong(7, m.getEquipeId());
            else
                ps.setNull(7, Types.BIGINT);

            ps.setString(8,  m.getPhoto());
            ps.setString(9,  m.getBiographie());
            ps.setString(10, m.getCentresInteret());
            ps.setString(11, m.getEtablissementOrigine());
            ps.setString(12, m.getLaboratoireOrigine());
            ps.setLong(13,   m.getId());

            ps.executeUpdate();

        } catch (Exception e) {
            logger.error("Erreur technique", e);
        }
    }

    /** Désactivation logique — ne supprime JAMAIS (règle RG-01 du CDC) */
    public void disable(Long id) {

        String sql = "UPDATE membre SET actif=false, date_depart=NOW() WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            logger.error("Erreur technique", e);
        }
    }

    // ════════════════════════════════════════════════════════
    // MAPPING — une seule méthode pour construire un Membre
    // ════════════════════════════════════════════════════════

    private Membre mapRow(ResultSet rs) throws SQLException {

        Membre m = new Membre();
        m.setId(rs.getLong("id"));
        m.setNom(rs.getString("nom"));
        m.setPrenom(rs.getString("prenom"));
        m.setEmail(rs.getString("email"));
        m.setTelephone(rs.getString("telephone"));
        m.setStatut(rs.getString("statut"));
        m.setRole(rs.getString("role"));
        m.setActif(rs.getBoolean("actif"));
        m.setPhoto(rs.getString("photo"));
        m.setBiographie(rs.getString("biographie"));
        m.setCentresInteret(rs.getString("centres_interet"));
        m.setEtablissementOrigine(rs.getString("etablissement_origine"));
        m.setLaboratoireOrigine(rs.getString("laboratoire_origine"));
        m.setDroitPublication(rs.getBoolean("droit_publication"));

        // Champs optionnels — peuvent être NULL en base
        Date dn = rs.getDate("date_naissance");
        if (dn != null) m.setDateNaissance(dn.toLocalDate());

        Date da = rs.getDate("date_affiliation");
        if (da != null) m.setDateAffiliation(da.toLocalDate());

        Date dd = rs.getDate("date_depart");
        if (dd != null) m.setDateDepart(dd.toLocalDate());

        long equipeId = rs.getLong("equipe_id");
        if (!rs.wasNull()) m.setEquipeId(equipeId);

        long userId = rs.getLong("utilisateur_id");
        if (!rs.wasNull()) m.setUtilisateurId(userId);

        Timestamp dc = rs.getTimestamp("date_creation");
        if (dc != null) m.setDateCreation(dc.toLocalDateTime());

        Timestamp dm = rs.getTimestamp("date_modification");
        if (dm != null) m.setDateModification(dm.toLocalDateTime());

        return m;
    }
    public void saveHistorique(Membre m) {

        String sql = """
            INSERT INTO membre_historique
            (membre_id, nom, prenom,
             telephone, biographie, centres_interet)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, m.getId());
            ps.setString(2, m.getNom());
            ps.setString(3, m.getPrenom());
            ps.setString(4, m.getTelephone());
            ps.setString(5, m.getBiographie());
            ps.setString(6, m.getCentresInteret());

            ps.executeUpdate();

        } catch (Exception e) {
            logger.error("Erreur technique", e);
        }
    }
    
    public Membre findByUtilisateurId(Long utilisateurId) {
        String sql = "SELECT * FROM membre WHERE utilisateur_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, utilisateurId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);   // ✅ construction complète et cohérente, comme partout ailleurs
            }
        } catch (Exception e) {
            logger.error("Erreur technique", e);
        }
        return null;
    }
    /** filtre : "actifs" (défaut), "inactifs" ou "tous" (valeurs fixes, pas d'injection possible). */
    private String clauseFiltre(String filtre) {
        if ("inactifs".equals(filtre)) return "WHERE actif = false";
        if ("tous".equals(filtre))     return "";
        return "WHERE actif = true";
    }

    public List<Membre> findPaginated(int start, int limit, String filtre) {

        List<Membre> list = new ArrayList<>();
        String sql = "SELECT * FROM membre " + clauseFiltre(filtre)
                   + " ORDER BY actif DESC, nom ASC LIMIT ?, ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, start);
            ps.setInt(2, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));

        } catch (Exception e) {
            logger.error("Erreur technique", e);
        }
        return list;
    }

    public int count(String filtre) {

        String sql = "SELECT COUNT(*) FROM membre " + clauseFiltre(filtre);

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) {
            logger.error("Erreur technique", e);
        }
        return 0;
    }
    
    public void updateDroitPublication(Long id, boolean valeur) {

        String sql = "UPDATE membre SET droit_publication = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, valeur);
            ps.setLong(2, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}