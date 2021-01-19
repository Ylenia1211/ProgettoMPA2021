package controller;

import dao.ConcreteAppointmentDAO;
import dao.ConcreteReportDAO;
import datasource.ConnectionDBH2;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
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

public class ReportAddDataController implements Initializable {
    public TextArea textDiagnosi;
    public TextArea textTerapia;
    public TextField textPath;
    public ImageView attachmentImage;
    public ArrayList<String> attachments = new ArrayList<>();
    public Label firstAttachment;
    public VBox allegati;
    public Button creaReportButton;
    public Button btnSaveReport;
    public VBox pane_main_grid;

    private ConcreteReportDAO reportDAO;
    private Appointment appointment;
    private final String idOwner;
    private final String idPet;
    private String idBooking;
    public Button btn;

    public ReportAddDataController(Appointment appointment) {
        this.appointment = appointment;
        this.idOwner = appointment.getId_owner();
        this.idPet = appointment.getId_pet();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.reportDAO = new ConcreteReportDAO(ConnectionDBH2.getInstance());
        this.idBooking = this.reportDAO.searchIdBookingByAppointment(this.appointment);
        this.attachmentImage.setStyle("-fx-background-image: url('/resources/attachment.png')");
        addButtonSave();
        addActionButton();
    }

    public void registrationReport(ActionEvent actionEvent) {
        Report newReport = new Report.Builder()
                .setId_booking(this.idBooking)
                .setId_owner(this.idOwner)
                .setId_pet(this.idPet)
                .setDiagnosis(this.textDiagnosi.getText())
                .setTreatments(this.textTerapia.getText())
                .setPathFile(this.textPath.getText()).build();
        this.reportDAO.add(newReport);  //creazione oggetto Report e salvataggio in DB
        System.out.println(newReport.toString());
    }
    public void addButtonSave() {
        this.btn = new Button("Salva");
        this.btn.setId("btn");
        this.btn.setPrefWidth(200);
        this.btn.setPrefHeight(30);

        this.btn.setStyle("-fx-background-color: #3DA4E3;-fx-text-fill: white;" +
                " -fx-border-color: transparent; -fx-font-size: 16px; ");
        this.pane_main_grid.getChildren().add(this.btn);
    }

    public void addActionButton() {
        this.btn.setOnAction(this::registrationReport);
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