package ma.lias.app.service;

import ma.lias.app.dao.MaterielDAO;
import ma.lias.app.exception.BusinessException;
import ma.lias.app.model.Materiel;

import java.util.List;

public class MaterielService {

    private final MaterielDAO dao = new MaterielDAO();

    public List<Materiel> findAll() {
        return dao.findAll();
    }

    public void create(Materiel m) {

        if (m.getQuantiteTotale() <= 0)
            throw new BusinessException("Quantité invalide");

        // Au début disponible = totale
        m.setQuantiteDisponible(m.getQuantiteTotale());

        dao.save(m);
    }

    public Materiel findById(Long id) {
        return dao.findById(id);
    }
}