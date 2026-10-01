package ma.lias.app.service;

import ma.lias.app.dao.PublicationDAO;
import ma.lias.app.exception.BusinessException;
import ma.lias.app.model.Publication;

import java.util.List;

public class PublicationService {

    private final PublicationDAO dao = new PublicationDAO();

    public void create(Publication p) {

        if (p.getTitre() == null || p.getTitre().isBlank())
            throw new BusinessException("Titre obligatoire");

        if (p.getAnnee() <= 0)
            throw new BusinessException("Année invalide");

        dao.save(p);
    }

    public void update(Publication p, Long membreId) {

        Publication existing = dao.findById(p.getId());

        if (existing == null)
            throw new BusinessException("Publication introuvable");

        if (!existing.getMembreId().equals(membreId))
            throw new BusinessException("Accès refusé");

        dao.update(p);
    }

    public void softDelete(Long id, Long membreId) {

        Publication p = dao.findById(id);

        if (p == null)
            throw new BusinessException("Publication introuvable");

        if (!p.getMembreId().equals(membreId))
            throw new BusinessException("Accès refusé");

        dao.softDelete(id);
    }

    public List<Publication> findByMembre(Long membreId) {
        return dao.findByMembre(membreId);
    }

    public Publication findById(Long id) {
        return dao.findById(id);
    }
    public List<Publication> findAll() {
        return dao.findAll();
    }

	public int countAll() {return dao.countAll();}
}