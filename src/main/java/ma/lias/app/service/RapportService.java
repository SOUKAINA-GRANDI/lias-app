package ma.lias.app.service;

import java.util.List;

import ma.lias.app.dao.ConventionDAO;
import ma.lias.app.dao.EvenementDAO;
import ma.lias.app.dao.MembreDAO;
import ma.lias.app.dao.PublicationDAO;
import ma.lias.app.dao.RapportAnnuelDAO;
import ma.lias.app.model.Evenement;
import ma.lias.app.model.RapportAnnuel;
import ma.lias.app.model.RapportAnnuelData;

public class RapportService {

    private final EvenementDAO evenementDAO = new EvenementDAO();
    private final PublicationDAO publicationDAO = new PublicationDAO();
    private final ConventionDAO conventionDAO = new ConventionDAO();
    private final MembreDAO membreDAO = new MembreDAO();
    private final RapportAnnuelDAO rapportAnnuelDAO = new RapportAnnuelDAO();

    public RapportAnnuelData generer(int year) {

        RapportAnnuelData data = new RapportAnnuelData();

        int prevYear = year - 1;

        data.setYear(year);
        data.setPrevYear(prevYear);

        // ✅ Événements
        List<Evenement> eventsCurrent = evenementDAO.findByYear(year);
        List<Evenement> eventsPrev = evenementDAO.findByYear(prevYear);

        data.setEventsCurrent(eventsCurrent.size());
        data.setEventsPrev(eventsPrev.size());
        data.setEvenements(eventsCurrent);

        // ✅ Publications
        data.setPubCurrent(publicationDAO.findByYear(year).size());
        data.setPubPrev(publicationDAO.findByYear(prevYear).size());

        // ✅ Conventions
        data.setConvCurrent(conventionDAO.countByYear(year));
        data.setConvPrev(conventionDAO.countByYear(prevYear));

        // ✅ Membres actifs
        data.setMembresActifs(membreDAO.countAll());

        return data;
    }

    /** Génère le rapport de `year` et l'archive, sauf s'il existe déjà. */
    public boolean genererEtArchiver(int year) {

        if (rapportAnnuelDAO.existsForYear(year)) {
            return false; // déjà fait, on ne duplique pas
        }

        RapportAnnuelData data = generer(year);

        RapportAnnuel archive = new RapportAnnuel();
        archive.setAnnee(year);
        archive.setNbPublications(data.getPubCurrent());
        archive.setNbEvenements(data.getEventsCurrent());
        archive.setNbConventions(data.getConvCurrent());
        archive.setMembresActifs(data.getMembresActifs());

        rapportAnnuelDAO.save(archive);
        return true;
    }

    public List<RapportAnnuel> findHistorique() {
        return rapportAnnuelDAO.findAll();
    }
}