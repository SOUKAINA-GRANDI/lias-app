package ma.lias.app.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ma.lias.app.model.Mandat;

public class MandatDAO {

    // ✅ Historique complet
    public List<Mandat> findAll() {

        List<Mandat> list = new ArrayList<>();

        String sql = """
            SELECT ma.*, me.nom AS directeur_nom, me.prenom AS directeur_prenom
            FROM mandat ma
            LEFT JOIN membre me ON me.id = ma.directeur_id
            ORDER BY ma.date_debut DESC
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public Mandat findActif() {

        String sql = """
            SELECT ma.*, me.nom AS directeur_nom, me.prenom AS directeur_prenom
            FROM mandat ma
            LEFT JOIN membre me ON me.id = ma.directeur_id
            WHERE ma.date_debut <= CURDATE()
            AND (ma.date_fin IS NULL OR ma.date_fin > CURDATE())
            LIMIT 1
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return mapRow(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // ✅ Création mandat
    public void save(Mandat m) {

        String sql = """
            INSERT INTO mandat
            (date_debut, date_fin, directeur_id)
            VALUES (?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(m.getDateDebut()));

            if (m.getDateFin() != null)
                ps.setDate(2, Date.valueOf(m.getDateFin()));
            else
                ps.setNull(2, Types.DATE);

            ps.setLong(3, m.getDirecteurId());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ Clôturer mandat actif
    public void cloturer(Long id, LocalDate dateFin) {

        String sql = """
            UPDATE mandat
            SET date_fin=?
            WHERE id=? AND date_fin IS NULL
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(dateFin));
            ps.setLong(2, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Mandat mapRow(ResultSet rs) throws SQLException {

        Mandat m = new Mandat();

        m.setId(rs.getLong("id"));
        m.setDateDebut(rs.getDate("date_debut").toLocalDate());

        Date df = rs.getDate("date_fin");
        if (df != null)
            m.setDateFin(df.toLocalDate());

        m.setDirecteurId(rs.getLong("directeur_id"));
        m.setDirecteurNom(rs.getString("directeur_nom"));
        m.setDirecteurPrenom(rs.getString("directeur_prenom"));
        return m;
    }
}