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
                                        span("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.\n" +
                                                "\n" +
                                                "Why do we use it?\n" +
                                                "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).\n" +
                                                "\n" +
                                                "\n" +
                                                "Where does it come from?\n" +
                                                "Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words, consectetur, from a Lorem Ipsum passage, and going through the cites of the word in classical literature, discovered the undoubtable source. Lorem Ipsum comes from sections 1.10.32 and 1.10.33 of \"de Finibus Bonorum et Malorum\" (The Extremes of Good and Evil) by Cicero, written in 45 BC. This book is a treatise on the theory of ethics, very popular during the Renaissance. " +
                                                "The first line of Lorem Ipsum, \"Lorem ipsum dolor sit amet..\", comes from a line in section 1.10.32."
                                             )

                                ),
                                td().with(
                                        span("name")
                                )

                        ),
                        tr().with(
                                td().with(
                                        span("aaaaa")

                                ),
                                td().with(
                                        span("aaaaaa")
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
