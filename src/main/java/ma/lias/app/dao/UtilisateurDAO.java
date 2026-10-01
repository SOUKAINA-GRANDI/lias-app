package ma.lias.app.dao;

import ma.lias.app.model.Utilisateur;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAO {

	public Utilisateur findByEmail(String email) {
	    String sql = "SELECT * FROM utilisateur WHERE email = ? AND actif = true";

	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {

	        ps.setString(1, email);
	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {
	            Utilisateur u = new Utilisateur();
	            u.setId(rs.getLong("id"));
	            u.setEmail(rs.getString("email"));
	            u.setPassword(rs.getString("password"));
	            u.setType(rs.getString("type"));
	            u.setActif(rs.getBoolean("actif"));
	            // 🚫 RETRAIT de u.setNom() et u.setPrenom() ici car la table utilisateur ne les possède pas !
	            return u;
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return null;
	}

    public void save(Utilisateur utilisateur) {
        String sql = "INSERT INTO utilisateur (email, password, type, nom, prenom) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, utilisateur.getEmail());
            ps.setString(2, utilisateur.getPassword());
            ps.setString(3, utilisateur.getType());
            ps.setString(4, utilisateur.getNom());
            ps.setString(5, utilisateur.getPrenom());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Utilisateur> findAll() {
        List<Utilisateur> list = new ArrayList<>();
        
        // On fait un LEFT JOIN pour récupérer le nom/prénom depuis la table membre s'ils existent
        String sql = "SELECT u.*, m.nom, m.prenom FROM utilisateur u " +
                     "LEFT JOIN membre m ON u.email = m.email " + // Ajuste m.email si ta colonne s'appelle autrement (ex: email_utilisateur)
                     "ORDER BY u.id DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Utilisateur u = new Utilisateur();
                u.setId(rs.getLong("id"));
                u.setEmail(rs.getString("email"));
                u.setType(rs.getString("type"));
                u.setActif(rs.getBoolean("actif"));
                
                // Récupération sécurisée de la date de création
                java.sql.Timestamp ts = rs.getTimestamp("date_creation");
                if (ts != null) {
                    u.setDateCreation(ts.toLocalDateTime());
                }
                
                // Récupération du nom et prénom (qui viennent du LEFT JOIN)
                u.setNom(rs.getString("nom"));
                u.setPrenom(rs.getString("prenom"));

                list.add(u);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public void toggleActif(Long id, boolean actif) {
        String sql = "UPDATE utilisateur SET actif=? WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, actif);
            ps.setLong(2, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save(Connection conn, Utilisateur u) throws SQLException {
        String sql = """
            INSERT INTO utilisateur
            (email, password, type, actif, nom, prenom, date_creation, activation_token, token_expiration)
            VALUES (?, ?, ?, ?, ?, ?, NOW(), ?, ?)
        """;

        try (PreparedStatement ps =
                     conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, u.getEmail());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getType());
            ps.setBoolean(4, u.isActif());
            ps.setString(5, u.getNom());
            ps.setString(6, u.getPrenom());
            ps.setString(7, u.getActivationToken());
            if (u.getTokenExpiration() != null) {
                ps.setTimestamp(8, Timestamp.valueOf(u.getTokenExpiration()));
            } else {
                ps.setNull(8, Types.TIMESTAMP);
            }

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                u.setId(rs.getLong(1));
            }
        }
    }

    /** Utilisateur ayant un lien d'activation valide (non expiré) correspondant au token. */
    public Utilisateur findByActivationToken(String token) {

        String sql = "SELECT * FROM utilisateur WHERE activation_token=? AND token_expiration > NOW()";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, token);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Utilisateur u = new Utilisateur();
                u.setId(rs.getLong("id"));
                u.setEmail(rs.getString("email"));
                u.setType(rs.getString("type"));
                u.setActif(rs.getBoolean("actif"));
                u.setActivationToken(rs.getString("activation_token"));
                return u;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /** Le membre choisit son mot de passe : on l'enregistre et on invalide le lien d'activation. */
    public void activerCompte(Long id, String motDePasseHache) {

        String sql = "UPDATE utilisateur SET password=?, activation_token=NULL, token_expiration=NULL WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, motDePasseHache);
            ps.setLong(2, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Utilisé pour distinguer "mauvais mot de passe" de "compte pas encore activé" lors du login. */
    public boolean activationEnAttente(String email) {

        String sql = "SELECT activation_token FROM utilisateur WHERE email=? AND token_expiration > NOW()";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getString("activation_token") != null;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Utilisateur findById(Long id) {
        String sql = "SELECT u.*, m.nom, m.prenom FROM utilisateur u " +
                     "LEFT JOIN membre m ON u.email = m.email WHERE u.id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Utilisateur u = new Utilisateur();
                u.setId(rs.getLong("id"));
                u.setEmail(rs.getString("email"));
                u.setType(rs.getString("type"));
                u.setActif(rs.getBoolean("actif"));
                u.setNom(rs.getString("nom"));
                u.setPrenom(rs.getString("prenom"));
                return u;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updatePassword(Long id, String password) {
        String sql = "UPDATE utilisateur SET password=? WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, password);
            ps.setLong(2, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public List<Long> findAllIds() {
        List<Long> ids = new ArrayList<>();
        String sql = "SELECT id FROM utilisateur WHERE actif=true";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ids.add(rs.getLong("id"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ids;
    }
    public List<Utilisateur> findByQuery(String query) {
        List<Utilisateur> list = new ArrayList<>();
        
        // Le filtre cherche si la requête est contenue dans l'email, le nom ou le prénom
        String sql = "SELECT u.*, m.nom, m.prenom FROM utilisateur u " +
                     "LEFT JOIN membre m ON u.email = m.email " +
                     "WHERE u.email LIKE ? OR m.nom LIKE ? OR m.prenom LIKE ? " +
                     "ORDER BY u.id DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String searchPattern = "%" + query + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Utilisateur u = new Utilisateur();
                    u.setId(rs.getLong("id"));
                    u.setEmail(rs.getString("email"));
                    u.setType(rs.getString("type"));
                    u.setActif(rs.getBoolean("actif"));
                    
                    java.sql.Timestamp ts = rs.getTimestamp("date_creation");
                    if (ts != null) {
                        u.setDateCreation(ts.toLocalDateTime());
                    }
                    
                    u.setNom(rs.getString("nom"));
                    u.setPrenom(rs.getString("prenom"));
                    list.add(u);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public void definirTokenReset(Long id, String token, java.time.LocalDateTime expiration) {

        String sql = "UPDATE utilisateur SET activation_token=?, token_expiration=? WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, token);
            ps.setTimestamp(2, java.sql.Timestamp.valueOf(expiration));
            ps.setLong(3, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /** Comme findByEmail, mais SANS filtrer sur actif — utilisée uniquement
     *  pour distinguer, après un échec de connexion, "mauvais mot de passe"
     *  d'un "compte désactivé (retraité/ancien membre)". */
    public Utilisateur findByEmailInclusInactif(String email) {

        String sql = "SELECT * FROM utilisateur WHERE email = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Utilisateur u = new Utilisateur();
                u.setId(rs.getLong("id"));
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("password"));
                u.setType(rs.getString("type"));
                u.setActif(rs.getBoolean("actif"));
                return u;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}