package ma.lias.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import ma.lias.app.model.MessageEvenement;

public class MessageEvenementDAO {

    public void envoyer(Long evenementId, Long expediteurId, String contenu) {
        String sql = """
            INSERT INTO message_evenement (evenement_id, expediteur_id, contenu)
            VALUES (?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, evenementId);
            ps.setLong(2, expediteurId);
            ps.setString(3, contenu);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ CORRIGÉ : Renvoie bien une List<MessageEvenement> avec le nom de l'expéditeur chargé via JOIN
    public List<MessageEvenement> findByEvenement(Long evenementId) {
        List<MessageEvenement> list = new ArrayList<>();

        String sql = """
            SELECT me.*, m.nom AS nom_expediteur 
            FROM message_evenement me
            LEFT JOIN membre m ON me.expediteur_id = m.utilisateur_id
            WHERE me.evenement_id = ?
            ORDER BY me.date_envoi ASC
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, evenementId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MessageEvenement m = new MessageEvenement();
                    m.setId(rs.getLong("id"));
                    m.setEvenementId(rs.getLong("evenement_id"));
                    m.setExpediteurId(rs.getLong("expediteur_id"));
                    m.setNomExpediteur(rs.getString("nom_expediteur")); // ✅ Plus d'erreur !
                    m.setContenu(rs.getString("contenu"));
                    m.setDateEnvoi(rs.getTimestamp("date_envoi").toLocalDateTime());

                    list.add(m);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}