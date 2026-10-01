package ma.lias.app.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import ma.lias.app.service.RapportService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@WebListener
public class RapportAnnuelListener implements ServletContextListener {

    private ScheduledExecutorService scheduler;
    private final RapportService rapportService = new RapportService();

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "rapport-annuel-scheduler");
            t.setDaemon(true);
            return t;
        });

        // 1. Rattrapage immédiat : si le serveur était éteint le 1er janvier,
        //    on génère quand même le rapport de l'année précédente.
        scheduler.execute(() -> genererAnneePrecedenteEnSecurite());

        // 2. Planification récurrente : chaque 1er janvier à 00h05.
        planifierProchaineExecution();
    }

    private void planifierProchaineExecution() {

        LocalDateTime maintenant = LocalDateTime.now();
        LocalDate prochain1erJanvier = (maintenant.getMonthValue() == 1 && maintenant.getDayOfMonth() == 1
                && maintenant.toLocalTime().isBefore(LocalTime.of(0, 5)))
                ? maintenant.toLocalDate()
                : LocalDate.of(maintenant.getYear() + 1, 1, 1);

        LocalDateTime prochainDeclenchement = prochain1erJanvier.atTime(0, 5);
        long delaiMinutes = ChronoUnit.MINUTES.between(maintenant, prochainDeclenchement);

        ScheduledFuture<?> tache = scheduler.schedule(() -> {
            genererAnneePrecedenteEnSecurite();
            planifierProchaineExecution(); // se replanifie pour l'année suivante
        }, delaiMinutes, TimeUnit.MINUTES);
    }

    private void genererAnneePrecedenteEnSecurite() {
        try {
            int anneePrecedente = LocalDate.now().getYear() - 1;
            rapportService.genererEtArchiver(anneePrecedente);
        } catch (Exception e) {
            e.printStackTrace(); // on ne doit jamais faire planter le serveur pour ça
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (scheduler != null) {
            scheduler.shutdownNow();
        }
    }
}