package ma.lias.app.dao;

import java.sql.*;
import java.util.*;

public class SuiviMaterielDAO {

    /** Règle d'éligibilité à l'attribution de matériel : un seul endroit à modifier. */
    private static final String ELIGIBLE = "m.actif = true AND m.statut = 'PERMANENT'";

    /** Un ligne par membre éligible, les moins servis en premier. */
    public List<Map<String, Object>> findSuiviMembres() {

        List<Map<String, Object>> list = new ArrayList<>();

        String sql =
            "SELECT m.id, m.prenom, m.nom, " +
            "       COUNT(a.id) AS total, " +
            "       COALESCE(SUM(CASE WHEN a.id IS NOT NULL AND a.date_retour IS NULL THEN 1 ELSE 0 END), 0) AS en_cours, " +
            "       MAX(a.date_attribution) AS derniere, " +
            "       GROUP_CONCAT(DISTINCT mat.nom ORDER BY mat.nom SEPARATOR ', ') AS materiels " +
            "FROM membre m " +
            "LEFT JOIN attribution_materiel a ON a.membre_id = m.id " +
            "LEFT JOIN materiel mat ON mat.id = a.materiel_id " +
            "WHERE " + ELIGIBLE + " " +
            "GROUP BY m.id, m.prenom, m.nom " +
            "ORDER BY total ASC, derniere ASC, m.nom ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("id", rs.getLong("id"));
                row.put("prenom", rs.getString("prenom"));
                row.put("nom", rs.getString("nom"));
                row.put("total", rs.getInt("total"));
                row.put("enCours", rs.getInt("en_cours"));
                row.put("derniere", rs.getDate("derniere"));
                row.put("materiels", rs.getString("materiels"));
                list.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /** Qui a reçu ce matériel, avec les dates. */
    public List<Map<String, Object>> findRecus(Long materielId) {

        List<Map<String, Object>> list = new ArrayList<>();

        String sql =
            "SELECT m.prenom, m.nom, a.date_attribution, a.date_retour , a.id AS attribution_id " +
            "FROM attribution_materiel a " +
            "JOIN membre m ON m.id = a.membre_id " +
            "WHERE a.materiel_id = ? " +
            "ORDER BY a.date_attribution DESC, a.id DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, materielId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("attributionId", rs.getLong("attribution_id"));
                row.put("prenom", rs.getString("prenom"));
                row.put("nom", rs.getString("nom"));
                row.put("dateAttribution", rs.getDate("date_attribution"));
                row.put("dateRetour", rs.getDate("date_retour"));
                list.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /** Membres éligibles qui n'ont JAMAIS reçu ce matériel. */
    public List<Map<String, Object>> findNonServis(Long materielId) {

        List<Map<String, Object>> list = new ArrayList<>();

        String sql =
            "SELECT m.prenom, m.nom FROM membre m " +
            "WHERE " + ELIGIBLE + " " +
            "AND NOT EXISTS (SELECT 1 FROM attribution_materiel a " +
            "                WHERE a.membre_id = m.id AND a.materiel_id = ?) " +
            "ORDER BY m.nom, m.prenom";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, materielId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("prenom", rs.getString("prenom"));
                row.put("nom", rs.getString("nom"));
                list.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}