package ma.lias.app.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    private static final int LOG_ROUNDS = 12;

    // ✅ Hash mot de passe
    public static String hash(String plainPassword) {

        if (plainPassword == null || plainPassword.isEmpty()) {
            throw new IllegalArgumentException("Mot de passe vide");
        }

        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(LOG_ROUNDS));
    }

    // ✅ Vérification mot de passe
    public static boolean verify(String plainPassword, String hashedPassword) {

        if (plainPassword == null || hashedPassword == null) {
            return false;
        }

        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}