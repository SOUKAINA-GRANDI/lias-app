package ma.lias.app.service;

import java.util.List;

import ma.lias.app.dao.ReunionDAO;
import ma.lias.app.exception.BusinessException;
import ma.lias.app.model.Reunion;

public class ReunionService {

    private final ReunionDAO dao = new ReunionDAO();

    /**
     * Crée une nouvelle réunion après validation des données de base.
     */
    public void create(Reunion r) {
        validateReunion(r);
        if (r.getPvPath() == null) {
            r.setPvPath("");
        }
        dao.save(r);
    }

    /**
     * Met à jour une réunion existante (Détails ou Rédaction du PV)
     */
    public void update(Reunion r) {
        if (r.getId() == null) {
            throw new BusinessException("Impossible de modifier une réunion sans un ID valide.");
        }
        
        // On applique les mêmes règles métier (Titre et Date obligatoires)
        validateReunion(r);
        
        // Appel de la méthode de mise à jour du DAO
        dao.update(r);
    }

    /**
     * Archive une réunion par son identifiant.
     */
    public void archive(Long id) {
        if (id == null) {
            throw new BusinessException("L'identifiant de la réunion est obligatoire pour l'archivage.");
        }
        dao.archive(id);
    }

    /**
     * Récupère toutes les réunions non archivées.
     */
    public List<Reunion> findAll() {
        return dao.findAll();
    }

    /**
     * Recherche une réunion spécifique par son ID.
     */
    public Reunion findById(Long id) {
        if (id == null) {
            return null;
        }
        return dao.findById(id);
    }

    /**
     * Factoring des règles de validation métier communes (Données obligatoires).
     */
    private void validateReunion(Reunion r) {
        if (r == null) {
            throw new BusinessException("Les données de la réunion sont absentes.");
        }

        if (r.getTitre() == null || r.getTitre().isBlank()) {
            throw new BusinessException("Le titre de la réunion est obligatoire.");
        }

        if (r.getDateReunion() == null) {
            throw new BusinessException("La date de la réunion est obligatoire.");
        }
    }
}