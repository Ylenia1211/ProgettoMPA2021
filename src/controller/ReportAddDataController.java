package controller;

import dao.ConcreteReportDAO;
import datasource.ConnectionDBH2;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Appointment;
import model.Report;
import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Il controller della view {@link view/reportView.fxml}
 */
public class ReportAddDataController implements Initializable {
    private final String ownerSurname;
    private final String ownerName;
    private final String petName;
    private final String ownerFiscalCode;
    public TextArea textDiagnosi;
    public TextArea textTerapia;
    public TextField textPath;
    public ImageView attachmentImage;
//    public ArrayList<String> attachments = new ArrayList<>();
    public Label firstAttachment;
    public VBox allegati;
//    public Button creaReportButton;
//    public Button btnSaveReport;
    public VBox pane_main_grid;
    public ImageView deleteFirstAttachmentButton;
    private ConcreteReportDAO reportDAO;
    private Appointment appointment;
    private final String idOwner;
    private final String idPet;
    private String idBooking;
    public Button btn;

    /**
     * Costruttore dell classe, setta gli attributi dell'oggetto che serviranno alla generazione di un nome customizzato
     * per il report in formato pdf
     *
     * @param appointment L'appuntamento specifico
     * @param ownerSurname Il cognome del proprietario
     * @param ownerName Il nome del proprietario
     * @param petName Il nome del paziente
     * @param ownerFiscalCode Il codice fiscale del proprietario per evitare omonimie
     */
    public ReportAddDataController(Appointment appointment, String ownerSurname, String ownerName, String petName, String ownerFiscalCode) {
        this.appointment = appointment;
        this.idOwner = appointment.getId_owner();
        this.idPet = appointment.getId_pet();
        this.ownerSurname = ownerSurname;
        this.ownerName = ownerName;
        this.petName = petName;
        this.ownerFiscalCode = ownerFiscalCode;
    }

    /**
     * Setta i campi della view e vi inserisce nuovi elementi
     *
     * {@inheritDoc}
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.reportDAO = new ConcreteReportDAO(ConnectionDBH2.getInstance());
        this.idBooking = this.reportDAO.searchIdBookingByAppointment(this.appointment);
        this.attachmentImage.setStyle("-fx-background-image: url('/resources/attachment.png')");
        addButtonSave();
        addActionButton();
    }

    /**
     * Funzione che crea un oggetto report con i parametri inseriti tramite Builder e assegna a
     * {@link ReportAddDataController#reportDAO} il nuovo oggetto di tipo {@link Report} generato grazie al Builder
     *
     * @param actionEvent L'evento registrato, in questo caso il click sul bottone "Salva"
     */
    public void registrationReport(ActionEvent actionEvent) {
        Report newReport = new Report.Builder()
                .setId_booking(this.idBooking)
                .setId_owner(this.idOwner)
                .setId_pet(this.idPet)
                .setDiagnosis(this.textDiagnosi.getText())
                .setTreatments(this.textTerapia.getText())
                .setPathFile(changePath(this.textPath.getText()))
                .build();
        this.reportDAO.add(newReport);  //creazione oggetto Report e salvataggio in DB
        System.out.println(newReport.toString());
        try {
            this.generateFolders(ownerSurname, ownerName, ownerFiscalCode, petName, this.textPath.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Funzione che effettua la copia di qualsiasi tipo di file tramite byte
     *
     * @param src Percorso del file sorgente da copiare
     * @param dest Percorso di dove si vuole copiare il file
     * @throws IOException Genera un'eccezione nel caso in cui non si trovi il file da copiare
     */
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

    /**
     * Questa funzione permette di generare le cartelle dove verranno memorizzate delle copie di report generati con i
     * loro allegati, usando i parametri inseriti per customizzare i nomi di cartelle e file, e usando il codice fiscale
     * come chiave primaria della cartella in caso di omonimie
     *
     * @param ownerSurname Cognome del proprietario
     * @param ownerName Nome del proprietario
     * @param ownerFiscalCode Codice fiscale del proprietario
     * @param petName Nome dell'animale
     * @param attachment Percorso dell'allegato
     * @throws IOException Genera un'eccezione nel caso in cui non si trovi il file
     */
    private void generateFolders(String ownerSurname, String ownerName, String ownerFiscalCode, String petName, String attachment) throws IOException {
        String outputFile = "./report/";

        // Creazione cartelle utente e paziente dove mettere il report
        String reportDirectoryName = outputFile.concat(ownerSurname + ownerName + "_" + ownerFiscalCode + "/" + petName);
        File reportDirectory = new File(reportDirectoryName);
        if (!reportDirectory.exists())
            reportDirectory.mkdirs();

        // Creazione cartella in cui inserire tutti gli allegati del paziente
        String attachmentDirectoryName = reportDirectoryName + "/allegati";
        File attachmentDirectory = new File(attachmentDirectoryName);
        if (!attachmentDirectory.exists())
            attachmentDirectory.mkdirs();

        // Copio l'allegato in una cartella interna per poterlo recuperare in caso di trasferimento del software in un
        // altro pc
        String pathfile = attachmentDirectoryName + "/" + attachment.substring(attachment.lastIndexOf("\\")+1);
        this.copy(new File(attachment), new File(pathfile));
    }

    /**
     * Funzione che sostituisce il percorso del file allegato con quello interno al software, per poterlo ritrovare nel
     * caso di passaggio del software su un pc differente
     *
     * @param oldPath Il percorso del file inserito dall'utente
     * @return Ritorna il percorso interno nell'apposita cartella del paziente
     */
    public String changePath(String oldPath) {
        return "./report/" + this.ownerSurname + this.ownerName + "_" + this.ownerFiscalCode + "/" + this.petName + "/" + "allegati/" + oldPath.substring(oldPath.lastIndexOf("\\")+1);
    }

    /**
     * Funzione che crea un nuovo bottone "Salva" e lo aggiunge alla vista
     */
    public void addButtonSave() {
        this.btn = new Button("Salva");
        this.btn.setId("btn");
        this.btn.setPrefWidth(200);
        this.btn.setPrefHeight(30);

        this.btn.setStyle("-fx-background-color: #3DA4E3;-fx-text-fill: white;" +
                " -fx-border-color: transparent; -fx-font-size: 16px; ");
        this.pane_main_grid.getChildren().add(this.btn);
    }

    /**
     * Funzione che associa la funzione registrationReport al bottone "Salva"
     */
    public void addActionButton() {
        this.btn.setOnAction(this::registrationReport);
    }

    /**
     * Funzione che permette l'inserimento di un allegato (immagine) al report
     */
    public void findAttachment() {
        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            String filePath = file.getAbsolutePath();
            if (this.firstAttachment.getText().equals("Nessuno")) {
                this.deleteFirstAttachmentButton.setVisible(true);
                this.deleteFirstAttachmentButton.setOnMouseClicked(mouseEvent -> {
                    this.firstAttachment.setText("Nessuno");
                    this.firstAttachment.setAlignment(Pos.CENTER_LEFT);
                    this.deleteFirstAttachmentButton.setVisible(false);
                    this.textPath.clear();
                });
                this.firstAttachment.setText(filePath);
            }
            else
                this.allegati.getChildren().add(new Label(filePath));
            //this.attachments.add(filePath);
            this.textPath.setText(filePath);
        }
    }
}