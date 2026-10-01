package ma.lias.app.util;
import java.util.Locale;
import java.util.Set;

import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class FileUploadUtil {

    private static final String UPLOAD_DIR = "uploads";
    private static final Set<String> EXT_DOCUMENTS =
            Set.of(".pdf", ".doc", ".docx", ".xls", ".xlsx", ".jpg", ".jpeg", ".png");
    private static final Set<String> EXT_PHOTOS =
            Set.of(".jpg", ".jpeg", ".png", ".webp");
    private static final long TAILLE_MAX_DOCUMENT = 10L * 1024 * 1024; // 10 Mo

    /** Retourne l'extension (minuscule) si elle est autorisée, sinon null. */
    private static String extensionAutorisee(Part part, Set<String> autorisees) {
        String nom = part.getSubmittedFileName();
        if (nom == null) return null;
        int i = nom.lastIndexOf('.');
        if (i < 0 || i == nom.length() - 1) return null;
        String ext = nom.substring(i).toLowerCase(Locale.ROOT);
        return autorisees.contains(ext) ? ext : null;
    }
    

    public static String saveFile(Part part, String applicationPath) throws IOException {

        String fileName = extractFileName(part);
        String uploadPath = applicationPath + File.separator + UPLOAD_DIR;

        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String filePath = uploadPath + File.separator + fileName;
        part.write(filePath);

        return UPLOAD_DIR + "/" + fileName;
    }

    private static String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        for (String token : contentDisp.split(";")) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf("=") + 2, token.length() - 1);
            }
        }
        return "";
    }
    public static boolean isValidPDF(Part filePart)
            throws IOException {

        try (InputStream is = filePart.getInputStream()) {

            byte[] header = new byte[5];

            int bytesRead = is.read(header);

            if (bytesRead < 5)
                return false;

            String signature = new String(header);

            return signature.startsWith("%PDF-");
        }
    }

    public static String sauvegarderPhoto(Part part, String baseDir) {
        try {
            String extension = extensionAutorisee(part, EXT_PHOTOS);
            if (extension == null || part.getSize() > ma.lias.app.config.Constantes.MAX_FILE_SIZE) {
                return null; // extension refusée ou fichier trop lourd
            }

            File dossierPhotos = new File(baseDir, "photos");
            if (!dossierPhotos.exists()) {
                dossierPhotos.mkdirs();
            }

            String nomUnique = UUID.randomUUID().toString() + extension;
            part.write(new File(dossierPhotos, nomUnique).getAbsolutePath());
            return nomUnique;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String sauvegarderDocument(Part part, String baseDir, String sousDossier) {
        try {
            String extension = extensionAutorisee(part, EXT_DOCUMENTS);
            if (extension == null || part.getSize() > TAILLE_MAX_DOCUMENT) {
                return null;
            }

            File dossier = new File(baseDir, sousDossier);
            if (!dossier.exists()) {
                dossier.mkdirs();
            }

            String nomUnique = UUID.randomUUID().toString() + extension;
            part.write(new File(dossier, nomUnique).getAbsolutePath());
            return "uploads/" + sousDossier + "/" + nomUnique;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
