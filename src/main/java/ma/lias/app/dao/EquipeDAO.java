package ma.lias.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import ma.lias.app.model.Equipe;

public class EquipeDAO {
    public int countMembres(Long equipeId) {

        String sql = "SELECT COUNT(*) FROM membre WHERE equipe_id = ? AND actif = true";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, equipeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Equipe> findAll() {

        List<Equipe> list = new ArrayList<>();

        String sql = """
            SELECT e.*, m.nom AS chef_nom, m.prenom AS chef_prenom
            FROM equipe e
            LEFT JOIN membre m ON e.chef_id = m.id
            ORDER BY e.id DESC
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Equipe e = new Equipe();
                e.setId(rs.getLong("id"));
                e.setNom(rs.getString("nom"));
                e.setDescription(rs.getString("description"));
                e.setChefId(rs.getObject("chef_id") != null ? rs.getLong("chef_id") : null);

                String chefNom = rs.getString("chef_nom");
                String chefPrenom = rs.getString("chef_prenom");

                if (chefNom != null) {
                    e.setChefNom(chefNom + " " + chefPrenom);
                }

                list.add(e);   // ✅ IMPORTANT
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public Equipe findById(Long id) {

        String sql = """
            SELECT e.*, m.nom AS chef_nom, m.prenom AS chef_prenom
            FROM equipe e
            LEFT JOIN membre m ON e.chef_id = m.id
            WHERE e.id=?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Equipe e = new Equipe();
                e.setId(rs.getLong("id"));
                e.setNom(rs.getString("nom"));
                e.setDescription(rs.getString("description"));
                e.setChefId(rs.getObject("chef_id") != null ? rs.getLong("chef_id") : null);

                String chefNom = rs.getString("chef_nom");
                String chefPrenom = rs.getString("chef_prenom");

                if (chefNom != null) {
                    e.setChefNom(chefNom + " " + chefPrenom);
                }

                return e;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public void save(Equipe equipe) {

        String sql = "INSERT INTO equipe (nom, description, chef_id) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, equipe.getNom());
            ps.setString(2, equipe.getDescription());

            if (equipe.getChefId() != null)
                ps.setLong(3, equipe.getChefId());
            else
                ps.setNull(3, Types.BIGINT);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(Equipe equipe) {

        String sql = "UPDATE equipe SET nom=?, description=?, chef_id=? WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, equipe.getNom());
            ps.setString(2, equipe.getDescription());

            if (equipe.getChefId() != null)
                ps.setLong(3, equipe.getChefId());
            else
                ps.setNull(3, Types.BIGINT);

            ps.setLong(4, equipe.getId());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(Long id) {

        String sql = "DELETE FROM equipe WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void archive(Long id) {

        String sql = "UPDATE equipe SET archive = true WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void assignerMembre(Long equipeId, Long membreId) {

        String sql = "UPDATE membre SET equipe_id=? WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, equipeId);
            ps.setLong(2, membreId);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public int countAll() {

        String sql = "SELECT COUNT(*) FROM equipe";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}