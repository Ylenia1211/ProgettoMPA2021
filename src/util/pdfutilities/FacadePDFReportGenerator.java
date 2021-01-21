package util.pdfutilities;
import com.lowagie.text.DocumentException;
import j2html.tags.ContainerTag;
import j2html.tags.Tag;
import model.Report;
import org.xhtmlrenderer.pdf.ITextRenderer;
import static j2html.TagCreator.*;
import java.io.*;


public class FacadePDFReportGenerator {
    public void creaReport(Report report) throws IOException {
        //creazione del report in pdf
        String documentHtml = createHtml("Report Page",
                h1("Report"),
                table().with(
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

        FileWriter fWriter = null;
        BufferedWriter writer = null;
        String inputFile = "./fileName.html";
        fWriter = new FileWriter("./fileName.html");
        writer = new BufferedWriter(fWriter);
        writer.write(documentHtml);
        writer.newLine(); //this is not actually needed for html files - can make your code more readable though
        writer.close(); //make sure you close the writer object

        String outputFile = "./filepdf.pdf";
        generatePDF(inputFile, outputFile);

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

        System.out.println("OK!");

    }
    public static String createHtml(String pageTitle, Tag... tags) {
           ContainerTag html = html(
                   head
                           (title(pageTitle), style("h1 {\n" +
                                   "    color: navy;\n" +
                                   "    margin-left: 20px;\n" +
                                   "} \n" +
                                   "table, th, td {\n" +
                                   "    border: 1px solid black;\n" +
                                   "}\n" +
                                   "\n" +
                                   "table {\n" +
                                   "    width: 100%;\n" +
                                   "}" )
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
