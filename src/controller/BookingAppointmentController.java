package controller;

import dao.ConcreteAppointmentDAO;
import datasource.ConnectionDBH2;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import model.Appointment;
import util.FieldVerifier;

import javax.swing.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static controller.RegistrationPetController.getKeyByValue;
import static util.gui.ButtonTable.MAX_SIZE;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * La classe BookingAppointmentController serve a registrare un Appuntamento.
 * Implementando i metodi di 'Initializable' {@link Initializable} inizializza la view associata al controller.
 * Essa e' il controller primario della view {@link view/bookingAppointment.fxml}
 * <p>
 * Implementa 'FieldVerifier' {@link FieldVerifier} per richiamare i metodi di controllo sui campi della registrazione del'Appointment.
 */
public class BookingAppointmentController implements Initializable, FieldVerifier {
    public VBox pane_main_grid;
    public Label labelTitle;
    public DatePicker textdateVisit;
    public VBox primary_grid;
    private ComboBox<Object> textTimeStart;
    private ComboBox<Object> textMinutesTimeStart;
    private ComboBox<Object> textTimeDuration;

    public TextField textSpecialitation;
    public ComboBox<String> textPet;
    private final ConcreteAppointmentDAO appointmentRepo;
    public Button btn;
    public List<LocalTime> heuresWorkDay;
    public List<Integer> minutes;

    //servono per il campo ricerca owner -->pets
    private GridPane container;
    private TextField searchText;
    private VBox dropDownMenu;
    private Map<String, String> listClient;
    private Map<String, String> listPets;
    private String idOwnerSearched;


    //servono per il campo ricerca doctor --> specialization
    private GridPane container2;
    private TextField searchText2;
    private VBox dropDownMenu2;
    private Map<String, String> listDoctor;
    private String idDoctorSearched;
    private String specializationDoctor;
    private List<Integer> minutesStart;

    private List<Object> fieldsText;
    private List<ComboBox<?>> fieldsComboBox;


    /**
     * Il costruttore della classe BookingAppointmentController, inizializza tutti i le liste utili per selezionare i campi nella view e crea
     * {@link BookingAppointmentController#appointmentRepo} un oggetto di tipo {@link ConcreteAppointmentDAO} richiamando la Connessione singleton {@link ConnectionDBH2} del database.
     */
    public BookingAppointmentController() {
        this.listClient = new HashMap<>();
        this.listDoctor = new HashMap<>();
        this.listPets = new HashMap<>();
        this.appointmentRepo = new ConcreteAppointmentDAO(ConnectionDBH2.getInstance());
    }

    /**
     * {@inheritDoc}
     * <p>
     * Inizializza i campi della view in modo appropriato.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //posso selezionare solo le date a partire da Oggi
        this.textdateVisit.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable((empty || date.compareTo(today) < 0) || (date.getDayOfWeek().getValue() == 7));//disabilita le domeniche
            }
        });
        this.textdateVisit.setValue(LocalDate.now());
        this.textdateVisit.setEditable(false);

        //settiamo le possibili ore lavorative
        this.heuresWorkDay = List.of(
                LocalTime.of(8, 0),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0),
                LocalTime.of(11, 0),
                LocalTime.of(12, 0),
                LocalTime.of(13, 0),
                LocalTime.of(15, 0),
                LocalTime.of(16, 0),
                LocalTime.of(17, 0)
        );

        this.minutes = List.of(15, 30, 45, 60, 90, 120);
        this.minutesStart = List.of(0, 15, 30, 45);

        addFieldTimeStart();
        addFieldMinutesTimeStart();
        addFieldTimeDuration();
        addFieldDoctor();
        addFieldSpecialization();
        addFieldOwner();
        addFieldPet();
        addButtonSave();
        addActionButton();

        //devono essere inizializzati
        this.idDoctorSearched = "";
        this.idOwnerSearched = "";
        this.specializationDoctor = "";

        this.fieldsText = List.of(this.idDoctorSearched, this.specializationDoctor, this.idOwnerSearched);
        this.fieldsComboBox = List.of(this.textPet, this.textTimeStart, this.textMinutesTimeStart, this.textTimeDuration);

    }


    /**
     * Funzione associata al {@link Button} {@link BookingAppointmentController#btn}, registra un nuovo appuntamento se tutti i  campi della prenotazione sono settati e verifica che
     * non si stia inserendo un Appuntamento non valido (sovrapposto con altri appuntamenti per lo stesso dottore e animale nel medesimo tempo)
     */
    public void registrationVisit() throws IllegalAccessException {
        // ricerca prenotazioni per quel dottore in quella data
        if (checkNull(this.fieldsText.stream()) || checkEmptyComboBox(this.fieldsComboBox.stream()) || this.specializationDoctor.contains("Tipo di visita (automatico)")) {
            JOptionPane.showMessageDialog(null, "Impossibile inserire la prenotazione. Devi riempire tutti i campi!");
        } else {
            Appointment p = createAppointment();
            if ((p.getLocalTimeStart().isBefore(LocalTime.now()) && p.getLocalDate().isEqual(LocalDate.now()))) {
                JOptionPane.showMessageDialog(null, "Impossibile inserire prenotazione: l'orario deve essere successivo all'orario attuale!");
            } else {

                List<Appointment> listAppointment = this.appointmentRepo.searchVisitbyDoctorOrPetAndDate(this.idDoctorSearched, p.getId_pet(), this.textdateVisit.getValue().toString());
                boolean isValid = listAppointment.stream().allMatch(item -> (item.getLocalTimeStart().isAfter(p.getLocalTimeStart()) &&
                        (item.getLocalTimeStart().isAfter(p.getLocalTimeEnd()) || item.getLocalTimeStart().equals(p.getLocalTimeEnd()))) ||
                        ((item.getLocalTimeEnd().isBefore(p.getLocalTimeStart()) || item.getLocalTimeEnd().equals(p.getLocalTimeStart())) &&
                                item.getLocalTimeEnd().isBefore(p.getLocalTimeEnd()))
                );


                if (checkSearchFieldIsCorrect(this.listDoctor.values(), this.searchText2.getText()) &&
                        checkSearchFieldIsCorrect(this.listClient.values(), this.searchText.getText())) {
                    if (isValid) {
                        this.appointmentRepo.add(p);
                    } else {
                        JOptionPane.showMessageDialog(null, "Impossibile inserire la prenotazione. Un altro appuntamento è già stato prenotato per quell'intervallo di tempo!");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Impossibile trovare il riferimento! Per favore, seleziona un opzione del menu a tendina!");
                }
            }
        }
    }

    /**
     * Crea un nuovo oggetto di tipo {@link Appointment} con i parametri inseriti nei campi della view
     *
     * @return Un nuovo oggetto di tipo {@link Appointment}
     */
    public Appointment createAppointment() {
        String idPetSearched = getKeyByValue(this.listPets, this.textPet.getValue());
        LocalTime timeStartVisit = ((LocalTime) this.textTimeStart.getValue()).plusMinutes((Integer) this.textMinutesTimeStart.getValue());
        return new Appointment.Builder()
                .setLocalDate(this.textdateVisit.getValue())
                .setLocalTimeStart(timeStartVisit)
                .setLocalTimeEnd(timeStartVisit.plusMinutes((Integer) this.textTimeDuration.getValue()))
                .setId_doctor(this.idDoctorSearched)
                .setSpecialitation(this.specializationDoctor.toUpperCase())
                .setId_owner(this.idOwnerSearched)
                .setId_pet(idPetSearched)
                .build();
    }

    /**
     * Aggiunge alla view il componente grafico per il settaggio dell'orario di inizio visita da associare all'Appointment
     */
    public void addFieldTimeStart() {
        HBox timeStartBox = new HBox();
        timeStartBox.setPrefWidth(800);
        timeStartBox.setAlignment(Pos.CENTER);
        Label labelTimeStart = new Label("Ora Inizio: ");
        labelTimeStart.setPrefWidth(200);
        labelTimeStart.setStyle("-fx-text-fill: #163754; -fx-font-size: 14px");
        this.textTimeStart = new ComboBox<>(FXCollections.observableArrayList(this.heuresWorkDay));
        this.textTimeStart.setId("textTimeStart");
        this.textTimeStart.setPromptText("Seleziona Orario");
        this.textTimeStart.setPrefWidth(600);
        this.textTimeStart.setBackground(new Background(new BackgroundFill(Color.valueOf("#94D6F1"), CornerRadii.EMPTY, Insets.EMPTY)));
        timeStartBox.getChildren().addAll(labelTimeStart, textTimeStart);
        this.pane_main_grid.getChildren().add(timeStartBox);
    }

    /**
     * Aggiunge alla view il componente grafico per il settaggio dei minuti di inizio visita da associare all'Appointment
     */
    public void addFieldMinutesTimeStart() {
        HBox minutesStartBox = new HBox();
        minutesStartBox.setPrefWidth(800);
        minutesStartBox.setAlignment(Pos.CENTER);
        Label labelMinutesStart = new Label("Minuti Inizio: ");
        labelMinutesStart.setPrefWidth(200);
        labelMinutesStart.setStyle("-fx-text-fill: #163754; -fx-font-size: 14px");
        this.textMinutesTimeStart = new ComboBox<>(FXCollections.observableArrayList(this.minutesStart));
        this.textMinutesTimeStart.setId("textMinutesTimeStart");
        this.textMinutesTimeStart.setPromptText("Seleziona minuti");
        this.textMinutesTimeStart.setPrefWidth(600);
        this.textMinutesTimeStart.setBackground(new Background(new BackgroundFill(Color.valueOf("#94D6F1"), CornerRadii.EMPTY, Insets.EMPTY)));
        minutesStartBox.getChildren().addAll(labelMinutesStart, textMinutesTimeStart);
        this.pane_main_grid.getChildren().add(minutesStartBox);
    }

    /**
     * Aggiunge alla view il componente grafico per il settaggio della durata (prevista) della visita, da associare all'Appointment
     */
    public void addFieldTimeDuration() {
        HBox durationBox = new HBox();
        durationBox.setPrefWidth(800);
        durationBox.setAlignment(Pos.CENTER);
        Label labelMinutes = new Label("Durata (prevista): ");
        labelMinutes.setStyle("-fx-text-fill: #163754; -fx-font-size: 14px");
        labelMinutes.setPrefWidth(200);
        this.textTimeDuration = new ComboBox<>(FXCollections.observableArrayList(this.minutes));
        this.textTimeDuration.setId("textTimeDuration");
        this.textTimeDuration.setPromptText("Seleziona Durata");
        this.textTimeDuration.setPrefWidth(600);
        this.textTimeDuration.setBackground(new Background(new BackgroundFill(Color.valueOf("#94D6F1"), CornerRadii.EMPTY, Insets.EMPTY)));
        durationBox.getChildren().addAll(labelMinutes, textTimeDuration);
        this.pane_main_grid.getChildren().add(durationBox);
    }

    /**
     * Aggiunge alla view il componente grafico per la ricerca dei dati relativi al Dottore da associare all'Appointment
     */
    public void addFieldDoctor() {
        this.listDoctor = this.appointmentRepo.searchAllDoctorByFiscalCod(); //ricerca per codice fiscale
        this.container2 = new GridPane();
        HBox searchBox2 = new HBox();

        this.searchText2 = new TextField();
        this.searchText2.setPrefWidth(800);
        this.container2.setAlignment(Pos.CENTER);
        this.searchText2.setStyle("-fx-border-color:#3da4e3; -fx-prompt-text-fill:#163754");
        this.searchText2.setPromptText("Inserisci Codice Fiscale Dottore");
        // aggiungere un ascoltatore per ascoltare le modifiche nel campo di testo
        this.searchText2.textProperty().addListener((observable, oldValue, newValue) -> {
            if (container2.getChildren().size() > 1) { // if already contains a drop-down menu -> remove it
                container2.getChildren().remove(1);
            }
            container2.add(populateDropDownMenuDoctor(newValue, this.listDoctor.values()), 0, 1); //  quindi aggiungere il menu a tendina popolato alla seconda riga del riquadro della griglia
        });
        //Button clean = new Button("Cancella");
        final ImageView imageView = new ImageView(
                new Image("./delete.png")
        );
        imageView.setFitHeight(18);
        imageView.setFitWidth(18);
        Button clean = new Button("", imageView);
        clean.setStyle("-fx-text-fill: white; -fx-background-color: #3da4e3");
        clean.setOnMouseClicked((e) -> {
            this.searchText2.clear();
            if (this.dropDownMenu != null) { // necessario senno NUllPointer exception
                this.dropDownMenu.getChildren().clear();
            }

        });
        searchBox2.getChildren().addAll(this.searchText2, clean); //, search);
        this.container2.add(searchBox2, 0, 0);
        this.pane_main_grid.getChildren().add(this.container2); //aggiunge drop-menu a view
    }


    /**
     * Questo metodo cerca un dato testo in una collection di stringhe (cioè le opzioni del menu) poi restituisce un
     * {@link VBox} contenente tutti i match
     *
     * @param text    La stringa da ricercare
     * @param options La collection in cui effettuare la ricerca
     * @return Il {@link VBox} con tutti i match
     */
    private VBox populateDropDownMenuDoctor(String text, Collection<String> options) {
        dropDownMenu2 = new VBox();
        dropDownMenu2.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, null, null)));
        for (String option : options) {
            //  se il testo dato non è vuoto e non è composto solo da spazi
            if (!text.replace(" ", "").isEmpty() && option.toUpperCase().contains(text.toUpperCase())) {
                Label label = new Label(option); // create a label and inserisce il testo dell'opzione
                // per poter cliccare sulle opzioni del menu a tendina
                dropDownMenu2.getChildren().add(label); // inserisce label a VBox
                label.setOnMouseEntered((e) -> label.setBackground(new Background(new BackgroundFill(Color.YELLOW, null, null))));
                label.setOnMouseExited((e) -> label.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, null, null))));
                label.setOnMouseClicked((e) -> {
                    this.searchText2.setText(label.getText());
                    this.idDoctorSearched = getKeyByValue(this.listDoctor, this.searchText2.getText());
                    this.dropDownMenu2.getChildren().clear();  //pulisce il drop-menu generale
                    this.specializationDoctor = this.appointmentRepo.searchSpecializationByDoctor(this.idDoctorSearched);
                    this.textSpecialitation.setText(this.specializationDoctor);

                });
            }
        }
        return dropDownMenu2; // alla fine restituire il VBox (cioè il menu a tendina)
    }

    /**
     * Aggiunge alla view il componente grafico per il settaggio di una specializzazione (tipo di visita) da associare all'Appointment
     */
    public void addFieldSpecialization() {
        this.textSpecialitation = new TextField();
        //this.textSpecialitation.setMaxWidth(400);
        this.textSpecialitation.setText("Tipo di visita (automatico)");
        this.textSpecialitation.setEditable(false);
        this.pane_main_grid.getChildren().add(this.textSpecialitation);
    }

    /**
     * Aggiunge alla view il componente grafico per l'aggiunzione di un Pet da associare all'Appointment
     */
    public void addFieldPet() {
        this.textPet = new ComboBox<>(FXCollections.observableArrayList(new ArrayList<>()));
        this.textPet.setPromptText("Aggiungi animale");
        this.textPet.setBackground(new Background(new BackgroundFill(Color.valueOf("#94D6F1"), CornerRadii.EMPTY, Insets.EMPTY)));
        this.textPet.setPrefWidth(800);
        this.pane_main_grid.getChildren().add(this.textPet);
    }

    /**
     * Aggiunge alla view il componente grafico per la ricerca dei dati relativi al Owner da associare all'Appointment
     */
    public void addFieldOwner() {
        this.listClient = this.appointmentRepo.searchAllClientByFiscalCod(); //ricerca per codice fiscale
        this.container = new GridPane();
        HBox searchBox = new HBox();
        this.searchText = new TextField();

        this.searchText.setPrefWidth(800);
        this.container.setAlignment(Pos.CENTER);
        this.searchText.setStyle("-fx-border-color:#3da4e3; -fx-prompt-text-fill:#163754");
        this.searchText.setPromptText("Inserisci Codice Fiscale Cliente");
        // aaggiungere un ascoltatore per ascoltare le modifiche nel campo di testo
        this.searchText.textProperty().addListener((observable, oldValue, newValue) -> {
            if (container.getChildren().size() > 1) { // if already contains a drop-down menu -> remove it
                container.getChildren().remove(1);
            }
            container.add(populateDropDownMenu(newValue, this.listClient.values()), 0, 1); //  quindi aggiungere il menu a tendina popolato alla seconda riga del riquadro della griglia
        });
        //Button clean = new Button("Cancella");
        final ImageView imageView = new ImageView(
                new Image("./delete.png")
        );
        imageView.setFitHeight(18);
        imageView.setFitWidth(18);
        Button clean = new Button("", imageView);
        clean.setStyle("-fx-text-fill: white; -fx-background-color: #3da4e3");
        clean.setOnMouseClicked((e) -> {
            this.searchText.clear();
            if (this.dropDownMenu != null) { // necessario senno NUllPointer exception
                this.dropDownMenu.getChildren().clear();
            }

        });

        searchBox.getChildren().addAll(this.searchText, clean); //, search);
        // add the search box to first row
        this.container.add(searchBox, 0, 0);
        //this.root.getChildren().add(this.container);
        this.pane_main_grid.getChildren().add(this.container); //aggiunge drop-menu a view
        this.container.setId("searchtextOwner");
    }


    /**
     * Questo metodo cerca un dato testo in una collection di stringhe (cioè le opzioni del menu) poi restituisce un
     * {@link VBox} contenente tutti i match
     *
     * @param text    La stringa da ricercare
     * @param options La collection in cui effettuare la ricerca
     * @return Il {@link VBox} con tutti i match
     */
    public VBox populateDropDownMenu(String text, Collection<String> options) {
        dropDownMenu = new VBox();
        dropDownMenu.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, null, null)));
        //dropDownMenu.setAlignment(Pos.CENTER);

        for (String option : options) {
            //  se il testo dato non è vuoto e non è composto solo da spazi
            if (!text.replace(" ", "").isEmpty() && option.toUpperCase().contains(text.toUpperCase())) {
                Label label = new Label(option); // create a label and inserisce il testo dell'opzione
                // per poter cliccare sulle opzioni del menu a tendina
                dropDownMenu.getChildren().add(label); // inserisce label a VBox
                label.setOnMouseEntered((e) -> label.setBackground(new Background(new BackgroundFill(Color.YELLOW, null, null))));
                label.setOnMouseExited((e) -> label.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, null, null))));
                label.setOnMouseClicked((e) -> {
                    this.searchText.setText(label.getText());
                    this.idOwnerSearched = getKeyByValue(listClient, this.searchText.getText());
                    this.dropDownMenu.getChildren().clear();  //pulisce il drop-menu generale
                    this.listPets = this.appointmentRepo.searchPetsByOwner(this.idOwnerSearched); //trovo gli animali associati all'owner
                    this.textPet.setItems(FXCollections.observableArrayList(this.listPets.values()));
                    //this.textPet.setItems(FXCollections.observableArrayList(this.listPets.stream().map(MasterData::getName).collect(Collectors.toList())));
                });
            }
        }
        return dropDownMenu; // alla fine restituire il VBox (cioè il menu a tendina)
    }

    /**
     * Aggiunge il {@link Button} {@link BookingAppointmentController#btn} alla view della registrazione
     * dell'Appointment
     */
    public void addButtonSave() {
        this.btn = new Button("Salva");
        this.btn.setId("btn");
        this.btn.setMaxWidth(MAX_SIZE); //MAX_SIZE
        this.btn.setPrefWidth(Region.USE_COMPUTED_SIZE);
        VBox.setMargin(btn, new Insets(0, 100, 0, 100));
        this.btn.setStyle("-fx-background-color: #3da4e3; -fx-text-fill: white;" +
                "-fx-border-color: transparent; -fx-font-size: 14px;");
        this.pane_main_grid.getChildren().add(this.btn);
    }

    /**
     * Assegna al {@link Button}  {@link BookingAppointmentController#btn} la funzione
     * {@link  BookingAppointmentController#registrationVisit()}
     */
    public void addActionButton() {
        this.btn.setOnAction(actionEvent -> {
            try {
                registrationVisit();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Getter dell'attributo {@link BookingAppointmentController#labelTitle}
     *
     * @return Il campo {@link BookingAppointmentController#labelTitle}
     */
    public Label getLabelTitle() {
        return labelTitle;
    }

    /**
     * Getter dell'attributo {@link BookingAppointmentController#labelTitle}
     *
     * @return Il campo {@link BookingAppointmentController#labelTitle}
     */
    public DatePicker getTextdateVisit() {
        return textdateVisit;
    }

    /**
     * Getter dell'attributo {@link BookingAppointmentController#textMinutesTimeStart}
     *
     * @return Il campo {@link BookingAppointmentController#textMinutesTimeStart}
     */
    public ComboBox<Object> getTextMinutesTimeStart() {
        return textMinutesTimeStart;
    }

    /**
     * Getter dell'attributo {@link BookingAppointmentController#appointmentRepo}
     *
     * @return oggetto di tipo {@link ConcreteAppointmentDAO }
     */
    public ConcreteAppointmentDAO getAppointmentRepo() {
        return appointmentRepo;
    }


    /**
     * Getter dell'attributo {@link BookingAppointmentController#textTimeStart}
     *
     * @return Il campo {@link BookingAppointmentController#textTimeStart}
     */
    public ComboBox<Object> getTextTimeStart() {
        return textTimeStart;
    }

    /**
     * Getter dell'attributo {@link BookingAppointmentController#textTimeDuration}
     *
     * @return Il campo {@link BookingAppointmentController#textTimeDuration}
     */
    public ComboBox<Object> getTextTimeDuration() {
        return textTimeDuration;
    }

    /**
     * Getter dell'attributo {@link BookingAppointmentController#fieldsText}
     *
     * @return la lista di campi testuali della view {@link BookingAppointmentController#fieldsText}
     */
    public List<Object> getFieldsText() {
        return fieldsText;
    }

    /**
     * Getter dell'attributo {@link BookingAppointmentController#fieldsComboBox}
     *
     * @return la lista di campi combo box della view {@link BookingAppointmentController#fieldsComboBox}
     */
    public List<ComboBox<?>> getFieldsComboBox() {
        return fieldsComboBox;
    }
}
