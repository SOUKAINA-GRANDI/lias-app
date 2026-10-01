package ma.lias.app.service;

import ma.lias.app.config.Constantes;
import ma.lias.app.dao.DocumentDAO;
import ma.lias.app.enums.TypeDocument;
import ma.lias.app.exception.BusinessException;
import ma.lias.app.model.Document;
import ma.lias.app.util.FileUploadUtil;

import jakarta.servlet.http.Part;

import java.util.List;

public class DocumentService {

    private final DocumentDAO dao = new DocumentDAO();

    /** Upload + enregistrement d'un nouveau document (CDC §10). */
    public void upload(String titre, String type, Long evenementId, Part fichier) {

        if (titre == null || titre.isBlank())
            throw new BusinessException("Le titre du document est obligatoire.");

        if (type == null || type.isBlank())
            throw new BusinessException("Le type du document est obligatoire.");

        try {
            TypeDocument.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Type de document invalide.");
        }

        if (fichier == null || fichier.getSize() == 0)
            throw new BusinessException("Le fichier est obligatoire.");

        String chemin = FileUploadUtil.sauvegarderDocument(fichier, Constantes.UPLOAD_DIR, "documents");
        if (chemin == null)
            throw new BusinessException("Échec de l'enregistrement du fichier.");

        Document d = new Document();
        d.setTitre(titre);
        d.setType(type);
        d.setCheminFichier(chemin);
        d.setEvenementId(evenementId);

        dao.save(d);
    }

    public List<Document> findAll() {
        return dao.findAll();
    }

    public List<Document> findByType(String type) {
        if (type == null || type.isBlank()) return dao.findAll();
        return dao.findByType(type);
    }

    public List<Document> search(String keyword) {
        return dao.search(keyword);
    }

    public Document findById(Long id) {
        if (id == null) throw new BusinessException("ID invalide");
        return dao.findById(id);
    }

    public void archive(Long id) {
        dao.archive(id);
    }

    public void desarchiver(Long id) {
        dao.desarchiver(id);
    }
    /** Dépose une nouvelle version d'un document existant, sans supprimer l'ancienne. */
    public void remplacer(Long documentId, Part nouveauFichier) {

        Document ancien = dao.findById(documentId);
        if (ancien == null)
            throw new BusinessException("Document introuvable.");

        if (nouveauFichier == null || nouveauFichier.getSize() == 0)
            throw new BusinessException("Le nouveau fichier est obligatoire.");

        String chemin = FileUploadUtil.sauvegarderDocument(nouveauFichier, Constantes.UPLOAD_DIR, "documents");
        if (chemin == null)
            throw new BusinessException("Échec de l'enregistrement du fichier.");

        Long origineId = (ancien.getDocumentParentId() != null) ? ancien.getDocumentParentId() : ancien.getId();

        int prochaineVersion = dao.findVersions(origineId).stream()
                .mapToInt(Document::getVersion)
                .max().orElse(ancien.getVersion()) + 1;

        Document nouveau = new Document();
        nouveau.setTitre(ancien.getTitre());
        nouveau.setType(ancien.getType());
        nouveau.setEvenementId(ancien.getEvenementId());
        nouveau.setCheminFichier(chemin);
        nouveau.setVersion(prochaineVersion);
        nouveau.setDocumentParentId(origineId);
        nouveau.setVersionCourante(true);

        dao.save(nouveau);
        dao.setVersionCourante(ancien.getId(), false);
    }

    public List<Document> findVersions(Long origineId) {
        return dao.findVersions(origineId);
    }
    public List<Document> findAll(String filtre) {
        return dao.findAll(filtre);
    }

    public List<Document> findByType(String type, String filtre) {
        if (type == null || type.isBlank()) return dao.findAll(filtre);
        return dao.findByType(type, filtre);
    }

    public List<Document> search(String keyword, String filtre) {
        return dao.search(keyword, filtre);
    }

    public int count(String filtre) {
        return dao.count(filtre);
    }
}
