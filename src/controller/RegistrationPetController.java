package controller;

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
import model.Gender;
import model.Pet;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.*;

public class RegistrationPetController implements Initializable {
    public VBox pane_main_grid;
    public Label labelTitle;
    public TextField textName;
    public TextField textSurname;
    public HBox gender;
    public RadioButton rbM;
    public ToggleGroup genderGroup;
    public RadioButton rbF;
    public DatePicker textdateBirth;
    public ComboBox textPetRace;
    //public ComboBox textOwner;
    public TextField textParticularSign;
    public Button btn;
    private ConcretePetDAO petRepo;
    private Map<String, String> listClient;


    //servono per il campo ricerca
    private GridPane container;
    private  HBox searchBox;
    private TextField searchText;
    private VBox dropDownMenu;

    public RegistrationPetController() {
        this.listClient  = new HashMap<>();
        this.rbM = new RadioButton(Gender.M.getDeclaringClass().descriptorString());
        this.rbF = new RadioButton(Gender.F.getDeclaringClass().descriptorString());

        try{
            ConnectionDBH2 connection = new ConnectionDBH2();
            this.petRepo = new ConcretePetDAO(connection);

        }

        catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       addFieldOwner();
       addFieldRace();
       addButtonSave();
       addActionButton();

    }
    public  void addButtonSave()  {
        this.btn = new Button("Salva");
        this.btn.setId("btn");
        this.pane_main_grid.getChildren().add(this.btn);
    }

    public void addActionButton() {
        this.btn.setOnAction(this::registrationPet);
    }

    public void registrationPet(ActionEvent actionEvent) {
             Pet p = createPet();
            //inserire controlli
            this.petRepo.add(p);


            /*
            System.out.println(p.getName());
            System.out.println(p.getSurname());
            System.out.println(p.getDatebirth());
            System.out.println(p.getSex());
            System.out.println(p.getId_petRace());
            System.out.println(p.getId_owner());
            System.out.println(p.getParticularSign());
           */
    }

    public Pet createPet(){
        RadioButton chk = (RadioButton)this.genderGroup.getSelectedToggle();
        System.out.println(chk.getText());
        String idOwnerSearched = getKeyByValue(listClient,this.searchText.getText());
        System.out.println(idOwnerSearched);

        Pet p = new Pet.Builder<>((String) this.textPetRace.getValue(),
                idOwnerSearched,
                this.textParticularSign.getText())
                .addName(this.textName.getText())
                .addSurname(this.textSurname.getText())
                .addSex((chk.getText().equals("M") ? Gender.M : Gender.F))
                .addDateBirth(this.textdateBirth.getValue())
                .build();

        return p;
    }


    public static <T, E> String getKeyByValue(Map<String, String> map, Object value) {
        return map.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), value))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
                //.collect(Collectors.toSet());
    }


    public void addFieldOwner()  {
        this.listClient = this.petRepo.searchAllClientByFiscalCod(); //ricerca per codice fiscale


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
        //Button search = new Button("Search");


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
                    this.dropDownMenu.getChildren().clear();  //pulisce il drop-menu generale
                });
            }
        }
        return dropDownMenu; // alla fine restituire il VBox (cioè il menu a tendina)
    }

    public void addFieldRace()  {
        List<String> listRace =this.petRepo.searchAllRace();
        this.textPetRace = new ComboBox(FXCollections
                .observableArrayList(listRace));
        this.textPetRace.setId("petRace");
        this.textPetRace.setPromptText("Razza");
        this.pane_main_grid.getChildren().add(this.textPetRace);
    }
}
