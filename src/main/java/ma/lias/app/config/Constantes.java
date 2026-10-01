package ma.lias.app.config;

public class Constantes {

    public static final int SESSION_TIMEOUT = 1800;

    public static final int MAX_FILE_SIZE = 2_000_000;

    public static final int AUDIT_PAGE_SIZE = 20;

    /**
     * URL de base de l'application (sans slash final), utilisée pour construire
     * les liens envoyés par email (activation de compte, etc.).
     * Configurable via -Dlias.base.url=https://mondomaine.fr/lias-app
     */
    public static final String APP_BASE_URL = resolveBaseUrl();

    private static String resolveBaseUrl() {
        String configured = System.getProperty("lias.base.url");
        if (configured != null && !configured.isBlank()) {
            return configured.endsWith("/") ? configured.substring(0, configured.length() - 1) : configured;
        }
        return "http://localhost:8080/lias-app";
    }

    /**
     * Dossier racine de stockage des fichiers (photos, CV, PV, documents...).
     * Portable : configurable via la propriété système "lias.upload.dir"
     * (ex: -Dlias.upload.dir=/var/lias_uploads au démarrage de Tomcat),
     * sinon repli automatique sur un dossier dans le répertoire personnel
     * de l'utilisateur qui exécute le serveur (fonctionne sur Windows/Linux/Mac).
     */
    public static final String UPLOAD_DIR = resolveUploadDir();

    private static String resolveUploadDir() {
        String configured = System.getProperty("lias.upload.dir");
        if (configured != null && !configured.isBlank()) {
            return normalize(configured);
        }
        String base = System.getProperty("user.home", ".");
        return normalize(base + java.io.File.separator + "lias_uploads");
    }

    private static String normalize(String path) {
        String p = path.replace("\\", "/");
        return p.endsWith("/") ? p : p + "/";
    }
}