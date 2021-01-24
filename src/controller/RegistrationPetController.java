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
import model.Secretariat;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.time.LocalDate;
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
        this.petRepo = new ConcretePetDAO(ConnectionDBH2.getInstance());
    }

    public ConcretePetDAO getPetRepo() {
        return petRepo;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.textdateBirth.setValue(LocalDate.now()); //non fa modificare all'utente il textfield  per evitare di mettere valori non consentiti
        this.textdateBirth.setEditable(false);
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
        // controlli
        if (!this.textName.getText().trim().isEmpty() &&
                !this.textSurname.getText().trim().isEmpty() &&
                (this.rbM.isSelected() || rbF.isSelected()) &&
                !this.textdateBirth.getValue().toString().isEmpty() &&
                !this.textPetRace.getValue().toString().isEmpty() &&
                !this.searchText.getText().trim().isEmpty() &&
                !this.textParticularSign.getText().trim().isEmpty()
        ) {
            Pet p = createPet();
            if (this.petRepo.isNotDuplicate(p)) {
                try {
                    this.petRepo.add(p);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            } else {
                JOptionPane.showMessageDialog(null, "Impossibile creare Animale! Già esistente!");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Per completare la registrazione devi completare TUTTI i campi!");
        }
    }

    public Pet createPet(){
        RadioButton chk = (RadioButton)this.genderGroup.getSelectedToggle();
        System.out.println(chk.getText());
        String idOwnerSearched = getKeyByValue(listClient,this.searchText.getText());
        System.out.println(idOwnerSearched);
        return new Pet.Builder<>()
                .addName(this.textName.getText())
                .addSurname(this.textSurname.getText())
                .addSex((chk.getText().equals("M") ? Gender.M : Gender.F))
                .addDateBirth(this.textdateBirth.getValue())
                .setId_petRace((String) this.textPetRace.getValue())
                .setId_owner(idOwnerSearched)
                .setParticularSign( this.textParticularSign.getText())
                .build();
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


    public VBox getPane_main_grid() {
        return pane_main_grid;
    }

    public Label getLabelTitle() {
        return labelTitle;
    }

    public TextField getTextName() {
        return textName;
    }

    public TextField getTextSurname() {
        return textSurname;
    }

    public HBox getGender() {
        return gender;
    }

    public RadioButton getRbM() {
        return rbM;
    }

    public ToggleGroup getGenderGroup() {
        return genderGroup;
    }

    public RadioButton getRbF() {
        return rbF;
    }

    public DatePicker getTextdateBirth() {
        return textdateBirth;
    }

    public ComboBox getTextPetRace() {
        return textPetRace;
    }

    public TextField getTextParticularSign() {
        return textParticularSign;
    }

    public Button getBtn() {
        return btn;
    }

    public Map<String, String> getListClient() {
        return listClient;
    }

    public GridPane getContainer() {
        return container;
    }

    public HBox getSearchBox() {
        return searchBox;
    }

    public TextField getSearchText() {
        return searchText;
    }

    public VBox getDropDownMenu() {
        return dropDownMenu;
    }
}
