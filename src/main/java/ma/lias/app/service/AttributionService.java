package ma.lias.app.service;

import java.sql.Connection;
import java.sql.SQLException;

import ma.lias.app.dao.AttributionMaterielDAO;
import ma.lias.app.dao.DBConnection;
import ma.lias.app.dao.MaterielDAO;
import ma.lias.app.exception.BusinessException;
import ma.lias.app.model.AttributionMateriel;
import ma.lias.app.model.Membre;

public class AttributionService {

    private final AttributionMaterielDAO attributionDAO = new AttributionMaterielDAO();
    private final MaterielDAO materielDAO = new MaterielDAO();
    private final MembreService membreService = new MembreService();

    public void attribuer(Long materielId, Long membreId) {

        if (materielDAO.findById(materielId) == null)
            throw new BusinessException("Matériel introuvable");
        Membre beneficiaire = membreService.findById(membreId);
        if (beneficiaire == null || !beneficiaire.isActif()
                || !"PERMANENT".equals(beneficiaire.getStatut())) {
            throw new BusinessException("Le bénéficiaire doit être un membre permanent actif.");
        }

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // Décrément atomique : échoue s'il ne reste plus de stock (même en accès concurrent)
                if (!materielDAO.decrementStock(conn, materielId))
                    throw new BusinessException("Stock insuffisant");

                attributionDAO.attribuer(conn, materielId, membreId);
                conn.commit();

            } catch (Exception e) {
                conn.rollback();
                if (e instanceof BusinessException be) throw be;
                throw new BusinessException("Erreur lors de l'attribution du matériel.", e);
            }
        } catch (SQLException e) {
            throw new BusinessException("Erreur technique de connexion à la base.", e);
        }
    }
    public void retourner(Long attributionId) {

        AttributionMateriel attribution = attributionDAO.findById(attributionId);

        if (attribution == null)
            throw new BusinessException("Attribution introuvable");

        if (attribution.getDateRetour() != null)
            throw new BusinessException("Ce matériel a déjà été retourné.");

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                boolean ok = attributionDAO.retourner(conn, attributionId);

                if (!ok) {
                    throw new BusinessException("Ce matériel a déjà été retourné.");
                }

                materielDAO.incrementStock(conn, attribution.getMaterielId());
                conn.commit();

            } catch (Exception e) {
                conn.rollback();
                if (e instanceof BusinessException be) throw be;
                throw new BusinessException("Erreur lors de l'enregistrement du retour.", e);
            }
        } catch (java.sql.SQLException e) {
            throw new BusinessException("Erreur technique de connexion à la base.", e);
        }
    }
}