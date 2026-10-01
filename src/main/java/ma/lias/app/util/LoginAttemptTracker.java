package ma.lias.app.util;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Protection anti brute-force basique sur /login.
 * En mémoire (suffisant pour une seule instance de serveur, contexte académique) :
 * après 5 échecs pour un même email, verrouille les tentatives 15 minutes.
 */
public class LoginAttemptTracker {

    private static final int MAX_TENTATIVES = 5;
    private static final long VERROUILLAGE_SECONDES = 15 * 60;

    private static class Etat {
        int echecs = 0;
        Instant dernierEchec = Instant.now();
    }

    private static final ConcurrentHashMap<String, Etat> tentatives = new ConcurrentHashMap<>();

    private LoginAttemptTracker() {}

    /** true si l'email est actuellement bloqué suite à trop d'échecs récents. */
    public static boolean estBloque(String email) {
        Etat etat = tentatives.get(cle(email));
        if (etat == null) return false;

        if (etat.echecs < MAX_TENTATIVES) return false;

        long secondesEcoulees = Instant.now().getEpochSecond() - etat.dernierEchec.getEpochSecond();
        if (secondesEcoulees >= VERROUILLAGE_SECONDES) {
            // Le verrouillage a expiré : on repart à zéro
            tentatives.remove(cle(email));
            return false;
        }
        return true;
    }

    /** Nombre de minutes restantes avant déverrouillage (pour le message d'erreur). */
    public static long minutesRestantes(String email) {
        Etat etat = tentatives.get(cle(email));
        if (etat == null) return 0;
        long secondesEcoulees = Instant.now().getEpochSecond() - etat.dernierEchec.getEpochSecond();
        long restant = VERROUILLAGE_SECONDES - secondesEcoulees;
        return Math.max(1, restant / 60 + 1);
    }

    public static void enregistrerEchec(String email) {
        tentatives.compute(cle(email), (k, etat) -> {
            if (etat == null) etat = new Etat();
            etat.echecs++;
            etat.dernierEchec = Instant.now();
            return etat;
        });
    }

    public static void reinitialiser(String email) {
        tentatives.remove(cle(email));
    }

    private static String cle(String email) {
        return email == null ? "" : email.trim().toLowerCase();
    }
}
