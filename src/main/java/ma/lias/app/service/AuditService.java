package ma.lias.app.service;

import ma.lias.app.dao.AuditLogDAO;
import ma.lias.app.model.AuditLog;
import ma.lias.app.model.Utilisateur;

public class AuditService {

    private final AuditLogDAO dao = new AuditLogDAO();

    public void log(Utilisateur user,
                    String action,
                    String entityName,
                    Long entityId) {

        AuditLog log = new AuditLog();

        log.setUtilisateurId(user.getId());
        log.setEmailUtilisateur(user.getEmail());
        log.setAction(action);
        log.setEntityName(entityName);
        log.setEntityId(entityId);

        dao.save(log);
    }
}