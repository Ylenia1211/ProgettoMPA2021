package util.pdfutilities;
import com.lowagie.text.DocumentException;
import j2html.tags.Tag;
import model.Report;
import org.xhtmlrenderer.pdf.ITextRenderer;
import static j2html.TagCreator.*;
import java.io.*;


public class FacadePDFReportGenerator {
    public void creaReport(Report report) throws IOException {
        //creazione del report in pdf

    }
    public static String createHtml(String pageTitle, Tag... tags) {
           return html(head(title(pageTitle)),
                       body(
                               header(),
                               main(tags), //the view from the partials example),
                               footer()
                       )
                    ).render();
    }
    public static void generatePDF(String inputHtmlPath, String outputPdfPath) {
        String url = null;
        try {
            url = new File(inputHtmlPath).toURI().toURL().toString();
            System.out.println("URL: " + url);
            OutputStream out = null;
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
    /*
    public void creaReport(Report report) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.setFont(PDType1Font.COURIER, 12);
        contentStream.beginText();
        this.createReportText(contentStream, report);
        contentStream.endText();
        contentStream.close();
        document.save(this.choosePDFSaveLocation());
        document.close();
    }

    public void createReportText(PDPageContentStream contentStream, Report report) throws IOException {

        contentStream.newLineAtOffset(25, 750);
        contentStream.setLeading(14.5f);
        contentStream.showText("Diagnosi:");
        contentStream.newLine();
        contentStream.showText(report.getDiagnosis());
        contentStream.newLine();
        contentStream.newLine();
        contentStream.showText("Terapia:");
        contentStream.newLine();
        contentStream.showText(report.getTreatments());
    }

    public String choosePDFSaveLocation(){
        String filepath = null;
        Stage saveStage = new Stage();
        FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
        chooser.getExtensionFilters().add(extFilter);
        File file = chooser.showSaveDialog(saveStage);
        if (file != null) {
            filepath = file.getAbsolutePath();
//            JOptionPane.showMessageDialog(null, "Report generato correttamente " +
//                    "in PDF nella cartella " + filepath);
        }
        else
            saveStage.close();
        return filepath;
    }
    */

}
