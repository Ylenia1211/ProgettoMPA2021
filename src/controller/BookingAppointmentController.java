package controller;

import dao.ConcreteAppointmentDAO;
import datasource.ConnectionDBH2;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import model.Appointment;
import util.FieldVerifier;

import javax.swing.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

import static controller.RegistrationPetController.getKeyByValue;

public class BookingAppointmentController implements Initializable, FieldVerifier {
    public VBox pane_main_grid;
    public Label labelTitle;
    public DatePicker textdateVisit;
    private ComboBox<Object> textTimeStart;
    private ComboBox<Object> textMinutesTimeStart;
    private ComboBox<Object> textTimeDuration;

    public TextField textSpecialitation;
    public ComboBox<String> textPet;
    private ConcreteAppointmentDAO appointmentRepo;
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

    public BookingAppointmentController() {
        this.listClient = new HashMap<>();
        this.listDoctor = new HashMap<>();
        this.listPets = new HashMap<>();
        try {
            //ConnectionDBH2 connection = new ConnectionDBH2();
            this.appointmentRepo = new ConcreteAppointmentDAO(ConnectionDBH2.getInstance());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //posso selezionare solo le date a partire da Oggi
        this.textdateVisit.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.compareTo(today) < 0 || (date.getDayOfWeek().getValue() == 7));//disabilita le domeniche
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

    }


    public ComboBox<Object> getTextMinutesTimeStart() {
        return textMinutesTimeStart;
    }


    public ConcreteAppointmentDAO getAppointmentRepo() {
        return appointmentRepo;
    }

    public ComboBox<Object> getTextTimeStart() {
        return textTimeStart;
    }

    public ComboBox<Object> getTextTimeDuration() {
        return textTimeDuration;
    }


    public void registrationVisit(ActionEvent actionEvent) throws IllegalAccessException {
        // ricerca prenotazioni per quel dottore in quella data
        Stream<Object> fieldsText;
        fieldsText = Stream.of( this.idDoctorSearched,this.specializationDoctor,this.idOwnerSearched);
        var fieldsComboBox =  Stream.of(this.textPet, this.textTimeStart, this.textMinutesTimeStart, this.textTimeDuration);

        if(checkNull(fieldsText) || checkEmptyComboBox(fieldsComboBox)) {
            JOptionPane.showMessageDialog(null, "Impossibile inserire la prenotazione. Devi riempire tutti i campi!");
        }else
        {
            Appointment p = createAppointment();
            List<Appointment> listAppointment = this.appointmentRepo.searchVisitbyDoctorAndDate(this.idDoctorSearched, this.textdateVisit.getValue().toString());
            boolean isValid = listAppointment.stream().allMatch(item -> (item.getLocalTimeStart().isAfter(p.getLocalTimeStart()) &&
                    (item.getLocalTimeStart().isAfter(p.getLocalTimeEnd()) || item.getLocalTimeStart().equals(p.getLocalTimeEnd()))) ||

                    ((item.getLocalTimeEnd().isBefore(p.getLocalTimeStart()) || item.getLocalTimeEnd().equals(p.getLocalTimeStart())) &&
                            item.getLocalTimeEnd().isBefore(p.getLocalTimeEnd()))
            );
            if (isValid) {
                this.appointmentRepo.add(p);
            } else {
                JOptionPane.showMessageDialog(null, "Impossibile inserire la prenotazione. Un altro appuntamento è già stato prenotato per quell'intervallo di tempo!");
            }

        }
    }

    public Appointment createAppointment() {
        String idPetSearched = getKeyByValue(this.listPets, this.textPet.getValue());
        //System.out.println(this.idPetSearched); ok
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

    public void addFieldTimeStart() {
        HBox timeStartBox = new HBox();
        timeStartBox.setAlignment(Pos.CENTER);
        Label labelTimeStart = new Label("Ora Inizio: ");
        labelTimeStart.setStyle("-fx-text-fill: white");
        this.textTimeStart = new ComboBox<>(FXCollections.observableArrayList(this.heuresWorkDay));
        this.textTimeStart.setId("textTimeStart");
        this.textTimeStart.setPromptText("Seleziona Orario");
        timeStartBox.getChildren().addAll(labelTimeStart, textTimeStart);
        this.pane_main_grid.getChildren().add(timeStartBox);
    }

    public void addFieldMinutesTimeStart() {
        HBox minutesStartBox = new HBox();
        minutesStartBox.setAlignment(Pos.CENTER);
        Label labelMinutesStart = new Label("Minuti Inizio: ");
        labelMinutesStart.setStyle("-fx-text-fill: white");
        this.textMinutesTimeStart = new ComboBox<>(FXCollections.observableArrayList(this.minutesStart));
        this.textMinutesTimeStart.setId("textMinutesTimeStart");
        this.textMinutesTimeStart.setPromptText("Seleziona minuti");
        minutesStartBox.getChildren().addAll(labelMinutesStart, textMinutesTimeStart);
        this.pane_main_grid.getChildren().add(minutesStartBox);
    }

    public void addFieldTimeDuration() {
        HBox durationBox = new HBox();
        durationBox.setAlignment(Pos.CENTER);
        Label labelMinutes = new Label("Durata (prevista): ");
        labelMinutes.setStyle("-fx-text-fill: white");
        this.textTimeDuration = new ComboBox<>(FXCollections.observableArrayList(this.minutes));
        this.textTimeDuration.setId("textTimeDuration");
        this.textTimeDuration.setPromptText("Seleziona Durata");
        durationBox.getChildren().addAll(labelMinutes, textTimeDuration);
        this.pane_main_grid.getChildren().add(durationBox);
    }

    public void addFieldDoctor() {
        this.listDoctor = this.appointmentRepo.searchAllDoctorByFiscalCod(); //ricerca per codice fiscale
        //this.listDoctor.values().forEach(System.out::println);  //test ok
        this.container2 = new GridPane();

        HBox searchBox2 = new HBox();
        //searchBox2.setPrefWidth(400);
        this.searchText2 = new TextField();
        this.container2.setAlignment(Pos.CENTER);
        this.searchText2.setPromptText("Inserisci Codice Fiscale Dottore");  //#todo:per ora è surname
        // aggiungere un ascoltatore per ascoltare le modifiche nel campo di testo
        this.searchText2.textProperty().addListener((observable, oldValue, newValue) -> {
            if (container2.getChildren().size() > 1) { // if already contains a drop-down menu -> remove it
                container2.getChildren().remove(1);
            }
            container2.add(populateDropDownMenuDoctor(newValue, this.listDoctor.values()), 0, 1); //  quindi aggiungere il menu a tendina popolato alla seconda riga del riquadro della griglia
        });
        Button clean = new Button("Cancella");
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

    public void addFieldSpecialization() {
        this.textSpecialitation = new TextField();
        this.textSpecialitation.setMaxWidth(400);
        this.textSpecialitation.setText("Tipo di visita (automatico)");
        this.textSpecialitation.setEditable(false);
        this.pane_main_grid.getChildren().add(this.textSpecialitation);
    }

    public void addFieldPet() {
        //List<String> empty = new ArrayList<>();
        this.textPet = new ComboBox<>(FXCollections.observableArrayList(new ArrayList<>()));
        this.textPet.setPromptText("Aggiungi animale");
        this.textPet.setPrefWidth(400);
        this.pane_main_grid.getChildren().add(this.textPet);
    }

    public void addFieldOwner() {
        this.listClient = this.appointmentRepo.searchAllClientByFiscalCod(); //ricerca per codice fiscale
        this.container = new GridPane();
        HBox searchBox = new HBox();
        this.searchText = new TextField();
        this.container.setAlignment(Pos.CENTER);
        //this.container.setPrefWidth(400);
        this.searchText.setPromptText("Inserisci Codice Fiscale Cliente");
        // aaggiungere un ascoltatore per ascoltare le modifiche nel campo di testo
        this.searchText.textProperty().addListener((observable, oldValue, newValue) -> {
            if (container.getChildren().size() > 1) { // if already contains a drop-down menu -> remove it
                container.getChildren().remove(1);
            }
            container.add(populateDropDownMenu(newValue, this.listClient.values()), 0, 1); //  quindi aggiungere il menu a tendina popolato alla seconda riga del riquadro della griglia
        });

        Button clean = new Button("Cancella");
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

    // questo metodo cerca un dato testo in una collection di stringhe (cioè le opzioni del menu)
    // poi restituisce un VBox contenente tutti i match
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

    public void addButtonSave() {
        this.btn = new Button("Salva");
        this.btn.setId("btn");
        this.btn.setPrefWidth(400);
        this.btn.setPrefHeight(30);
        this.btn.setStyle("-fx-background-color: #3DA4E3;-fx-text-fill: white;" +
                " -fx-border-color: transparent; -fx-font-size: 14px; ");
        this.pane_main_grid.getChildren().add(this.btn);
    }

    public void addActionButton() {
        this.btn.setOnAction(actionEvent -> {
            try {
                registrationVisit(actionEvent);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }


}
