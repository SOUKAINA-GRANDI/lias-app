package ma.lias.app.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import ma.lias.app.model.Evenement;

import java.io.OutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

public class PDFGenerator {

    private static final Logger logger = Logger.getLogger(PDFGenerator.class.getName());

    public static void generateRapport(OutputStream out,
            int year,
            int prevYear,
            int pubCurrent,
            int pubPrev,
            int evtCurrent,
            int evtPrev,
            int convCurrent,
            int convPrev,
            int membresActifs,
            List<Evenement> evenements){

        Document document = new Document(PageSize.A4, 50, 50, 50, 50);

        try {

            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD);
            Paragraph title = new Paragraph("RAPPORT ANNUEL DU LABORATOIRE LIAS", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Année : " + year));
            document.add(new Paragraph("Date génération : " + LocalDate.now()));
            document.add(new Paragraph(" "));

            Font bold = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);

            document.add(new Paragraph("Résumé exécutif", bold));
            document.add(new Paragraph("Ce rapport présente les activités scientifiques et administratives du laboratoire."));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);

            table.addCell("Indicateur");
            table.addCell(String.valueOf(year));
            table.addCell(String.valueOf(prevYear));

            table.addCell("Publications");
            table.addCell(String.valueOf(pubCurrent));
            table.addCell(String.valueOf(pubPrev));

            table.addCell("Événements");
            table.addCell(String.valueOf(evtCurrent));
            table.addCell(String.valueOf(evtPrev));

            table.addCell("Conventions");
            table.addCell(String.valueOf(convCurrent));
            table.addCell(String.valueOf(convPrev));

            document.add(table);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Nombre membres actifs : " + membresActifs));

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Liste des événements :", bold));

            if (evenements != null && !evenements.isEmpty()) {
                for (Evenement e : evenements) {
                    document.add(new Paragraph("- "
                            + e.getTitre()
                            + " | "
                            + e.getLieu()
                            + " | "
                            + e.getDateDebut()));
                }
            } else {
                document.add(new Paragraph("Aucun événement pour cette année."));
            }

        } catch (Exception e) {
            logger.severe("Erreur génération PDF : " + e.getMessage());
        } finally {
            document.close();
        }
    }
}