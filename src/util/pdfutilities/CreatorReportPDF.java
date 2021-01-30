package util.pdfutilities;

import com.lowagie.text.DocumentException;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import model.*;
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

public class CreatorReportPDF {

   public void addAttachments(Report report, String outputFile, String attachmentPath) throws IOException {
        if(!report.getPathFile().trim().isEmpty()){
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

    public String choosePDFSaveLocation(String fileName){
        String filepath = null;
        Stage saveStage = new Stage();
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Downloads/"));;
        File file = chooser.showDialog(saveStage);
        if (file != null)
            filepath = file.getAbsolutePath() + "/" + fileName;
        else
            saveStage.close();
        return filepath;
    }

}
