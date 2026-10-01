package ma.lias.app.dao;

import ma.lias.app.model.Laboratoire;

import java.sql.*;

public class LaboratoireDAO {

    /** Table singleton : on prend toujours la première ligne (id le plus petit). */
    public Laboratoire find() {

        String sql = "SELECT * FROM laboratoire ORDER BY id ASC LIMIT 1";

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

    public void insert(Laboratoire l) {

        String sql = """
            INSERT INTO laboratoire (nom, description, date_creation, adresse, email_contact, telephone, site_web)
            VALUES (?,?,?,?,?,?,?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            bind(ps, l);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) l.setId(rs.getLong(1));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(Laboratoire l) {

        String sql = """
            UPDATE laboratoire SET
              nom=?, description=?, date_creation=?, adresse=?,
              email_contact=?, telephone=?, site_web=?, date_modification=NOW()
            WHERE id=?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            bind(ps, l);
            ps.setLong(8, l.getId());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bind(PreparedStatement ps, Laboratoire l) throws SQLException {
        ps.setString(1, l.getNom());
        ps.setString(2, l.getDescription());
        if (l.getDateCreation() != null) {
            ps.setDate(3, Date.valueOf(l.getDateCreation()));
        } else {
            ps.setNull(3, Types.DATE);
        }
        ps.setString(4, l.getAdresse());
        ps.setString(5, l.getEmailContact());
        ps.setString(6, l.getTelephone());
        ps.setString(7, l.getSiteWeb());
    }

    private Laboratoire mapRow(ResultSet rs) throws SQLException {
        Laboratoire l = new Laboratoire();
        l.setId(rs.getLong("id"));
        l.setNom(rs.getString("nom"));
        l.setDescription(rs.getString("description"));
        if (rs.getDate("date_creation") != null) {
            l.setDateCreation(rs.getDate("date_creation").toLocalDate());
        }
        l.setAdresse(rs.getString("adresse"));
        l.setEmailContact(rs.getString("email_contact"));
        l.setTelephone(rs.getString("telephone"));
        l.setSiteWeb(rs.getString("site_web"));
        if (rs.getTimestamp("date_modification") != null) {
            l.setDateModification(rs.getTimestamp("date_modification").toLocalDateTime());
        }
        return l;
    }
}
