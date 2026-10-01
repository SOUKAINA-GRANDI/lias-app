package ma.lias.app.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ma.lias.app.model.Membre;
import ma.lias.app.model.MembreHistorique;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MembreHistoriqueDAO {

    private static final Logger logger = LoggerFactory.getLogger(MembreHistoriqueDAO.class);


    /**
     * Snapshot AVANT modification (CDC §3 ⭐) : nom, prénom, téléphone, bio,
     * centres d'intérêt, statut, rôle, équipe, photo. Rien n'est écrasé,
     * chaque appel crée une nouvelle ligne horodatée.
     */
    public void saveHistorique(Membre m) {

        String sql = """
            INSERT INTO membre_historique
            (membre_id, nom, prenom, telephone, biographie, centres_interet,
             statut, `role`, equipe_id, photo)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, m.getId());
            ps.setString(2, m.getNom());
            ps.setString(3, m.getPrenom());
            ps.setString(4, m.getTelephone());
            ps.setString(5, m.getBiographie());
            ps.setString(6, m.getCentresInteret());
            ps.setString(7, m.getStatut());
            ps.setString(8, m.getRole());
            if (m.getEquipeId() != null) {
                ps.setLong(9, m.getEquipeId());
            } else {
                ps.setNull(9, Types.BIGINT);
            }
            ps.setString(10, m.getPhoto());

            ps.executeUpdate();

        } catch (Exception e) {
            logger.error("Erreur technique", e);
        }
    }

    /** Historique complet d'un membre, le plus récent en premier. */
    public List<MembreHistorique> findByMembreId(Long membreId) {

        List<MembreHistorique> list = new ArrayList<>();

        String sql = """
            SELECT h.*, e.nom AS equipe_nom
            FROM membre_historique h
            LEFT JOIN equipe e ON e.id = h.equipe_id
            WHERE h.membre_id = ?
            ORDER BY h.date_modification DESC
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, membreId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (Exception e) {
            logger.error("Erreur technique", e);
        }
        return list;
    }

    private MembreHistorique mapRow(ResultSet rs) throws SQLException {

        MembreHistorique h = new MembreHistorique();
        h.setId(rs.getLong("id"));
        h.setMembreId(rs.getLong("membre_id"));
        h.setNom(rs.getString("nom"));
        h.setPrenom(rs.getString("prenom"));
        h.setTelephone(rs.getString("telephone"));
        h.setBiographie(rs.getString("biographie"));
        h.setCentresInteret(rs.getString("centres_interet"));
        h.setStatut(rs.getString("statut"));
        h.setRole(rs.getString("role"));
        if (rs.getObject("equipe_id") != null) {
            h.setEquipeId(rs.getLong("equipe_id"));
        }
        h.setPhoto(rs.getString("photo"));
        h.setEquipeNom(rs.getString("equipe_nom"));
        if (rs.getTimestamp("date_modification") != null) {
            h.setDateModification(rs.getTimestamp("date_modification").toLocalDateTime());
        }
        return h;
    }
}