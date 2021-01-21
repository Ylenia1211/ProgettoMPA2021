package controller;

import dao.ConcreteReportDAO;
import datasource.ConnectionDBH2;
import j2html.TagCreator;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Appointment;
import model.Report;
import util.pdfutilities.FacadePDFReportGenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static j2html.TagCreator.h1;
import static j2html.TagCreator.table;
import static util.pdfutilities.FacadePDFReportGenerator.createHtml;

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
            Report newReport = createNewReport();
            this.reportDAO.update(this.idBooking, newReport);
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

    private Report createNewReport() {
        return new Report.Builder()
                //.setId_pet(this.idPet)
                //.setId_booking(this.idBooking)
                //.setId_owner(this.idOwner)
                .setDiagnosis(this.textDiagnosi.getText())
                .setTreatments(this.textTerapia.getText())
                .setPathFile(this.textPath.getText())
                .build();
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
}
