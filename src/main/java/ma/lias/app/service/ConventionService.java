package ma.lias.app.service;

import ma.lias.app.dao.ConventionDAO;
import ma.lias.app.exception.BusinessException;
import ma.lias.app.model.Convention;
import ma.lias.app.model.Document;
import ma.lias.app.model.Evenement;

import java.util.List;

public class ConventionService {

    private final ConventionDAO dao = new ConventionDAO();

    // ✅ Créer convention
    public void create(Convention c) {

        if (c.getDateFin() != null &&
            c.getDateDebut().isAfter(c.getDateFin())) {

            throw new BusinessException("Dates invalides");
        }

        dao.save(c);
    }

    // ✅ Modifier convention
    public void update(Convention c) {

        if (c.getId() == null)
            throw new BusinessException("ID manquant");

        if (c.getDateFin() != null &&
            c.getDateDebut().isAfter(c.getDateFin())) {

            throw new BusinessException("Dates invalides");
        }

        dao.update(c);
    }

    // ✅ Archiver convention (soft delete)
    public void archive(Long id) {

        if (id == null)
            throw new BusinessException("ID invalide");

        dao.archive(id);
    }

    // ✅ Récupérer toutes les conventions
    public List<Convention> findAll() {
        return dao.findAll();
    }

    // ✅ Récupérer par ID
    public Convention findById(Long id) {

        if (id == null)
            throw new BusinessException("ID invalide");

        return dao.findById(id);
    }

    // ✅ Rapport
    public int countByYear(int year) {
        return dao.countByYear(year);
    }
    public List<Evenement> findEvenementsAssocies(Long conventionId) {
        return dao.findEvenementsAssocies(conventionId);
    }

    public List<Document> findDocumentsAssocies(Long conventionId) {
        return dao.findDocumentsAssocies(conventionId);
    }

    public void lierEvenement(Long conventionId, Long evenementId) {
        dao.lierEvenement(conventionId, evenementId);
    }

    public void delierEvenement(Long conventionId, Long evenementId) {
        dao.delierEvenement(conventionId, evenementId);
    }

    public void lierDocument(Long conventionId, Long documentId) {
        dao.lierDocument(conventionId, documentId);
    }

    public void delierDocument(Long conventionId, Long documentId) {
        dao.delierDocument(conventionId, documentId);
    }
}