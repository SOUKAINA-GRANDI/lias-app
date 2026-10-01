package ma.lias.app.service;

import ma.lias.app.dao.LaboratoireDAO;
import ma.lias.app.exception.BusinessException;
import ma.lias.app.model.Laboratoire;

public class LaboratoireService {

    private final LaboratoireDAO dao = new LaboratoireDAO();

    public Laboratoire get() {
        Laboratoire l = dao.find();
        if (l == null) {
            // Sécurité : si la ligne n'existe pas encore, on en crée une par défaut
            l = new Laboratoire();
            l.setNom("Laboratoire LIAS");
            dao.insert(l);
        }
        return l;
    }

    public void update(Laboratoire l) {

        if (l.getNom() == null || l.getNom().isBlank())
            throw new BusinessException("Le nom du laboratoire est obligatoire.");

        Laboratoire existant = dao.find();
        if (existant == null) {
            dao.insert(l);
        } else {
            l.setId(existant.getId());
            dao.update(l);
        }
    }
}
