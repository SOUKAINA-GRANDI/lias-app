package ma.lias.app.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ma.lias.app.dao.*;

public class SearchService {

    /** Du moins privilégié au plus privilégié. */
    public enum Niveau { PUBLIC, DOCTORANT, ASSOCIE, PERMANENT, DIRECTION }

    private final MembreDAO membreDAO = new MembreDAO();
    private final EvenementDAO evenementDAO = new EvenementDAO();
    private final PublicationDAO publicationDAO = new PublicationDAO();
    private final ConventionDAO conventionDAO = new ConventionDAO();
    private final ReunionDAO reunionDAO = new ReunionDAO();
    private final DocumentDAO documentDAO = new DocumentDAO();

    public Map<String, Object> search(String keyword, Niveau niveau) {
        Map<String, Object> results = new HashMap<>();

        // Par défaut : rien (les JSP lisent ces clés)
        results.put("membres", new ArrayList<>());
        results.put("documents", new ArrayList<>());
        results.put("conventions", new ArrayList<>());
        results.put("reunions", new ArrayList<>());

        // Public : événements et publications (déjà visibles sur le site)
        results.put("evenements", evenementDAO.search(keyword));
        results.put("publications", publicationDAO.search(keyword));

        // Connectés (doctorant et plus)
        if (niveau.ordinal() >= Niveau.DOCTORANT.ordinal()) {
            results.put("membres", membreDAO.search(keyword));
        }
        // Associé et plus : documents
        if (niveau.ordinal() >= Niveau.ASSOCIE.ordinal()) {
            results.put("documents", documentDAO.search(keyword));
        }
        // Permanent et plus : modules internes
        if (niveau.ordinal() >= Niveau.PERMANENT.ordinal()) {
            results.put("conventions", conventionDAO.search(keyword));
            results.put("reunions", reunionDAO.search(keyword));
        }
        return results;
    }
}