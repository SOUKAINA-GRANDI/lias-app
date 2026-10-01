package ma.lias.app.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    private static final Logger logger = LoggerFactory.getLogger(DBConnection.class);


    // Valeurs par défaut de secours (si db.properties est absent ET aucune
    // variable d'environnement définie) — pour ne jamais casser le démarrage.
    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/lias_db"
            + "?useSSL=false&serverTimezone=UTC"
            + "&useUnicode=true&characterEncoding=UTF-8&connectionCollation=utf8mb4_unicode_ci";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASSWORD = "root";

    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;

    static {
        Properties props = new Properties();
        try (InputStream in = DBConnection.class.getClassLoader()
                .getResourceAsStream("db.properties")) {
            if (in != null) {
                props.load(in);
            }
        } catch (IOException e) {
            logger.error("Erreur technique", e);
        }

        // Ordre de priorité : variable d'environnement > db.properties > défaut
        URL      = envOuProps("DB_URL", props, "db.url", DEFAULT_URL);
        USER     = envOuProps("DB_USER", props, "db.user", DEFAULT_USER);
        PASSWORD = envOuProps("DB_PASSWORD", props, "db.password", DEFAULT_PASSWORD);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Erreur chargement Driver MySQL", e);
        }
    }

    private static String envOuProps(String envKey, Properties props, String propKey, String defaut) {
        String env = System.getenv(envKey);
        if (env != null && !env.isBlank()) return env;

        String prop = props.getProperty(propKey);
        if (prop != null && !prop.isBlank()) return prop;

        return defaut;
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
