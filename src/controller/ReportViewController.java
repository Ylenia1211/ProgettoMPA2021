package controller;

import dao.ConcreteDoctorDAO;
import dao.ConcreteReportDAO;
import datasource.ConnectionDBH2;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.*;
import util.SessionUser;
import util.pdfutilities.FacadePDFReportGenerator;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Implementando i metodi di 'Inizializable' {@link Initializable} inizializza la view associata al controller.
 * Controlla la parte inferiore della view del report, comprendente i campi {@link ReportViewController#textDiagnosi},
 * {@link ReportViewController#textTerapia}, {@link ReportViewController#lastHbox} contenente i campi per la
 * visualizzazione del percorso degli allegati e un bottone per l'inserimento degli allegati, i 4 bottoni
 * {@link ReportViewController#savePDFReportButton}, {@link ReportViewController#cancelReportButton},
 * {@link ReportViewController#deleteReportButton} e {@link ReportViewController#creaPDFReportButton}, e infine la
 * {@link CheckBox} {@link ReportViewController#enableModify} per abilitare la modifica
 */
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
    public ImageView deleteFirstAttachmentButton;
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
    private final Owner owner;
    private final Pet pet;
    private final Doctor doctor;
    private final ConcreteDoctorDAO doctorRepo;

    /**
     * Costruttore della classe, setta gli attributi necessari per la generazione di un report della visita. Setta l'attributo {@link ReportViewController#doctorRepo} con una nuova istanza
     * di {@link ConcreteDoctorDAO} richiamando la Connessione singleton {@link ConnectionDBH2} del database.
     *
     * @param appointment Lo specifico appuntamento
     * @param owner       Il proprietario
     * @param pet         Il paziente
     * @param doctor      Il medico che ha effettuato la visita
     */
    public ReportViewController(Appointment appointment, Owner owner, Pet pet, Doctor doctor) {
        this.appointment = appointment;
        this.idOwner = appointment.getId_owner();
        this.idPet = appointment.getId_pet();
        this.owner = owner;
        this.pet = pet;
        this.doctor = doctor;
        this.doctorRepo = new ConcreteDoctorDAO(ConnectionDBH2.getInstance());
    }

    /**
     * Inizializza i campi della view in modo appropriato.
     * Setta i campi della view recuperandoli da un istanza di {@link ConcreteDoctorDAO}
     * <p>
     * {@inheritDoc}
     */
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
            this.deleteFirstAttachmentButton.setVisible(true);
            this.deleteFirstAttachmentButton.setDisable(true);
            this.deleteFirstAttachmentButton.setOnMouseClicked(mouseEvent -> {
                this.firstAttachment.setText("Nessuno");
                this.firstAttachment.setAlignment(Pos.CENTER);
                this.deleteFirstAttachmentButton.setVisible(false);
                this.textPath.clear();
            });
            this.firstAttachment.setText(this.report.getPathFile());
        }

        this.setButtons();
        String id_doctorSession = this.doctorRepo.search(SessionUser.getDoctor());
        String id_doctorReport = this.doctorRepo.search(this.doctor);

        if (id_doctorReport.equals(id_doctorSession)) {
            this.addEnableModifyCheckBox();
        }
        this.buttons.setAlignment(Pos.CENTER);
        this.buttons.setSpacing(20);
        this.pane_main_grid.getChildren().add(buttons);
        this.addCreateAndDeleteButtonsPDFReport();

        try {
            this.generateFolders(this.owner, this.pet, this.textPath.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Assegna la grafica ai bottoni per le operazioni sul report e assegna loro delle funzioni che permettono di
     * effettuare operazioni di creazione, eliminazione, modifica e annullamento modifiche sul report
     */
    private void setButtons() {
        // Creo il createButton
        this.creaPDFReportButton = new Button("Crea PDF");
        this.creaPDFReportButton.setId("createPDF");
        this.creaPDFReportButton.setPrefWidth(200);
        this.creaPDFReportButton.setPrefHeight(30);
        this.creaPDFReportButton.setStyle("-fx-background-color: #3DA4E3;-fx-text-fill: white;" +
                " -fx-border-color: transparent; -fx-font-size: 16px; ");
        this.creaPDFReportButton.setOnAction(actionEvent -> {
            try {
                creaReport(this.report, this.appointment, this.owner, this.pet, this.doctor);
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
        this.deleteReportButton.setOnAction(actionEvent -> this.reportDAO.delete(this.idBooking));

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
            this.refreshPage();
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
            this.textPath.setEditable(false);
            this.enableModify.setSelected(false);
            this.attachmentImage.setDisable(true);
            this.buttons.getChildren().clear();
            if (this.report.getPathFile().trim().isEmpty()) {
                this.firstAttachment.setText("Nessuno");
                this.deleteFirstAttachmentButton.setVisible(false);
            } else {
                this.firstAttachment.setText(this.report.getPathFile());
                this.deleteFirstAttachmentButton.setVisible(true);
                this.deleteFirstAttachmentButton.setDisable(true);
            }

            this.addCreateAndDeleteButtonsPDFReport();
        });
    }

    /**
     * Aggiorna la view per effettuare il refresh dei dati per la creazione del report in pdf aggiornato
     */
    private void refreshPage() {
        Scene scene = this.savePDFReportButton.getScene();
        BorderPane borderPane = (BorderPane) scene.lookup("#borderPane");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/createReport.fxml"));
        loader.setControllerFactory(p -> new CreateReportController(this.appointment, true));
        try {
            borderPane.setCenter(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Crea un nuovo oggetto di tipo {@link Report} grazie al Builder
     *
     * @return Un oggetto di tipo {@link Report}
     */
    private Report createNewReport() {
        return new Report.Builder()
                .setDiagnosis(this.textDiagnosi.getText())
                .setTreatments(this.textTerapia.getText())
                .setPathFile(this.textPath.getText())
                .build();
    }

    /**
     * Questa funzione permette di generare le cartelle dove verranno memorizzate delle copie di report generati con i
     * loro allegati, usando i parametri inseriti per customizzare i nomi di cartelle e file, e usando il codice fiscale
     * come chiave primaria della cartella in caso di omonimie
     *
     * @param owner      Il proprietario
     * @param pet        Il paziente
     * @param attachment Percorso dell'allegato
     * @throws IOException Genera un'eccezione nel caso in cui non si trovi il file
     */
    private void generateFolders(Owner owner, Pet pet, String attachment) throws IOException {
        String outputFile = "./report/";

        // Se non presenti, creazione cartelle utente e paziente dove mettere il report
        String reportDirectoryName = outputFile.concat(owner.getSurname() + owner.getName() + "_" +
                owner.getFiscalCode() + "/" + pet.getName());
        File reportDirectory = new File(reportDirectoryName);
        if (!reportDirectory.exists())
            reportDirectory.mkdirs();

        // Se non presenti, creazione cartella in cui inserire tutti gli allegati del paziente
        String attachmentDirectoryName = reportDirectoryName + "/allegati";
        File attachmentDirectory = new File(attachmentDirectoryName);
        if (!attachmentDirectory.exists())
            attachmentDirectory.mkdirs();
        pathfile = attachmentDirectoryName + "/" + attachment.substring(attachment.lastIndexOf("\\") + 1);
    }

    /**
     * Assegna ai due bottoni nell'{@link HBox} {@link ReportViewController#buttons} le funzioni di creazione report e
     * cancellazione
     */
    private void addCreateAndDeleteButtonsPDFReport() {
        this.buttons.getChildren().add(0, this.creaPDFReportButton);
        this.buttons.getChildren().add(1, this.deleteReportButton);
    }

    /**
     * Assegna ai due bottoni nell'{@link HBox} {@link ReportViewController#buttons} le funzioni di salvataggio report e
     * annullamento modifiche
     */
    private void addSaveAndCancelButtonsPDFReport() {
        this.buttons.getChildren().add(0, this.savePDFReportButton);
        this.buttons.getChildren().add(1, this.cancelReportButton);
    }

    /**
     * Gestisce l'abilitazione delle modifiche controllando le funzioni dei bottoni e abilitando o disabilitando i campi
     */
    private void addEnableModifyCheckBox() {
        this.enableModify = new CheckBox("Abilita modifiche");
        this.enableModify.setStyle("-fx-font-size: 14px; -fx-text-fill: #163754;");
        this.enableModify.setId("enableModify");
        this.enableModify.setOnAction(actionEvent -> {
            if (!enableModify.isSelected()) {
                this.buttons.getChildren().clear();
                this.addCreateAndDeleteButtonsPDFReport();
                this.textDiagnosi.setEditable(false);
                this.textTerapia.setEditable(false);
                this.textPath.setEditable(false);
                this.attachmentImage.setDisable(true);
                this.deleteFirstAttachmentButton.setDisable(true);

            } else {
                this.buttons.getChildren().clear();
                this.addSaveAndCancelButtonsPDFReport();
                textDiagnosi.setEditable(true);
                textTerapia.setEditable(true);
                textPath.setEditable(true);
                attachmentImage.setDisable(false);
                this.deleteFirstAttachmentButton.setDisable(false);
            }
        });
        this.lastHbox.setPadding(new Insets(20.0));
        this.lastHbox.getChildren().add(0, enableModify);
    }

    /**
     * Gestisce l'aggiunzione delle immagini allegate
     */
    public void findAttachment() {
        final FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter imageFilter
                = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
        fileChooser.getExtensionFilters().add(imageFilter);
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            String filePath = file.getAbsolutePath();
            if (this.firstAttachment.getText().equals("Nessuno")) {
                this.deleteFirstAttachmentButton.setVisible(true);
                this.deleteFirstAttachmentButton.setOnMouseClicked(mouseEvent -> {
                    this.firstAttachment.setText("Nessuno");
                    this.firstAttachment.setAlignment(Pos.CENTER);
                    this.deleteFirstAttachmentButton.setVisible(false);
                    this.textPath.clear();
                });
                this.firstAttachment.setText(filePath);
                this.textPath.setText(filePath);
            } else {
                this.textPath.setAlignment(Pos.CENTER_LEFT);
                this.textPath.setText(filePath);
            }
            this.attachments.add(filePath);
        }
    }
}
