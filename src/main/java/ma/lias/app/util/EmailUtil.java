package ma.lias.app.util;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailUtil {

    private static final Logger logger = LoggerFactory.getLogger(EmailUtil.class);

    private static final String FROM_EMAIL = "lias.club.app@gmail.com";

    private static final String PASSWORD =
            System.getenv("LIAS_EMAIL_PASSWORD");

    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";

    private static Session createSession() {

        Properties props = new Properties();

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        // ⚠️ mail.debug désactivé : en "true" il écrit le trafic SMTP (donc les
        // identifiants) en clair dans les logs — jamais souhaitable, encore
        // moins en production.

        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, PASSWORD);
            }
        });
    }
    public static void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            Session session = createSession();
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

            message.setSubject(subject, "UTF-8");
            message.setContent(htmlContent, "text/html; charset=UTF-8");

            Transport.send(message);
            logger.info("Email envoyé avec succès à : {}", to);

        } catch (Exception e) {
            logger.error("Erreur envoi email à {}", to, e);

            // On propage l'erreur pour ne pas mentir à l'utilisateur
            throw new RuntimeException("Impossible d'envoyer l'email de confirmation : " + e.getMessage(), e);
        }
    }
}