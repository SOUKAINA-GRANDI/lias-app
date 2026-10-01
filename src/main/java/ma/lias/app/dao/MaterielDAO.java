package ma.lias.app.dao;

import ma.lias.app.model.Materiel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaterielDAO {

    public List<Materiel> findAll() {
        List<Materiel> list = new ArrayList<>();
        String sql = "SELECT * FROM materiel ORDER BY id DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Materiel m = new Materiel();
                m.setId(rs.getLong("id"));
                m.setNom(rs.getString("nom"));
                m.setType(rs.getString("type"));
                m.setMarque(rs.getString("marque"));
                m.setQuantiteTotale(rs.getInt("quantite_totale"));
                m.setQuantiteDisponible(rs.getInt("quantite_disponible"));
                m.setDateAchat(rs.getDate("date_achat"));

                list.add(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public void incrementStock(Connection conn, Long id) throws SQLException {

        String sql = "UPDATE materiel SET quantite_disponible = quantite_disponible + 1 WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
    public boolean decrementStock(Connection conn, Long id) throws SQLException {
        String sql = """
            UPDATE materiel
            SET quantite_disponible = quantite_disponible - 1
            WHERE id=? AND quantite_disponible > 0
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public void save(Materiel m) {
        String sql = "INSERT INTO materiel (nom, type, marque, quantite_totale, quantite_disponible, date_achat) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, m.getNom());
            ps.setString(2, m.getType());
            ps.setString(3, m.getMarque());
            ps.setInt(4, m.getQuantiteTotale());
            ps.setInt(5, m.getQuantiteDisponible());
            ps.setDate(6, m.getDateAchat()); 
            
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la sauvegarde du matériel : " + e.getMessage(), e);
        }
    }

    public boolean decrementStock(Long id) {
        String sql = """
            UPDATE materiel
            SET quantite_disponible = quantite_disponible - 1
            WHERE id=? AND quantite_disponible > 0
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            int rows = ps.executeUpdate();
            return rows > 0; // ✅ Permet de savoir si le décrément a fonctionné

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Version renommé en français si ton service appelle 'decrementerStock' au lieu de 'decrementStock'
    public void decrementerStock(Long id) {
        this.decrementStock(id);
    }

    public void incrementStock(Long id) {
        String sql = """
            UPDATE materiel
            SET quantite_disponible =
                CASE
                    WHEN quantite_disponible < quantite_totale
                    THEN quantite_disponible + 1
                    ELSE quantite_disponible
                END
            WHERE id=?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Materiel findById(Long id) {
        String sql = "SELECT * FROM materiel WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Materiel m = new Materiel();
                    m.setId(rs.getLong("id"));
                    m.setNom(rs.getString("nom"));
                    m.setType(rs.getString("type"));
                    m.setMarque(rs.getString("marque"));
                    m.setQuantiteTotale(rs.getInt("quantite_totale"));
                    m.setQuantiteDisponible(rs.getInt("quantite_disponible"));
                    m.setDateAchat(rs.getDate("date_achat")); 

                    return m;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}