package ma.lias.app.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;

public class MembreEquipeDAO {

    private static final Logger logger = LoggerFactory.getLogger(MembreEquipeDAO.class);


    public void assigner(Long membreId, Long equipeId) {

        String sql = "INSERT INTO membre_equipe (membre_id, equipe_id, date_debut) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, membreId);
            ps.setLong(2, equipeId);
            ps.setDate(3, Date.valueOf(LocalDate.now()));

            ps.executeUpdate();

        } catch (Exception e) {
            logger.error("Erreur technique", e);
        }
    }

    public void retirer(Long membreId, Long equipeId) {

        String sql = "UPDATE membre_equipe SET date_fin=? WHERE membre_id=? AND equipe_id=? AND date_fin IS NULL";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(LocalDate.now()));
            ps.setLong(2, membreId);
            ps.setLong(3, equipeId);

            ps.executeUpdate();

        } catch (Exception e) {
            logger.error("Erreur technique", e);
        }
    }

    /** Clôture toute affectation active du membre, quelle que soit l'équipe
     *  (utilisé quand un membre change d'équipe). */
    public void cloturerActifDuMembre(Long membreId) {

        String sql = "UPDATE membre_equipe SET date_fin=? WHERE membre_id=? AND date_fin IS NULL";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(LocalDate.now()));
            ps.setLong(2, membreId);

            ps.executeUpdate();

        } catch (Exception e) {
            logger.error("Erreur technique", e);
        }
    }
}