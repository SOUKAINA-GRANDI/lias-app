package ma.lias.app.service;

import ma.lias.app.dao.UtilisateurDAO;
import ma.lias.app.model.Utilisateur;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {

    private UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    public Utilisateur authenticate(String email, String password) {

        Utilisateur user = utilisateurDAO.findByEmail(email);

        if (user == null) {
            return null;
        }

        if (user.getPassword() == null) {
            return null;
        }

        try {
            if (!BCrypt.checkpw(password, user.getPassword())) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }

        return user;
    }
    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
    
    /** Vérifie le mot de passe même sur un compte désactivé (usage : message de connexion différencié). */
    public Utilisateur authenticateInclusInactif(String email, String password) {

        Utilisateur user = utilisateurDAO.findByEmailInclusInactif(email);

        if (user == null || user.getPassword() == null) {
            return null;
        }

        try {
            if (!BCrypt.checkpw(password, user.getPassword())) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }

        return user;
    }
   
}