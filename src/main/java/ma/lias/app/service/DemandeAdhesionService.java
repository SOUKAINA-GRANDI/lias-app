package ma.lias.app.service;
import ma.lias.app.enums.StatutMembre;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ma.lias.app.dao.*;
import ma.lias.app.enums.StatutDemande;
import ma.lias.app.exception.BusinessException;
import ma.lias.app.model.*;
import ma.lias.app.util.PasswordUtil;
import ma.lias.app.util.EmailUtil;
import ma.lias.app.dao.MandatDAO;
import ma.lias.app.dao.UtilisateurDAO;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class DemandeAdhesionService {

    private static final Logger logger = LoggerFactory.getLogger(DemandeAdhesionService.class);


    private final DemandeAdhesionDAO demandeDAO = new DemandeAdhesionDAO();
    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
    private final MembreDAO membreDAO = new MembreDAO();
    private final NotificationDAO notifDAO = new NotificationDAO();
    private final AffiliationHistoriqueDAO affiliDAO = new AffiliationHistoriqueDAO();
    private final NotificationService notificationService = new NotificationService();
    private final MandatDAO mandatDAO = new MandatDAO();

    public DemandeAdhesion findById(Long demandeId) {
        return demandeDAO.findById(demandeId);
    }

    public List<DemandeAdhesion> findAllEnAttente() {
        return demandeDAO.findAllEnAttente();
    }
    // ✅ Notifie le directeur en mandat qu'une nouvelle demande est arrivée
    public void notifierNouvelleDemande(DemandeAdhesion demande) {

        Mandat mandat = mandatDAO.findActif();
        if (mandat == null || mandat.getDirecteurId() == null) return;

        Membre directeur = membreDAO.findById(mandat.getDirecteurId());
        if (directeur != null && directeur.getUtilisateurId() != null) {
            notificationService.notifyUser(directeur.getUtilisateurId(),
                    "Nouvelle demande d'adhésion de " + demande.getPrenom()
                            + " " + demande.getNom() + ".");
        }
    }
    public void valider(Long demandeId) {
        valider(demandeId, StatutMembre.PERMANENT);
    }

    // ✅ Méthode de validation nettoyée et opérationnelle
    public void valider(Long demandeId, StatutMembre statut) {
        if (statut == null
                || statut == StatutMembre.RETRAITE
                || statut == StatutMembre.ANCIEN) {
            throw new BusinessException("Statut invalide pour une nouvelle adhésion.");
        }

        DemandeAdhesion demande = demandeDAO.findById(demandeId);

        if (demande == null)
            throw new BusinessException("Demande introuvable dans la base de données.");

        if (!StatutDemande.EN_ATTENTE.name().equals(demande.getStatut()))
            throw new BusinessException("Cette demande a déjà été traitée.");

        // Sécurité : le compte n'a PAS de mot de passe utilisable tant qu'il n'est pas activé.
        // On stocke un hash d'une valeur aléatoire (impossible à deviner/utiliser), et on
        // envoie un LIEN D'ACTIVATION à durée de vie limitée plutôt qu'un mot de passe en clair.
        String activationToken = UUID.randomUUID().toString().replace("-", "");
        String mdpPlaceholderInutilisable = UUID.randomUUID().toString();

        try (Connection conn = DBConnection.getConnection()) {

            conn.setAutoCommit(false);

            try {
                // 1. Créer le compte utilisateur
                Utilisateur utilisateur = new Utilisateur();
                utilisateur.setEmail(demande.getEmail());
                utilisateur.setPassword(PasswordUtil.hash(mdpPlaceholderInutilisable));
                utilisateur.setType("MEMBRE"); 
                utilisateur.setActif(true);
                utilisateur.setActivationToken(activationToken);
                utilisateur.setTokenExpiration(java.time.LocalDateTime.now().plusHours(72));

                utilisateurDAO.save(conn, utilisateur);

                // 2. Créer la fiche membre correspondante
                Membre membre = new Membre();
                membre.setNom(demande.getNom());
                membre.setPrenom(demande.getPrenom());
                membre.setEmail(demande.getEmail());
                membre.setStatut(statut.name());
                membre.setEtablissementOrigine(demande.getEtablissement());
                membre.setEquipeId(demande.getEquipeId());
                membre.setDateAffiliation(LocalDate.now());
                membre.setActif(true);
                membre.setUtilisateurId(utilisateur.getId());

                membreDAO.save(conn, membre);

                // 3. Enregistrer l'historique d'affiliation
                affiliDAO.startAffiliation(conn, membre.getId());

                // 4. Mettre à jour le statut de la demande avec la bonne valeur ENUM 'ACCEPTEE'
                demandeDAO.updateStatut(conn, demandeId, "ACCEPTEE");
                
                conn.commit(); // 💾 Sauvegarde SQL sécurisée passée avec succès !

                // 5. Notification interne au système (Isolée pour ne pas bloquer)
                try {
                    notifDAO.create(utilisateur.getId(), "Votre demande a été acceptée.");
                } catch (Exception notifEx) {
                    logger.error("Erreur technique", notifEx); 
                }

                // 6. Envoi de l'e-mail avec un LIEN D'ACTIVATION (valable 72h) plutôt qu'un mot de passe en clair
                try {
                    String lienActivation = ma.lias.app.config.Constantes.APP_BASE_URL
                            + "/activation?token=" + activationToken;

                    String sujet = "Bienvenue au Laboratoire LIAS - Activez votre compte";
                    String html = ma.lias.app.util.EmailTemplateUtil.render(
                            "adhesion-acceptee.html",
                            "#059669",
                            java.util.Map.of(
                                    "prenom", demande.getPrenom(),
                                    "nom", demande.getNom(),
                                    "lienActivation", lienActivation,
                                    "email", demande.getEmail()
                            )
                    );

                    EmailUtil.sendHtmlEmail(demande.getEmail(), sujet, html);
                } catch (Exception mailEx) {
                    logger.error("Erreur technique", mailEx);
                }

            } catch (Exception e) {
                conn.rollback(); 
                throw new BusinessException("Erreur lors de la validation de la transaction SQL : " + e.getMessage(), e);
            }

        } catch (Exception e) {
            throw new BusinessException("Erreur technique de connexion au serveur : " + e.getMessage(), e);
        }
    }

    // ✅ Refuser une demande
    public void refuser(Long demandeId) {
        DemandeAdhesion demande = demandeDAO.findById(demandeId);

        if (demande == null)
            throw new BusinessException("Demande introuvable");

        if (!StatutDemande.EN_ATTENTE.name().equals(demande.getStatut()))
            throw new BusinessException("Demande déjà traitée");

        demandeDAO.updateStatut(demandeId, "REFUSEE");
        
        try {
            String sujet = "Mise à jour concernant votre demande d'adhésion - LIAS";
            String html = ma.lias.app.util.EmailTemplateUtil.render(
                    "adhesion-refusee.html",
                    "#ef4444",
                    java.util.Map.of(
                            "prenom", demande.getPrenom(),
                            "nom", demande.getNom()
                    )
            );
            
            EmailUtil.sendHtmlEmail(demande.getEmail(), sujet, html);
        } catch (Exception ignored) {}
    }
}