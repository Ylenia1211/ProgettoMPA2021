package util.pdfutilities;

import com.lowagie.text.DocumentException;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import model.Report;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Classe utilizzata per creare un oggetto 'CreatorReportPDF':{@link CreatorReportPDF}
 * il quale contiene i metodi utili per la creazione di un pdf con allegati.
 */
public class CreatorReportPDF {

    /**
     * Metodo che aggiunge allegati al report creato.
     *
     * @param report         oggetto report {@link Report} a cui vogliamo aggiungere un allegato.
     * @param outputFile     path del report a cui vogliamo aggiungere l'allegato.
     * @param attachmentPath path dell'allegato.
     */
    public void addAttachments(Report report, String outputFile, String attachmentPath) throws IOException {
        if (!report.getPathFile().trim().isEmpty()) {
            File file = new File(outputFile);
            PDDocument document = PDDocument.load(file);
            PDPage attachmentPage = new PDPage();
            document.addPage(attachmentPage);
            PDPageContentStream contentStream = new PDPageContentStream(document, attachmentPage);
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(25, 750);
            contentStream.setLeading(14.5f);
            contentStream.showText("Allegati:");
            contentStream.newLine();
            contentStream.endText();
            PDImageXObject pdImage = PDImageXObject.createFromFile(attachmentPath, document);
            contentStream.drawImage(pdImage, 25, 550, 300, 180);
            contentStream.close();
            document.save(outputFile);
            document.close();
        }
    }

    /**
     * Metodo che genera il pdf del Report.
     *
     * @param inputHtmlPath path del file html generato nella prima fase di costruzione del file per il report.
     * @param outputPdfPath path dove verrà salvato di default il  pdf del report.
     */
    public void generatePDF(String inputHtmlPath, String outputPdfPath) {
        String url;
        try {
            url = new File(inputHtmlPath).toURI().toURL().toString();
            System.out.println("URL: " + url);
            OutputStream out;
            out = new FileOutputStream(outputPdfPath);
            //Flying Saucer part
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocument(url);
            renderer.layout();
            renderer.createPDF(out);
            out.close();

        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo che genera il pdf del Report in un path a scelta dell'utente (inizialmente cartella Download).
     *
     * @param fileName path a scelta dove verrà salvato il  pdf del report.
     */
    public String choosePDFSaveLocation(String fileName) {
        String filepath = null;
        Stage saveStage = new Stage();
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Downloads/"));
        ;
        File file = chooser.showDialog(saveStage);
        if (file != null)
            filepath = file.getAbsolutePath() + "/" + fileName;
        else
            saveStage.close();
        return filepath;
    }

}
