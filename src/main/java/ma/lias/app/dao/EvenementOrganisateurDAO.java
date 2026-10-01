package ma.lias.app.dao;

import java.sql.*;
import java.util.*;

public class EvenementOrganisateurDAO {

    /** Ids des membres organisateurs d'un événement. */
    public Set<Long> findIdsByEvenement(Long evenementId) {

        Set<Long> ids = new LinkedHashSet<>();
        String sql = "SELECT membre_id FROM evenement_organisateur WHERE evenement_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, evenementId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) ids.add(rs.getLong(1));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ids;
    }

    /** "Prénom Nom, Prénom Nom" pour un événement (chaîne vide s'il n'y en a pas). */
    public String findNomsByEvenement(Long evenementId) {

        String sql = """
            SELECT GROUP_CONCAT(CONCAT(m.prenom, ' ', m.nom) ORDER BY m.nom SEPARATOR ', ')
            FROM evenement_organisateur eo
            JOIN membre m ON m.id = eo.membre_id
            WHERE eo.evenement_id = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, evenementId);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getString(1) != null) return rs.getString(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /** evenementId -> "Nom1, Nom2" pour tous les événements (une seule requête). */
    public Map<Long, String> findNomsParEvenement() {

        Map<Long, String> map = new HashMap<>();

        String sql = """
            SELECT eo.evenement_id,
                   GROUP_CONCAT(CONCAT(m.prenom, ' ', m.nom) ORDER BY m.nom SEPARATOR ', ') AS noms
            FROM evenement_organisateur eo
            JOIN membre m ON m.id = eo.membre_id
            GROUP BY eo.evenement_id
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) map.put(rs.getLong("evenement_id"), rs.getString("noms"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /** Remplace la liste des organisateurs d'un événement (transaction). */
    public void remplacer(Long evenementId, Collection<Long> membreIds) {

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement del = conn.prepareStatement(
                        "DELETE FROM evenement_organisateur WHERE evenement_id = ?")) {
                    del.setLong(1, evenementId);
                    del.executeUpdate();
                }

                try (PreparedStatement ins = conn.prepareStatement(
                        "INSERT INTO evenement_organisateur (evenement_id, membre_id) VALUES (?, ?)")) {
                    for (Long membreId : membreIds) {
                        ins.setLong(1, evenementId);
                        ins.setLong(2, membreId);
                        ins.addBatch();
                    }
                    ins.executeBatch();
                }

                conn.commit();

            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'affectation des organisateurs", e);
        }
    }
}