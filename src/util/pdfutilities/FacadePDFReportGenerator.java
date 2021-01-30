package util.pdfutilities;

import model.*;

import javax.swing.*;
import java.io.IOException;
//richiama i metodi dei due sottosistemi: htmlReport e pdfReport
public class FacadePDFReportGenerator {
    public static String pathfile;

    private final HTMLReport htmlReport = new HTMLReport();
    private  final  CreatorReportPDF creatorReportPDF = new CreatorReportPDF();

    public void  creaReport(Report report, Appointment appointment, Owner owner, Pet pet, Doctor doctor)  throws IOException {
        String inputFile = htmlReport.savePageHtml(report, appointment, owner, pet, doctor);

        //data in cui è stata effettuata la visita
        String fileName = appointment.getLocalDate() + "_"+ pet.getName().toUpperCase() + "_" +
                pet.getSurname().toUpperCase()  +".pdf";
        String outputFile = "./report/".concat(owner.getSurname() + owner.getName() + "_" + owner.getFiscalCode() +
                "/" + pet.getName()) + "/" + fileName;


        String externalPath = creatorReportPDF.choosePDFSaveLocation(fileName);
        creatorReportPDF.generatePDF(inputFile, outputFile);
        creatorReportPDF.generatePDF(inputFile, externalPath);

        if(!report.getPathFile().trim().isEmpty()) {
            creatorReportPDF.addAttachments(report, outputFile, report.getPathFile());
            creatorReportPDF.addAttachments(report, externalPath, report.getPathFile());
        }
        htmlReport.deletePageHtml(inputFile);

        JOptionPane.showMessageDialog(null, "Pdf del report creato correttamente!\n" +
                " (percorso: "+ externalPath +")");
    }
}
