package controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.converter.LocalTimeStringConverter;
import model.Pet;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BookingAppointmentController implements Initializable {
    public VBox pane_main_grid;
    public Label labelTitle;
    public DatePicker textdateVisit;
    public ComboBox textTimeStart;
    public ComboBox textTimeDuration;
    //public ComboBox textDoctor;
    public TextField textSpecialitation;
    //public ComboBox textOwner;
    //public ComboBox textPet;
    public Button btn;
    public List<LocalTime> heuresWorkDay;
    public List<Integer> minutes;

    public BookingAppointmentController() {

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
        addButtonSave();
        addActionButton();
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
        this.minutes = List.of( 15 , 30 , 45) ;
        this.textTimeDuration = new ComboBox(FXCollections.observableArrayList(this.minutes));
        this.textTimeDuration.setId("textTimeDuration");
        this.textTimeDuration.setPromptText("Seleziona Durata");
        this.pane_main_grid.getChildren().add(this.textTimeDuration);
    }

    public  void addButtonSave()  {
        this.btn = new Button("Salva");
        this.btn.setId("btn");
        this.pane_main_grid.getChildren().add(this.btn);
    }

    public void addActionButton() {
        this.btn.setOnAction(this::registrationVisit);
    }

    public void registrationVisit(ActionEvent actionEvent) {

        LocalDate localDate = this.textdateVisit.getValue();
        LocalDateTime trial = LocalDateTime.of(2021, 1, 20, 12, 30);
        LocalTime localTime = ((LocalTime) this.textTimeStart.getValue()).plusMinutes((Integer)this.textTimeDuration.getValue());
        LocalDateTime localDateTime = LocalDateTime.of( localDate , localTime ) ;
        //System.out.println(localDateTime);

        System.out.println(localDateTime.isAfter(trial));

        String trialConversion = localDateTime.toString();
        String[] parts = trialConversion.split("T");
        String date = parts[0];
        String heur = parts[1];
        String str = date+ " " +heur;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(str, formatter);
        System.out.println(dateTime);
        //Pet p = createPet();
        //inserire controlli
        //this.petRepo.add(p);
    }
}
