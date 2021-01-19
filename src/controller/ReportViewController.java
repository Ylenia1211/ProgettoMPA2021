package controller;
import datasource.ConnectionDBH2;
import javafx.fxml.Initializable;
import model.Appointment;

import java.net.URL;
import java.util.ResourceBundle;
import dao.ConcreteReportDAO;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Appointment;
import model.Owner;
import model.Pet;
import model.Report;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ReportViewController implements Initializable {
    public TextArea textDiagnosi;
    public TextArea textTerapia;
    public TextField textPath;
    public ImageView attachmentImage;
    public ArrayList<String> attachments = new ArrayList<>();
    public Label firstAttachment;
    public VBox allegati;
    public Button creaPDFReportButton;
    public VBox pane_main_grid;
    private ConcreteReportDAO reportDAO;
    private Appointment appointment;
    private final String idOwner;
    private final String idPet;
    private String idBooking;


    public ReportViewController(Appointment appointment) {
        this.appointment = appointment;
        this.idOwner = appointment.getId_owner();
        this.idPet = appointment.getId_pet();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.reportDAO = new ConcreteReportDAO(ConnectionDBH2.getInstance());
        this.idBooking = this.reportDAO.searchIdBookingByAppointment(this.appointment);
        this.attachmentImage.setStyle("-fx-background-image: url('/resources/attachment.png')");
        //devono contenere i risultati ricavati dal db
        //ricerco i dati del report tramite l'id della prenotazione
        Report report = this.reportDAO.searchByIdBooking(this.idBooking);
        this.textDiagnosi.setText(report.getDiagnosis());
        this.textTerapia.setText(report.getTreatments());
        this.textPath.setText(report.getPathFile());
        //inizialmente non sono edititabili
        this.textDiagnosi.setEditable(false);
        this.textTerapia.setEditable(false);
        this.textPath.setEditable(false);


        //#Todo:aggiungere bottone o checkbox per la modifica (setEditable(true)) e dunque moodificare i campi Dignosi,terapia, textPath
        //#Todo: quando si abilita la modifica il campo crea pdf e cancella devono sparire e deve apparire il bottone di salvataggio della modifica, quando si disabilita ricompaiono
        //#Todo: applicare le modifiche della dignosi, terapia e path nel Db ( usare dao del report)
        //#Todo: aggiungere bottone cancellazione e cancellare il report dal db
        //#todo: creazione del file pdf

        addButtonCreatePDFReport();
        this.creaPDFReportButton.setOnAction(actionEvent -> {
            try {
                creaReport();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void addButtonCreatePDFReport() {
        this.creaPDFReportButton = new Button("Crea PDF");
        this.creaPDFReportButton.setId("btn");
        this.creaPDFReportButton.setPrefWidth(200);
        this.creaPDFReportButton.setPrefHeight(30);
        this.creaPDFReportButton.setStyle("-fx-background-color: #3DA4E3;-fx-text-fill: white;" +
                " -fx-border-color: transparent; -fx-font-size: 16px; ");
        this.pane_main_grid.getChildren().add(this.creaPDFReportButton);
    }


    public void findAttachment() {
        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            String filePath = file.getAbsolutePath();
            if (this.firstAttachment.getText().equals("Nessuno"))
                this.firstAttachment.setText(filePath);
            else
                this.allegati.getChildren().add(new Label(filePath));
            this.attachments.add(filePath);
            this.textPath.setText(filePath);
        }
    }

    public void creaReport() throws IOException {
        //int ID = ++DoctorDashboard.reportID;
        //Creating PDF document object
        PDDocument document = new PDDocument();
        PDPage my_page = new PDPage();
        PDPageContentStream contentStream = new PDPageContentStream(document, my_page);


//        String labelDiagnosi = "Diagnosi:";
        String reportText = "Diagnosi:\n" + this.textDiagnosi.getText() + "\nTerapia:\n";
//        String labelTerapia = "Terapia:";
//        String textTerapia = "Terapia:\n" + this.textTerapia.getText();


//        //Begin the Content stream
//        contentStream.beginText();
        //Setting the font to the Content stream
//        contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
//        contentStream.setLeading(1.5f);
//        //Setting the position for the line
//        contentStream.newLineAtOffset(25, 700);
//
//        String text = "Paziente:";// +
////                      "Dati cane\n\n" +
//
//
//        //Adding text in the form of string
//        contentStream.showText(labelDiagnosi);
//        contentStream.newLine();
//        contentStream.showText(textDiagnosi);
//        contentStream.newLine();
//        contentStream.newLine();
//        contentStream.showText(labelTerapia);
//        contentStream.newLine();
//        contentStream.showText(textTerapia);
//        contentStream.newLine();
//
////        contentStream.newLine();
        //Ending the content stream
//        contentStream.endText();
////
//        System.out.println("Content added");
//
        //Closing the content stream
        contentStream.close();
        document.addPage(my_page);

//        if (this.attachments.size() > 0){
//            PDPage attachment = new PDPage();
//            document.addPage(attachment);
//            PDPage attachmentPage = document.getPage(1);
//            for (String path: this.attachments) {
//                PDImageXObject pdImage = PDImageXObject.createFromFile(path, document);
//                contentStream = new PDPageContentStream(document, attachmentPage);
//                contentStream.drawImage(pdImage, 70, 250);
//                contentStream.close();
//            }
//
//        }
        //Saving the document
//        String currentPath = System.getProperty("user.dir");
//        document.save(currentPath + "/report/report_test.pdf");
        String filepath = null;
        Stage saveStage = new Stage();
        FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
        chooser.getExtensionFilters().add(extFilter);
        File file = chooser.showSaveDialog(saveStage);
        if (file != null) {
            System.out.println("PDF created");
            filepath = file.getAbsolutePath();
//            document.close();
        }
        else
            saveStage.close();

        MultiLineParagraph.createParagraph(reportText, filepath);
//        MultiLineParagraph.createParagraph(textTerapia, filepath);
//
        if (this.attachments.size() > 0){
            PDPage attachment = new PDPage();
            document.addPage(attachment);
            PDPage attachmentPage = document.getPage(1);
            for (String path: this.attachments) {
                PDImageXObject pdImage = PDImageXObject.createFromFile(path, document);
                contentStream = new PDPageContentStream(document, attachmentPage);
                contentStream.drawImage(pdImage, 70, 250);
                contentStream.close();
            }
        }
    }


    public static class MultiLineParagraph {

        private static final PDFont FONT = PDType1Font.TIMES_ROMAN;
        private static final float FONT_SIZE = 13;
        private static final float LEADING = -1.5f * FONT_SIZE;

        public static void createParagraph(String text, String path)  {

            try (PDDocument doc = PDDocument.load(new File(path))){
                PDPage page = doc.getPage(0);
                //doc.addPage(page);
                PDPageContentStream contentStream = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true);

                PDRectangle mediaBox = page.getMediaBox();
                float marginY = 80;
                float marginX = 60;
                float width = mediaBox.getWidth() - 2 * marginX;
                float startX = mediaBox.getLowerLeftX() + marginX;
                float startY = mediaBox.getUpperRightY() - marginY;

//                String text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt" +
//                        " ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco" +
//                        " laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in " +
//                        " ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco" +
//                        " laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in " +
//                        "voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat" +
//                        " non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";

                contentStream.beginText();
                addParagraph(contentStream, width, startX, startY, text, true);
//                addParagraph(contentStream, width, 0, -FONT_SIZE, text);
//                addParagraph(contentStream, width, 0, -FONT_SIZE, text, false);
                contentStream.endText();

                contentStream.close();
                if (doc.getNumberOfPages() > 1)
                    doc.removePage(1);
                doc.save(path);
            } catch (IOException e){
                System.err.println("Exception while trying to create pdf document - " + e);
            }
        }

        private static void addParagraph(PDPageContentStream contentStream, float width, float sx,
                                         float sy, String text) throws IOException {
            addParagraph(contentStream, width, sx, sy, text, false);
        }

        private static void addParagraph(PDPageContentStream contentStream, float width, float sx,
                                         float sy, String text, boolean justify) throws IOException {
            List<String> lines = parseLines(text, width);
            contentStream.setFont(FONT, FONT_SIZE);
            contentStream.newLineAtOffset(sx, sy);
            for (String line: lines) {
                float charSpacing = 0;
                if (justify){
                    if (line.length() > 1) {
                        float size = FONT_SIZE * FONT.getStringWidth(line.replace("\n", " ").replace("\r", " ")) / 1000;
                        float free = width - size;
                        if (free > 0 && !lines.get(lines.size() - 1).equals(line)) {
                            charSpacing = free / (line.length() - 1);
                        }
                    }
                }
                contentStream.setCharacterSpacing(charSpacing);
                if (line.contains("\n"))
                    contentStream.newLine();
                contentStream.showText(line.replace("\n", " ").replace("\r", " "));
                contentStream.newLineAtOffset(0, LEADING);
            }
        }

        private static List<String> parseLines(String text, float width) throws IOException {
            List<String> lines = new ArrayList<String>();
            int lastSpace = -1;
            while (text.length() > 0) {
                int spaceIndex = text.indexOf(' ', lastSpace + 1);
                if (spaceIndex < 0)
                    spaceIndex = text.length();
                String subString = text.substring(0, spaceIndex);
                float size = FONT_SIZE * FONT.getStringWidth(subString.replace("\n", " ").replace("\r", " ")) / 1000;
                if (size > width) {
                    if (lastSpace < 0){
                        lastSpace = spaceIndex;
                    }
                    subString = text.substring(0, lastSpace);
                    lines.add(subString);
                    text = text.substring(lastSpace).trim();
                    lastSpace = -1;
                } else if (spaceIndex == text.length()) {
                    lines.add(text);
                    text = "";
                } else {
                    lastSpace = spaceIndex;
                }
            }
            return lines;
        }
    }

}
