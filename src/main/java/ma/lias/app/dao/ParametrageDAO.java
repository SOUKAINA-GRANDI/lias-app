package ma.lias.app.dao;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ParametrageDAO {

    public Map<String, String> findAll() {

        Map<String, String> map = new HashMap<>();

        String sql = "SELECT * FROM parametrage";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                map.put(
                        rs.getString("cle"),
                        rs.getString("valeur")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    public void update(String cle,
                       String valeur) {

        String sql = """
            UPDATE parametrage
            SET valeur=?
            WHERE cle=?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, valeur);
            ps.setString(2, cle);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}