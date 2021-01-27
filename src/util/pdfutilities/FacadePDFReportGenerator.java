package util.pdfutilities;
import com.lowagie.text.DocumentException;
import j2html.tags.ContainerTag;
import j2html.tags.Tag;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import model.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.h2.store.fs.FileUtils;
import org.xhtmlrenderer.pdf.ITextRenderer;
import javax.swing.*;
import static j2html.TagCreator.*;
import java.io.*;

public class FacadePDFReportGenerator {

    public void creaReport(Report report, Appointment appointment, Owner owner, Pet pet, Doctor doctor)  throws IOException {
        //creazione del report in pdf
        String documentHtml = createHtml("Report Page",
                div().with(
                        h1("Vet Clinic Management Report")
                ),
                div().with(
                        table().attr("style", "border: 1px solid #3DA4E3;").with(
                                tr().with(
                                        td().with(
                                                table().with(
                                                        tr().with(
                                                                td().attr("colspan", "2")
                                                                    .attr("style", "text-align: center; background-color: #add8e6; font-size: 12;")
                                                                        .with(
                                                                                span("Dati Cliente")
                                                                        )
                                                        ),
                                                        tr().with(
                                                                td().attr("style", "background-color: #a7c4c9")
                                                                        .with(
                                                                                span("Cognome")
                                                                        ),
                                                                td().with(
                                                                        span(owner.getSurname())
                                                                )
                                                        ),
                                                        tr().with(
                                                                td().attr("style", "background-color: #a7c4c9")
                                                                        .with(
                                                                                span("Nome")
                                                                        ),
                                                                td().with(
                                                                        span(owner.getName())
                                                                )
                                                        ),
                                                        tr().with(
                                                                td().attr("style", "background-color: #a7c4c9")
                                                                        .with(
                                                                                span("Codice Fiscale")
                                                                        ),
                                                                td().with(
                                                                        span(owner.getFiscalCode())
                                                                )
                                                        ),
                                                        tr().with(
                                                                td().attr("style", "background-color: #a7c4c9")
                                                                        .with(
                                                                                span("Data di Nascita")
                                                                        ),
                                                                td().with(
                                                                        span(owner.getDatebirth().toString())
                                                                )
                                                        )
                                                )
                                        ),
                                        td().with(
                                                table().with(
                                                        tr().with(
                                                                td().attr("colspan", "2")
                                                                        .attr("style", "text-align: center; background-color: #add8e6; font-size: 12;")
                                                                        .with(
                                                                                span("Dati Paziente")
                                                                        )
                                                        ),
                                                        tr().with(
                                                                td().attr("style", "background-color: #a7c4c9")
                                                                        .with(
                                                                                span("Cognome")
                                                                        ),
                                                                td().with(
                                                                        span(pet.getSurname())
                                                                )
                                                        ),
                                                        tr().with(
                                                                td().attr("style", "background-color: #a7c4c9")
                                                                        .with(
                                                                                span("Nome")
                                                                        ),
                                                                td().with(
                                                                        span(pet.getName())
                                                                )
                                                        ),
                                                        tr().with(
                                                                td().attr("style", "background-color: #a7c4c9")
                                                                        .with(
                                                                                span("Sesso")
                                                                        ),
                                                                td().with(
                                                                        span(pet.getSex().toString())
                                                                )
                                                        ),
                                                        tr().with(
                                                                td().attr("style", "background-color: #a7c4c9")
                                                                        .with(
                                                                                span("Data di Nascita")
                                                                        ),
                                                                td().with(
                                                                        span(pet.getDatebirth().toString())
                                                                )
                                                        )
                                                )
                                        ),
                                        td().with(
                                                table().with(
                                                        tr().with(
                                                                td().attr("colspan", "2")
                                                                        .attr("style", "text-align: center; background-color: #add8e6; font-size: 12;")
                                                                        .with(
                                                                                span("Dati Prenotazione")
                                                                        )
                                                        ),
                                                        tr().with(
                                                                td().attr("style", "background-color: #a7c4c9")
                                                                        .with(
                                                                                span("Tipologia di visita")
                                                                        ),
                                                                td().with(
                                                                        span(appointment.getSpecialitation())
                                                                )
                                                        ),
                                                        tr().with(
                                                                td().attr("style", "background-color: #a7c4c9")
                                                                        .with(
                                                                                span("Data")
                                                                        ),
                                                                td().with(
                                                                        span(appointment.getLocalDate().toString())
                                                                )
                                                        )
                                                )
                                        ),
                                        td().with(
                                                table().with(
                                                        tr().with(
                                                                td().attr("colspan", "2")
                                                                        .attr("style", "text-align: center; background-color: #add8e6; font-size: 12;")
                                                                        .with(
                                                                                span("Dati Medico")
                                                                        )
                                                        ),
                                                        tr().with(
                                                                td().attr("style", "background-color: #a7c4c9")
                                                                        .with(
                                                                                span("Cognome")
                                                                        ),
                                                                td().with(
                                                                        span(doctor.getSurname())
                                                                )
                                                        ),
                                                        tr().with(
                                                                td().attr("style", "background-color: #a7c4c9")
                                                                        .with(
                                                                                span("Nome")
                                                                        ),
                                                                td().with(
                                                                        span(doctor.getName())
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                ),
                div().with(
                        h2().with(
                                span("Diagnosi")
                        ),
                        p().with(
                                span(report.getDiagnosis())
                        )
                ),
                div().with(
                        h2().with(
                                span("Terapia")
                        ),
                        p().with(
                                span(report.getTreatments())
                        )
                )
        );

        FileWriter fWriter;
        BufferedWriter writer;
        String inputFile = "./fileName.html";
        fWriter = new FileWriter(inputFile);
        writer = new BufferedWriter(fWriter);
        writer.write(documentHtml);
        writer.newLine(); //this is not actually needed for html files - can make your code more readable though
        writer.close(); //make sure you close the writer object
                                       //data in cui è stata effettuata la visita
        String fileName = appointment.getLocalDate() + "_"+ pet.getName().toUpperCase() + "_"+ pet.getSurname().toUpperCase()  +".pdf";
        String outputFile = this.generateFolders(owner, pet, fileName, report.getPathFile());
        String externalPath = this.choosePDFSaveLocation(fileName);
        generatePDF(inputFile, outputFile);
        generatePDF(inputFile, externalPath);

        if(!report.getPathFile().trim().isEmpty()) {
            this.addAttachments(report, outputFile, report.getPathFile().trim());
            this.addAttachments(report, externalPath, report.getPathFile().trim());
        }

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

    private String generateFolders(Owner owner, Pet pet, String fileName, String attachment) throws IOException {
        String outputFile = "./report/";

        // Creazione cartelle utente e paziente dove mettere il report
        String reportDirectoryName = outputFile.concat(owner.getSurname() + owner.getName() + "_" + owner.getFiscalCode() + "/" + pet.getName());
        File reportDirectory = new File(reportDirectoryName);
        if (!reportDirectory.exists())
            reportDirectory.mkdirs();

        // Creazione cartella in cui inserire tutti gli allegati del paziente
        String attachmentDirectoryName = reportDirectoryName + "/allegati";
        File attachmentDirectory = new File(attachmentDirectoryName);
        if (!attachmentDirectory.exists())
            attachmentDirectory.mkdirs();
        this.copy(new File(attachment), new File(attachmentDirectoryName + "/" + attachment.substring(attachment.lastIndexOf("\\")+1)));

        return reportDirectoryName + "/" + fileName;
    }

    private void copy(File src, File dest) throws IOException {
        try (InputStream is = new FileInputStream(src); OutputStream os = new FileOutputStream(dest)) {
            // buffer size 1K
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(buf)) > 0) {
                os.write(buf, 0, bytesRead);
            }
        }
    }

    private void addAttachments(Report report, String outputFile, String attachmentPath) throws IOException {
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
//            PDImageXObject pdImage = PDImageXObject.createFromFile("D:/Desktop/radiografia.jpg", document);
            contentStream.drawImage(pdImage, 25, 550, 300, 180);
            contentStream.close();
            document.save(outputFile);
            document.close();
        }
    }

    public static String createHtml(String pageTitle, Tag... tags) {
        ContainerTag html = html(
                head
                        (title(pageTitle), style("""
                                h1, h2, h3 {
                                    text-align:center;
                                    color: #3DA4E3;
                                    font-family: "Calibri",serif;
                                }
                                p {
                                 text-align: center;
                                }
                                table {
                                    border-collapse: separate;
                                    border: 1px solid #3DA4E3;
                                    vertical-align: top;
                                    font-size: 10;
                                }
                                tr {
                                    vertical-align: top;
                                    border-collapse: collapse;
                                    border: 1px solid #3DA4E3;
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

    public String choosePDFSaveLocation(String fileName){
        String filepath = null;
        Stage saveStage = new Stage();
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Downloads/"));
//        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
//        chooser.getExtensionFilters().add(extFilter);
        File file = chooser.showDialog(saveStage);
        if (file != null) {
            filepath = file.getAbsolutePath() + "/" + fileName;
            JOptionPane.showMessageDialog(null, "Report generato correttamente " +
                    "in PDF nella cartella " + filepath);
        }
        else
            saveStage.close();
        return filepath;
    }
}
