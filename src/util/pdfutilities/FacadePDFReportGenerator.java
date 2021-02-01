package util.pdfutilities;

import model.*;

import javax.swing.*;
import java.io.IOException;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Classe utilizzata per creare un  FacadePDFReportGenerator {@link FacadePDFReportGenerator}: la quale richiama i due sottosistemi HTMLReport {@link HTMLReport} e CreatorReportPdF {@link CreatorReportPDF}
 */
public class FacadePDFReportGenerator {
    public static String pathfile;

    private final HTMLReport htmlReport = new HTMLReport();
    private final CreatorReportPDF creatorReportPDF = new CreatorReportPDF();

    /**
     * Metodo che aggiunge creare il pdf del Report creato.
     *
     * @param report      oggetto report {@link Report} di cui vogliamo creare il PDF.
     * @param appointment oggetto Appointment che contiene dati utili della visita da immagazinare nel PDF del Report
     * @param owner       oggetto Owner che contiene dati utili della visita da immagazinare nel PDF del Report
     * @param pet         oggetto Pet che contiene dati utili della visita da immagazinare nel PDF del Report.
     * @param doctor      oggetto Doctor che contiene dati utili della visita da immagazinare nel PDF del Report
     */
    public void creaReport(Report report, Appointment appointment, Owner owner, Pet pet, Doctor doctor) throws IOException {
        String inputFile = htmlReport.savePageHtml(report, appointment, owner, pet, doctor);

        //data in cui è stata effettuata la visita
        String fileName = appointment.getLocalDate() + "_" + pet.getName().toUpperCase() + "_" +
                pet.getSurname().toUpperCase() + ".pdf";
        String outputFile = "./report/".concat(owner.getSurname() + owner.getName() + "_" + owner.getFiscalCode() +
                "/" + pet.getName()) + "/" + fileName;


        String externalPath = creatorReportPDF.choosePDFSaveLocation(fileName);
        creatorReportPDF.generatePDF(inputFile, outputFile);
        creatorReportPDF.generatePDF(inputFile, externalPath);

        if (!report.getPathFile().trim().isEmpty()) {
            creatorReportPDF.addAttachments(report, outputFile, report.getPathFile());
            creatorReportPDF.addAttachments(report, externalPath, report.getPathFile());
        }
        htmlReport.deletePageHtml(inputFile);

        JOptionPane.showMessageDialog(null, "Pdf del report creato correttamente!\n" +
                " (percorso: " + externalPath + ")");
    }
}
