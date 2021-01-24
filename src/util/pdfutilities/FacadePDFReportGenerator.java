package util.pdfutilities;
import com.lowagie.text.DocumentException;
import j2html.tags.ContainerTag;
import j2html.tags.Tag;
import model.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.xhtmlrenderer.pdf.ITextRenderer;
import javax.swing.*;
import static j2html.TagCreator.*;
import java.io.*;

public class FacadePDFReportGenerator {

    public void creaReport(Report report, Appointment appointment, Owner owner, Pet pet, Doctor doctor)  throws IOException {
        //creazione del report in pdf
        String documentHtml = createHtml("Report Page",
                h1("Report"),
                table().with(
                        tr().with(
                                td().with(
                                        span("Cliente:")
                                ),
                                td().with(
                                        span(owner.getName())
                                ),
                                td().with(
                                        span(owner.getSurname())
                                ),
                                td().with(
                                        span(owner.getSex().toString())
                                ),
                                td().with(
                                        span(owner.getDatebirth().toString())
                                )

                        ),

                        tr().with(
                                td().with(
                                        span("Paziente:")
                                ),
                                td().with(
                                        span(pet.getName())
                                ),
                                td().with(
                                        span(pet.getSurname())
                                ),
                                td().with(
                                        span(pet.getSex().toString())
                                ),
                                td().with(
                                        span(pet.getDatebirth().toString())
                                )
                        ),
                        tr().with(
                                td().with(
                                        span("Dottore che ha effettuato la visita:")
                                ),
                                td().with(
                                        span(doctor.getName())
                                ),
                                td().with(
                                        span(doctor.getSurname())
                                ),
                                td().with(
                                        span(doctor.getSex().toString())
                                ),
                                td().with(
                                        span(doctor.getDatebirth().toString())
                                )
                        ),
                        tr().with(
                                td().with(
                                        span("Tipo di Visita:")

                                ),
                                td().with(
                                        span(doctor.getSpecialization())
                                )
                        ),
                        tr().with(
                                td().with(
                                        span("Diagnosi:")

                                ),
                                td().with(
                                        span(report.getDiagnosis())
                                )
                        ),
                        tr().with(
                                td().with(
                                        span("Cure: ")

                                ),
                                td().with(
                                        span(report.getTreatments())
                                )
                        )
        ));

        FileWriter fWriter;
        BufferedWriter writer;
        String inputFile = "./fileName.html";
        fWriter = new FileWriter("./fileName.html");
        writer = new BufferedWriter(fWriter);
        writer.write(documentHtml);
        writer.newLine(); //this is not actually needed for html files - can make your code more readable though
        writer.close(); //make sure you close the writer object
                                       //data in cui è stata effettuata la visita
        String outputFile = "./report/"+ appointment.getLocalDate() + "_"+ pet.getName().toUpperCase() + "_"+ pet.getSurname().toUpperCase()  +".pdf";
        generatePDF(inputFile, outputFile);
        if(!report.getPathFile().trim().isEmpty())
            this.checkForAttachments(report, outputFile, report.getPathFile().trim());

        //per cancellare il file html di supporto creato visto che non ci serve
        File file = new File(inputFile);
        if(file.delete())
        {
            System.out.println("File html cancellato");
        }
        else
        {
            System.out.println("File NON cancellato");
        }

        JOptionPane.showMessageDialog(null, "Pdf del report creato correttamente!\n (percorso: "+ outputFile +")");

    }

    private void checkForAttachments(Report report, String outputFile, String attachmentPath) throws IOException {
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
//            PDImageXObject pdImage = PDImageXObject.createFromFile(attachmentPath, document);
            PDImageXObject pdImage = PDImageXObject.createFromFile("D:/Desktop/radiografia.jpg", document);
            contentStream.drawImage(pdImage, 70, 250);
            contentStream.close();
            document.save(outputFile);
            document.close();
        }
    }

    public static String createHtml(String pageTitle, Tag... tags) {
           ContainerTag html = html(
                   head
                           (title(pageTitle), style("""
                                   h1 {
                                       color: navy;
                                       margin-left: 20px;
                                   }\s
                                   table, th, td {
                                       border: 1px solid black;
                                   }

                                   table {
                                       width: 100%;
                                   }""")
                           ),
                                   //link().withRel("stylesheet").withHref(pathCss).withType("text/css")),
                       body(
                               header(),
                               main(tags), //contiene tutti i tag dell'html
                               footer()
                       )
                    ).attr("lang", "it");
           return document().render() + html.render();
    }
    public static void generatePDF(String inputHtmlPath, String outputPdfPath) {
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
