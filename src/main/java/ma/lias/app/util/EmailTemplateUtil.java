package ma.lias.app.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Moteur de templates email minimaliste : charge un fichier HTML depuis
 * classpath:/email-templates/, remplace les {{variables}}, et enveloppe le
 * tout dans le layout commun. Volontairement simple (pas de dépendance type
 * Thymeleaf/FreeMarker) pour rester cohérent avec l'architecture JEE pure du
 * reste du projet.
 */
public class EmailTemplateUtil {

    private static final Logger logger = LoggerFactory.getLogger(EmailTemplateUtil.class);
    private static final Pattern PLACEHOLDER = Pattern.compile("\\{\\{(\\w+)\\}\\}");

    private EmailTemplateUtil() {}

    /**
     * Rend un template de contenu, puis l'enveloppe dans le layout commun.
     *
     * @param templateName nom du fichier dans email-templates/ (ex: "adhesion-acceptee.html")
     * @param couleurBordure couleur CSS de la bordure gauche (ex: "#059669")
     * @param variables valeurs à injecter ; échappées HTML sauf si la clé finit par "Url" ou "Lien"
     */
    public static String render(String templateName, String couleurBordure, Map<String, String> variables) {

        String contenu = remplacerVariables(charger(templateName), variables);

        Map<String, String> varsLayout = Map.of("content", "__RAW__", "couleur", couleurBordure);
        String layout = charger("layout.html");

        // "content" ne doit pas être ré-échappé : on l'injecte après coup, tel quel.
        String resultat = layout.replace("{{couleur}}", couleurBordure)
                                 .replace("{{content}}", contenu);
        return resultat;
    }

    private static String remplacerVariables(String template, Map<String, String> variables) {
        Matcher matcher = PLACEHOLDER.matcher(template);
        StringBuilder sb = new StringBuilder();

        while (matcher.find()) {
            String cle = matcher.group(1);
            String valeur = variables.getOrDefault(cle, "");

            // Les liens ne sont pas échappés (ils contiennent des "&" légitimes dans l'URL),
            // tout le reste (saisi par un utilisateur) l'est pour éviter une injection HTML.
            boolean estUnLien = cle.toLowerCase().contains("lien") || cle.toLowerCase().contains("url");
            String remplacement = estUnLien ? valeur : escapeHtml(valeur);

            matcher.appendReplacement(sb, Matcher.quoteReplacement(remplacement));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private static String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    private static String charger(String nomFichier) {
        String chemin = "email-templates/" + nomFichier;
        try (InputStream in = EmailTemplateUtil.class.getClassLoader().getResourceAsStream(chemin)) {
            if (in == null) {
                throw new IllegalStateException("Template email introuvable : " + chemin);
            }
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.error("Impossible de charger le template email {}", chemin, e);
            throw new IllegalStateException("Erreur de lecture du template email : " + chemin, e);
        }
    }
}
