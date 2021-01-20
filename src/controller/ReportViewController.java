package controller;
import datasource.ConnectionDBH2;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import model.Appointment;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import dao.ConcreteReportDAO;

import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Report;
import util.pdfutilities.FacadePDFReportGenerator;

import java.io.File;
import java.util.ArrayList;

public class ReportViewController extends FacadePDFReportGenerator implements Initializable {
    public Button creaPDFReportButton;
    public Button deleteReportButton;
    private Button savePDFReportButton;
    private Button cancelReportButton;
    public TextArea textDiagnosi;
    public TextArea textTerapia;
    public TextField textPath;
    public ImageView attachmentImage;
    public ArrayList<String> attachments = new ArrayList<>();
    public Label firstAttachment;
    public VBox allegati;
    public VBox pane_main_grid;
    public HBox lastHbox;
    public HBox buttons = new HBox();
    private final Appointment appointment;
    private final String idOwner;
    private final String idPet;
    private String idBooking;
    private CheckBox enableModify;
    private ConcreteReportDAO reportDAO;
    private Report report;


    public ReportViewController(Appointment appointment) {
        this.appointment = appointment;
        this.idOwner = appointment.getId_owner();
        this.idPet = appointment.getId_pet();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.reportDAO = new ConcreteReportDAO(ConnectionDBH2.getInstance());
        this.idBooking = reportDAO.searchIdBookingByAppointment(this.appointment);
        this.attachmentImage.setStyle("-fx-background-image: url('/resources/attachment.png')");
        //devono contenere i risultati ricavati dal db
        //ricerco i dati del report tramite l'id della prenotazione
        this.report = reportDAO.searchByIdBooking(this.idBooking);
        this.textDiagnosi.setText(report.getDiagnosis());
        this.textTerapia.setText(report.getTreatments());
        this.textPath.setText(report.getPathFile());
        //inizialmente non sono edititabili
        this.textDiagnosi.setEditable(false);
        this.textTerapia.setEditable(false);
        this.textPath.setEditable(false);
        this.attachmentImage.setDisable(true);
        if (!this.textPath.getText().trim().isEmpty()) {
            this.firstAttachment.setText(this.report.getPathFile());
        }
        /*
        #Todo: aggiungere bottone o checkbox per la modifica (setEditable(true)) e dunque moodificare i campi Dignosi,terapia, textPath
        #Todo: quando si abilita la modifica il campo crea pdf e cancella devono sparire e deve apparire il bottone di salvataggio della modifica, quando si disabilita ricompaiono
        #Todo: applicare le modifiche della dignosi, terapia e path nel Db ( usare dao del report)
        #Todo: aggiungere bottone cancellazione e cancellare il report dal db
        #todo: creazione del file pdf
        */
        this.setButtons();
        this.addEnableModifyCheckBox();
        this.buttons.setAlignment(Pos.CENTER);
        this.buttons.setSpacing(20);
        this.pane_main_grid.getChildren().add(buttons);
        this.addCreateAndDeleteButtonsPDFReport();
    }

    private void setButtons(){
        // Creo il createButton
        this.creaPDFReportButton = new Button("Crea PDF");
        this.creaPDFReportButton.setId("createPDF");
        this.creaPDFReportButton.setPrefWidth(200);
        this.creaPDFReportButton.setPrefHeight(30);
        this.creaPDFReportButton.setStyle("-fx-background-color: #3DA4E3;-fx-text-fill: white;" +
                " -fx-border-color: transparent; -fx-font-size: 16px; ");
        this.creaPDFReportButton.setOnAction(actionEvent -> {
            try {
                creaReport(this.report);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Creo il deleteButton
        this.deleteReportButton = new Button("Elimina report");
        this.deleteReportButton.setId("deleteReport");
        this.deleteReportButton.setPrefWidth(200);
        this.deleteReportButton.setPrefHeight(30);
        this.deleteReportButton.setStyle("-fx-background-color: red;-fx-text-fill: white;" +
                " -fx-border-color: transparent; -fx-font-size: 16px; ");
        this.deleteReportButton.setOnAction(actionEvent -> {
            this.reportDAO.delete(this.idBooking);
        });

        // Creo il saveButton
        this.savePDFReportButton = new Button("Salva");
        this.savePDFReportButton.setId("btn");
        this.savePDFReportButton.setPrefWidth(200);
        this.savePDFReportButton.setPrefHeight(30);
        this.savePDFReportButton.setStyle("-fx-background-color: #3DA4E3;-fx-text-fill: white;" +
                " -fx-border-color: transparent; -fx-font-size: 16px; ");
        this.savePDFReportButton.setOnAction(actionEvent -> {
            this.report.setDiagnosis(this.textDiagnosi.getText());
            this.report.setTreatments(this.textTerapia.getText());
            this.report.setPathFile(this.textPath.getText());
            this.reportDAO.update(this.idBooking, this.report);
        });

        // Creo il cancelButton
        this.cancelReportButton = new Button("Annulla modifiche");
        this.cancelReportButton.setId("btn");
        this.cancelReportButton.setPrefWidth(200);
        this.cancelReportButton.setPrefHeight(30);
        this.cancelReportButton.setStyle("-fx-background-color: red;-fx-text-fill: white;" +
                " -fx-border-color: transparent; -fx-font-size: 16px; ");
        this.cancelReportButton.setOnAction(actionEvent -> {
            this.textDiagnosi.setText(this.report.getDiagnosis());
            this.textTerapia.setText(this.report.getTreatments());
            this.textPath.setText(this.report.getPathFile());
            this.enableModify.setSelected(false);
            this.buttons.getChildren().clear();
            this.addCreateAndDeleteButtonsPDFReport();
        });
    }

    private void addCreateAndDeleteButtonsPDFReport() {
        this.buttons.getChildren().add(0, this.creaPDFReportButton);
        this.buttons.getChildren().add(1, this.deleteReportButton);
    }

    private void addSaveAndCancelButtonsPDFReport() {
        this.buttons.getChildren().add(0, this.savePDFReportButton);
        this.buttons.getChildren().add(1, this.cancelReportButton);
    }

    private void addEnableModifyCheckBox(){
        this.enableModify = new CheckBox("Abilita modifiche");
        this.enableModify.setId("enableModify");
        this.enableModify.setOnAction(actionEvent -> {
            if (!enableModify.isSelected()) {
                this.buttons.getChildren().clear();
                this.addCreateAndDeleteButtonsPDFReport();
                textDiagnosi.setEditable(false);
                textTerapia.setEditable(false);
                textPath.setEditable(false);
                attachmentImage.setDisable(true);
            }else {
                this.buttons.getChildren().clear();
                this.addSaveAndCancelButtonsPDFReport();
                textDiagnosi.setEditable(true);
                textTerapia.setEditable(true);
                textPath.setEditable(true);
                attachmentImage.setDisable(false);
            }
        });
        this.lastHbox.getChildren().add(0, enableModify);
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

//    public void creaReport() throws IOException {
//        //int ID = ++DoctorDashboard.reportID;
//        //Creating PDF document object
//        PDDocument document = new PDDocument();
//        PDPage my_page = new PDPage();
//        PDPageContentStream contentStream = new PDPageContentStream(document, my_page);
//
//
////        String labelDiagnosi = "Diagnosi:";
//        String reportText = "Diagnosi:\n" + this.textDiagnosi.getText() + "\nTerapia:\n";
////        String labelTerapia = "Terapia:";
////        String textTerapia = "Terapia:\n" + this.textTerapia.getText();
//
//
////        //Begin the Content stream
////        contentStream.beginText();
//        //Setting the font to the Content stream
////        contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
////        contentStream.setLeading(1.5f);
////        //Setting the position for the line
////        contentStream.newLineAtOffset(25, 700);
////
////        String text = "Paziente:";// +
//////                      "Dati cane\n\n" +
////
////
////        //Adding text in the form of string
////        contentStream.showText(labelDiagnosi);
////        contentStream.newLine();
////        contentStream.showText(textDiagnosi);
////        contentStream.newLine();
////        contentStream.newLine();
////        contentStream.showText(labelTerapia);
////        contentStream.newLine();
////        contentStream.showText(textTerapia);
////        contentStream.newLine();
////
//////        contentStream.newLine();
//        //Ending the content stream
////        contentStream.endText();
//////
////        System.out.println("Content added");
////
//        //Closing the content stream
//        contentStream.close();
//        document.addPage(my_page);
//
////        if (this.attachments.size() > 0){
////            PDPage attachment = new PDPage();
////            document.addPage(attachment);
////            PDPage attachmentPage = document.getPage(1);
////            for (String path: this.attachments) {
////                PDImageXObject pdImage = PDImageXObject.createFromFile(path, document);
////                contentStream = new PDPageContentStream(document, attachmentPage);
////                contentStream.drawImage(pdImage, 70, 250);
////                contentStream.close();
////            }
////
////        }
//        //Saving the document
////        String currentPath = System.getProperty("user.dir");
////        document.save(currentPath + "/report/report_test.pdf");
//        String filepath = null;
//        Stage saveStage = new Stage();
//        FileChooser chooser = new FileChooser();
//        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
//        chooser.getExtensionFilters().add(extFilter);
//        File file = chooser.showSaveDialog(saveStage);
//        if (file != null) {
//            System.out.println("PDF created");
//            filepath = file.getAbsolutePath();
////            document.close();
//        }
//        else
//            saveStage.close();
//
//        MultiLineParagraph.createParagraph(reportText, filepath);
////        MultiLineParagraph.createParagraph(textTerapia, filepath);
////
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
//        }
//    }
//
//
//    public static class MultiLineParagraph {
//
//        private static final PDFont FONT = PDType1Font.TIMES_ROMAN;
//        private static final float FONT_SIZE = 13;
//        private static final float LEADING = -1.5f * FONT_SIZE;
//
//        public static void createParagraph(String text, String path)  {
//
//            try (PDDocument doc = PDDocument.load(new File(path))){
//                PDPage page = doc.getPage(0);
//                //doc.addPage(page);
//                PDPageContentStream contentStream = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true);
//
//                PDRectangle mediaBox = page.getMediaBox();
//                float marginY = 80;
//                float marginX = 60;
//                float width = mediaBox.getWidth() - 2 * marginX;
//                float startX = mediaBox.getLowerLeftX() + marginX;
//                float startY = mediaBox.getUpperRightY() - marginY;
//
////                String text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt" +
////                        " ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco" +
////                        " laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in " +
////                        " ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco" +
////                        " laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in " +
////                        "voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat" +
////                        " non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
//
//                contentStream.beginText();
//                addParagraph(contentStream, width, startX, startY, text, true);
////                addParagraph(contentStream, width, 0, -FONT_SIZE, text);
////                addParagraph(contentStream, width, 0, -FONT_SIZE, text, false);
//                contentStream.endText();
//
//                contentStream.close();
//                if (doc.getNumberOfPages() > 1)
//                    doc.removePage(1);
//                doc.save(path);
//            } catch (IOException e){
//                System.err.println("Exception while trying to create pdf document - " + e);
//            }
//        }
//
//        private static void addParagraph(PDPageContentStream contentStream, float width, float sx,
//                                         float sy, String text) throws IOException {
//            addParagraph(contentStream, width, sx, sy, text, false);
//        }
//
//        private static void addParagraph(PDPageContentStream contentStream, float width, float sx,
//                                         float sy, String text, boolean justify) throws IOException {
//            List<String> lines = parseLines(text, width);
//            contentStream.setFont(FONT, FONT_SIZE);
//            contentStream.newLineAtOffset(sx, sy);
//            for (String line: lines) {
//                float charSpacing = 0;
//                if (justify){
//                    if (line.length() > 1) {
//                        float size = FONT_SIZE * FONT.getStringWidth(line.replace("\n", " ").replace("\r", " ")) / 1000;
//                        float free = width - size;
//                        if (free > 0 && !lines.get(lines.size() - 1).equals(line)) {
//                            charSpacing = free / (line.length() - 1);
//                        }
//                    }
//                }
//                contentStream.setCharacterSpacing(charSpacing);
//                if (line.contains("\n"))
//                    contentStream.newLine();
//                contentStream.showText(line.replace("\n", " ").replace("\r", " "));
//                contentStream.newLineAtOffset(0, LEADING);
//            }
//        }
//
//        private static List<String> parseLines(String text, float width) throws IOException {
//            List<String> lines = new ArrayList<String>();
//            int lastSpace = -1;
//            while (text.length() > 0) {
//                int spaceIndex = text.indexOf(' ', lastSpace + 1);
//                if (spaceIndex < 0)
//                    spaceIndex = text.length();
//                String subString = text.substring(0, spaceIndex);
//                float size = FONT_SIZE * FONT.getStringWidth(subString.replace("\n", " ").replace("\r", " ")) / 1000;
//                if (size > width) {
//                    if (lastSpace < 0){
//                        lastSpace = spaceIndex;
//                    }
//                    subString = text.substring(0, lastSpace);
//                    lines.add(subString);
//                    text = text.substring(lastSpace).trim();
//                    lastSpace = -1;
//                } else if (spaceIndex == text.length()) {
//                    lines.add(text);
//                    text = "";
//                } else {
//                    lastSpace = spaceIndex;
//                }
//            }
//            return lines;
//        }
//    }
}
