package ma.lias.app.dao;

import ma.lias.app.model.DemandeAdhesion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DemandeAdhesionDAO {

    // Requête de base : on joint l'équipe pour afficher son nom
    private static final String SELECT_BASE = """
        SELECT d.*, e.nom AS equipe_nom
        FROM demande_adhesion d
        LEFT JOIN equipe e ON e.id = d.equipe_id
    """;

    // ✅ Toutes demandes
    public List<DemandeAdhesion> findAll() {

        List<DemandeAdhesion> list = new ArrayList<>();
        String sql = SELECT_BASE + " ORDER BY d.date_demande DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(mapRow(rs));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ✅ Seulement EN_ATTENTE
    public List<DemandeAdhesion> findAllEnAttente() {

        List<DemandeAdhesion> list = new ArrayList<>();
        String sql = SELECT_BASE
                   + " WHERE d.statut = 'EN_ATTENTE' ORDER BY d.date_demande DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(mapRow(rs));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ✅ Trouver par ID
    public DemandeAdhesion findById(Long id) {

        String sql = SELECT_BASE + " WHERE d.id = ?";

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

    // ✅ Mise à jour statut (sécurisée)
    public void updateStatut(Long id, String statut) {

        String sql = """
            UPDATE demande_adhesion
            SET statut=?
            WHERE id=? AND statut='EN_ATTENTE'
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, statut);
            ps.setLong(2, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ Version transactionnelle
    public void updateStatut(Connection conn, Long id, String statut) throws SQLException {

        String sql = """
            UPDATE demande_adhesion
            SET statut=?
            WHERE id=? AND statut='EN_ATTENTE'
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, statut);
            ps.setLong(2, id);
            ps.executeUpdate();
        }
    }

    // ✅ Sauvegarde (avec statut visé, établissement, équipe)
    public void save(DemandeAdhesion d) {

        String sql = """
            INSERT INTO demande_adhesion
            (nom, prenom, email, cv_path, motivation,
             statut_vise, etablissement, equipe_id,
             statut, date_demande)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, d.getNom());
            ps.setString(2, d.getPrenom());
            ps.setString(3, d.getEmail());
            ps.setString(4, d.getCvPath());
            ps.setString(5, d.getMotivation());
            ps.setString(6, d.getStatutVise());
            ps.setString(7, d.getEtablissement());

            if (d.getEquipeId() != null) ps.setLong(8, d.getEquipeId());
            else ps.setNull(8, Types.BIGINT);

            ps.setString(9, d.getStatut());
            ps.setTimestamp(10, Timestamp.valueOf(d.getDateDemande()));

            ps.executeUpdate();

        } catch (Exception e) {
            // On ne perd plus une candidature en silence : l'erreur remonte
            throw new RuntimeException("Erreur lors de l'enregistrement de la demande d'adhésion", e);
        }
    }

    // ✅ Mapping centralisé
    private DemandeAdhesion mapRow(ResultSet rs) throws SQLException {

        DemandeAdhesion d = new DemandeAdhesion();

        d.setId(rs.getLong("id"));
        d.setNom(rs.getString("nom"));
        d.setPrenom(rs.getString("prenom"));
        d.setEmail(rs.getString("email"));
        d.setCvPath(rs.getString("cv_path"));
        d.setMotivation(rs.getString("motivation"));
        d.setStatut(rs.getString("statut"));
        d.setDateDemande(rs.getTimestamp("date_demande").toLocalDateTime());

        d.setStatutVise(rs.getString("statut_vise"));
        d.setEtablissement(rs.getString("etablissement"));
        long equipeId = rs.getLong("equipe_id");
        d.setEquipeId(rs.wasNull() ? null : equipeId);
        d.setEquipeNom(rs.getString("equipe_nom"));

        return d;
    }

    public int countEnAttente() {

        String sql = "SELECT COUNT(*) FROM demande_adhesion WHERE statut='EN_ATTENTE'";

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