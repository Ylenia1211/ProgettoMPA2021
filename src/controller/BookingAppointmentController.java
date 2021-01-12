package controller;

import dao.ConcreteAppointmentDAO;
import dao.ConcretePetDAO;
import datasource.ConnectionDBH2;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.converter.LocalTimeStringConverter;
import model.*;

import javax.swing.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.stream.Collectors;

import static controller.RegistrationPetController.getKeyByValue;

public class BookingAppointmentController implements Initializable {
    public VBox pane_main_grid;
    public Label labelTitle;
    public DatePicker textdateVisit;
    public ComboBox textTimeStart;
    public ComboBox textTimeDuration;

    public TextField textSpecialitation;
    public ComboBox textPet;
    private ConcreteAppointmentDAO appointmentRepo;
    public Button btn;
    public List<LocalTime> heuresWorkDay;
    public List<Integer> minutes;

    //servono per il campo ricerca owner -->pets
    private GridPane container;
    private  HBox searchBox;
    private TextField searchText;
    private VBox dropDownMenu;
    private Map<String, String> listClient;
    private List<Pet> listPets;
    private String idOwnerSearched;


    //servono per il campo ricerca doctor --> specialization
    private GridPane container2;
    private  HBox searchBox2;
    private TextField searchText2;
    private VBox dropDownMenu2;
    private Map<String, String> listDoctor;
    private String idDoctorSearched;
    private String specializationDoctor;

    public BookingAppointmentController() {
        this.listClient  = new HashMap<>();
        this.listDoctor  = new HashMap<>();
        try{
            ConnectionDBH2 connection = new ConnectionDBH2();
            this.appointmentRepo = new ConcreteAppointmentDAO(connection);
        }
        catch (Exception e){
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
                setDisable(empty || date.compareTo(today) < 0 );
            }
        });
        addFieldTimeStart();
        addFieldTimeDuration();
        addFieldDoctor();
        addFieldSpecialization();
        addFieldOwner();
        addFieldPet();
        addButtonSave();
        addActionButton();

    }

    public void registrationVisit(ActionEvent actionEvent) {
        LocalDate localDate = this.textdateVisit.getValue();
        LocalTime localTime = ((LocalTime) this.textTimeStart.getValue()).plusMinutes((Integer)this.textTimeDuration.getValue());
        Appointment p = createAppointment();
        System.out.println(p.toString());
        //inserire controlli
        //this.apponintment.add(p);
    }

    public Appointment createAppointment(){
        //System.out.println(this.textPet.getValue().toString()); test ok
        Appointment p = new Appointment.Builder()
                .setLocalDate(this.textdateVisit.getValue())
                .setLocalTimeStart((LocalTime)this.textTimeStart.getValue())
                .setLocalTimeEnd(((LocalTime) this.textTimeStart.getValue()).plusMinutes((Integer)this.textTimeDuration.getValue()))
                .setId_doctor(this.idDoctorSearched)
                .setSpecialitation(this.specializationDoctor)
                .setId_owner(this.idOwnerSearched)
                .setId_pet(this.textPet.getValue().toString())  //#Todo:da cambiare con id  idPetSearched
                .build();
        return p;
    }

    public void addFieldTimeStart()  {
        this.heuresWorkDay = List.of(
                LocalTime.of( 8 , 0 ) ,
                LocalTime.of( 9 , 0 ) ,
                LocalTime.of( 10 , 0 ) ,
                LocalTime.of( 11 , 0 ) ,
                LocalTime.of( 12 , 0 ) ,
                LocalTime.of( 13 , 0 ) ,
                LocalTime.of( 15 , 0 ) ,
                LocalTime.of( 16 , 0 ) ,
                LocalTime.of( 17 , 0 )
        );
        this.textTimeStart = new ComboBox(FXCollections.observableArrayList(this.heuresWorkDay));
        this.textTimeStart.setId("textTimeStart");
        this.textTimeStart.setPromptText("Seleziona Orario");
        this.pane_main_grid.getChildren().add(this.textTimeStart);
    }

    public void addFieldTimeDuration()  {
        this.minutes = List.of( 15 , 30 , 45, 60,  90, 120) ;
        this.textTimeDuration = new ComboBox(FXCollections.observableArrayList(this.minutes));
        this.textTimeDuration.setId("textTimeDuration");
        this.textTimeDuration.setPromptText("Seleziona Durata");
        this.pane_main_grid.getChildren().add(this.textTimeDuration);
    }

    public void addFieldDoctor()  {
        this.listDoctor = this.appointmentRepo.searchAllDoctorByFiscalCod(); //ricerca per codice fiscale
        //this.listDoctor.values().forEach(System.out::println);  //test ok
        this.container2 = new GridPane();
        this.searchBox2 = new HBox();
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
            this.dropDownMenu2.getChildren().clear();

        });
        this.searchBox2.getChildren().addAll(this.searchText2, clean); //, search);
        this.container2.add(this.searchBox2, 0, 0);
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
                label.setOnMouseEntered((e) -> {label.setBackground(new Background(new BackgroundFill(Color.YELLOW, null, null))); } );
                label.setOnMouseExited((e) -> {label.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, null, null))); } );
                label.setOnMouseClicked((e) -> {
                    this.searchText2.setText(label.getText());
                    this.idDoctorSearched = getKeyByValue(this.listDoctor,this.searchText2.getText());
                    this.dropDownMenu2.getChildren().clear();  //pulisce il drop-menu generale
                    this.specializationDoctor = this.appointmentRepo.searchSpecializationByDoctor(this.idDoctorSearched);
                    this.textSpecialitation.setText(this.specializationDoctor);

                });
            }
        }
        return dropDownMenu2; // alla fine restituire il VBox (cioè il menu a tendina)
    }

    public void addFieldSpecialization()  {
        this.textSpecialitation = new TextField();
        this.textSpecialitation.setText("Tipo di visita (automatico)");
        this.textSpecialitation.setEditable(false);
        this.pane_main_grid.getChildren().add(this.textSpecialitation);
    }

    public void addFieldPet()  {
        //List<String> empty = new ArrayList<>();

        this.textPet  = new ComboBox(FXCollections.observableArrayList(new ArrayList<>()));
        this.textPet.setPromptText("Aggiungi animale");
        this.pane_main_grid.getChildren().add(this.textPet);
    }


    public void addFieldOwner()  {
        this.listClient = this.appointmentRepo.searchAllClientByFiscalCod(); //ricerca per codice fiscale

        this.container = new GridPane();
        this.searchBox = new HBox();
        this.searchText = new TextField();

        //this.container.setGridLinesVisible(true);
        this.container.setAlignment(Pos.CENTER);
        this.searchText.setPromptText("Inserisci Codice Fiscale Cliente");
        //System.out.println(this.container.getAlignment().name());
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
            this.dropDownMenu.getChildren().clear();

        });

        this.searchBox.getChildren().addAll(this.searchText, clean); //, search);
        // add the search box to first row
        this.container.add(this.searchBox, 0, 0);
        //this.root.getChildren().add(this.container);
        this.pane_main_grid.getChildren().add(this.container); //aggiunge drop-menu a view

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
                label.setOnMouseEntered((e) -> {label.setBackground(new Background(new BackgroundFill(Color.YELLOW, null, null))); } );
                label.setOnMouseExited((e) -> {label.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, null, null))); } );
                label.setOnMouseClicked((e) -> {
                    this.searchText.setText(label.getText());
                    this.idOwnerSearched = getKeyByValue(listClient,this.searchText.getText());
                    this.dropDownMenu.getChildren().clear();  //pulisce il drop-menu generale
                    this.listPets = this.appointmentRepo.searchPetsByOwner(this.idOwnerSearched);
                    this.textPet.setItems(FXCollections.observableArrayList(this.listPets.stream().map(MasterData::getName).collect(Collectors.toList())));

                });
            }
        }
        return dropDownMenu; // alla fine restituire il VBox (cioè il menu a tendina)
    }

    public  void addButtonSave()  {
        this.btn = new Button("Salva");
        this.btn.setId("btn");
        this.pane_main_grid.getChildren().add(this.btn);
    }

    public void addActionButton() {
        this.btn.setOnAction(this::registrationVisit);
        JOptionPane.showMessageDialog(null, "Visita registrata correttamente");
    }


}
