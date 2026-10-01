package ma.lias.app.util;

import ma.lias.app.model.RapportAnnuelData;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.OutputStream;

public class ExcelGenerator {

    public static void generate(OutputStream out,
            RapportAnnuelData data) throws Exception {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Rapport Annuel");

        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("Année");
        row.createCell(1).setCellValue(data.getYear());

        row = sheet.createRow(1);
        row.createCell(0).setCellValue("Événements");
        row.createCell(1).setCellValue(data.getEventsCurrent());

        row = sheet.createRow(2);
        row.createCell(0).setCellValue("Publications");
        row.createCell(1).setCellValue(data.getPubCurrent());

        workbook.write(out);
        workbook.close();
    }
}