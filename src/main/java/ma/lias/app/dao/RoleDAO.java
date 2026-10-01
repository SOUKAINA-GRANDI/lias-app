package ma.lias.app.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ma.lias.app.model.Role;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RoleDAO {

    private static final Logger logger = LoggerFactory.getLogger(RoleDAO.class);


    // ✅ Insère une nouvelle ligne de rôle (nouvelle période)
    public void save(Role r) {

        String sql = "INSERT INTO `role` (membre_id, nom, date_debut, date_fin) VALUES (?,?,?,?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, r.getMembreId());
            ps.setString(2, r.getNom());
            ps.setDate(3, Date.valueOf(r.getDateDebut()));
            if (r.getDateFin() != null) {
                ps.setDate(4, Date.valueOf(r.getDateFin()));
            } else {
                ps.setNull(4, Types.DATE);
            }

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                r.setId(rs.getLong(1));
            }

        } catch (Exception e) {
            logger.error("Erreur technique", e);
        }
    }

    // ✅ Clôture le(s) rôle(s) actif(s) d'un membre (date_fin = date donnée)
    public void cloturerActif(Long membreId, LocalDate dateFin) {

        String sql = "UPDATE `role` SET date_fin=? WHERE membre_id=? AND date_fin IS NULL";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(dateFin));
            ps.setLong(2, membreId);

            ps.executeUpdate();

        } catch (Exception e) {
            logger.error("Erreur technique", e);
        }
    }

    // ✅ Rôle actuellement actif d'un membre (date_fin IS NULL)
    public Role findActifByMembre(Long membreId) {

        String sql = "SELECT * FROM `role` WHERE membre_id=? AND date_fin IS NULL ORDER BY date_debut DESC LIMIT 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, membreId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRow(rs);
            }

        } catch (Exception e) {
            logger.error("Erreur technique", e);
        }
        return null;
    }

    // ✅ Le titulaire actif du rôle DIRECTEUR (utile pour vérification métier)
    public Role findDirecteurActif() {

        String sql = "SELECT * FROM `role` WHERE nom='DIRECTEUR' AND date_fin IS NULL ORDER BY date_debut DESC LIMIT 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return mapRow(rs);
            }

        } catch (Exception e) {
            logger.error("Erreur technique", e);
        }
        return null;
    }

    // ✅ Historique complet des rôles d'un membre (le plus récent en premier)
    public List<Role> findHistoriqueByMembre(Long membreId) {

        List<Role> list = new ArrayList<>();
        String sql = "SELECT * FROM `role` WHERE membre_id=? ORDER BY date_debut DESC";

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

    // ✅ Historique global (toutes les affectations de rôle, tous membres), avec nom/prénom
    public List<Role> findAllWithMembre() {

        List<Role> list = new ArrayList<>();
        String sql = """
            SELECT r.*, m.nom AS membre_nom, m.prenom AS membre_prenom
            FROM `role` r
            JOIN membre m ON m.id = r.membre_id
            ORDER BY r.date_debut DESC
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Role r = mapRow(rs);
                r.setMembreNom(rs.getString("membre_nom"));
                r.setMembrePrenom(rs.getString("membre_prenom"));
                list.add(r);
            }

        } catch (Exception e) {
            logger.error("Erreur technique", e);
        }
        return list;
    }

    private Role mapRow(ResultSet rs) throws SQLException {
        Role r = new Role();
        r.setId(rs.getLong("id"));
        r.setMembreId(rs.getLong("membre_id"));
        r.setNom(rs.getString("nom"));
        r.setDateDebut(rs.getDate("date_debut").toLocalDate());
        if (rs.getDate("date_fin") != null) {
            r.setDateFin(rs.getDate("date_fin").toLocalDate());
        }
        return r;
    }
    /** Rôles de gouvernance (Vice-directeur, Chef d'équipe) actifs à un moment
     *  quelconque pendant la période [debut, fin]. fin = null → mandat en cours. */
    public List<Role> findGouvernanceParPeriode(LocalDate debut, LocalDate fin) {

        List<Role> list = new ArrayList<>();

        String sql = """
            SELECT r.*, m.nom AS membre_nom, m.prenom AS membre_prenom
            FROM `role` r
            JOIN membre m ON m.id = r.membre_id
            WHERE r.nom IN ('VICE_DIRECTEUR', 'CHEF_EQUIPE')
              AND r.date_debut <= COALESCE(?, CURDATE())
              AND (r.date_fin IS NULL OR r.date_fin >= ?)
            ORDER BY r.nom, r.date_debut
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (fin != null) ps.setDate(1, Date.valueOf(fin)); else ps.setNull(1, Types.DATE);
            ps.setDate(2, Date.valueOf(debut));

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Role r = mapRow(rs);
                r.setMembreNom(rs.getString("membre_nom"));
                r.setMembrePrenom(rs.getString("membre_prenom"));
                list.add(r);
            }

        } catch (Exception e) {
            logger.error("Erreur technique", e);
        }
        return list;
}
    }