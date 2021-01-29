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
import util.FieldVerifier;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class RegistrationPetController implements Initializable, FieldVerifier {
    public VBox pane_main_grid;
    public Label labelTitle;
    public TextField textName;
    public TextField textSurname;
    public HBox gender;
    public RadioButton rbM;
    public ToggleGroup genderGroup;
    public RadioButton rbF;
    public DatePicker textdateBirth;
    public ComboBox<String> textPetRace;
    //public ComboBox textOwner;
    public TextField textParticularSign;
    public Button btn;
    private ConcretePetDAO petRepo;
    private Map<String, String> listClient;
    public double MAX_SIZE = 1.7976931348623157E308;
    //servono per il campo ricerca
    private GridPane container;
    private HBox searchBox;
    private TextField searchText;
    private VBox dropDownMenu;
    private List<TextField> fieldsTextPet;
    private List<ComboBox<?>> fieldsComboBox;

    public RegistrationPetController() {
        this.listClient = new HashMap<>();
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
        this.rbM.setSelected(true); //default
        addFieldOwner();
        addFieldRace();
        addButtonSave();
        addActionButton();
        this.fieldsTextPet = List.of(this.textName,
                this.textSurname,
                this.textParticularSign,
                this.searchText);
        this.fieldsComboBox = List.of(this.textPetRace);
    }

    public void addButtonSave() {
        this.btn = new Button("Salva");
        this.btn.setId("btn");
        this.btn.setMaxWidth(MAX_SIZE); //MAX_SIZE
        this.btn.setPrefWidth(Region.USE_COMPUTED_SIZE);
        this.btn.setStyle("-fx-background-color: #3DA4E3;-fx-text-fill: white;" +
                " -fx-border-color: transparent; -fx-font-size: 14px; ");
        this.pane_main_grid.getChildren().add(this.btn);
    }


    public void addActionButton() {
        this.btn.setOnAction(this::registrationPet);
    }

    public void registrationPet(ActionEvent actionEvent) {
        //String text = this.searchText.getText();
        //boolean b =   checkSearchFieldIsCorrect(this.listClient.values(), this.searchText.getText());
        if(
                checkSearchFieldIsCorrect(this.listClient.values(), this.searchText.getText()) &&
                        !checkEmptyTextField(this.fieldsTextPet.stream())
                        && !checkEmptyComboBox(this.fieldsComboBox.stream())
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

    public Pet createPet() {
        RadioButton chk = (RadioButton) this.genderGroup.getSelectedToggle();
        System.out.println(chk.getText());
        String idOwnerSearched = getKeyByValue(listClient, this.searchText.getText());
        System.out.println(idOwnerSearched);
        return new Pet.Builder<>()
                .addName(this.textName.getText().toUpperCase())
                .addSurname(this.textSurname.getText().toUpperCase())
                .addSex((chk.getText().equals("M") ? Gender.M : Gender.F))
                .addDateBirth(this.textdateBirth.getValue())
                .setId_petRace((String) this.textPetRace.getValue())
                .setId_owner(idOwnerSearched)
                .setParticularSign(this.textParticularSign.getText().toUpperCase())
                .build();
    }

    public List<TextField> getFieldsTextPet() {
        return fieldsTextPet;
    }

    public List<ComboBox<?>> getFieldsComboBox() {
        return fieldsComboBox;
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


    public void addFieldOwner() {
        this.listClient = this.petRepo.searchAllClientByFiscalCod(); //ricerca per codice fiscale
        this.container = new GridPane();
        this.searchBox = new HBox();
        this.searchText = new TextField("");

        //this.container.setGridLinesVisible(true);
        this.searchText.setPrefWidth(800);
        this.container.setAlignment(Pos.CENTER);
        this.searchText.setStyle("-fx-border-color:#3da4e3; -fx-prompt-text-fill:#163754");
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
        clean.setPrefWidth(100);
        clean.setStyle("-fx-text-fill: white; -fx-background-color: #3da4e3");
        clean.setOnMouseClicked((e) -> {
            this.searchText.clear();
            if (this.dropDownMenu != null) { // necessario senno NUllPointer exception
                this.dropDownMenu.getChildren().clear();
            }
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
                label.setOnMouseEntered((e) -> label.setBackground(new Background(new BackgroundFill(Color.YELLOW, null, null))));
                label.setOnMouseExited((e) -> label.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, null, null))));
                label.setOnMouseClicked((e) -> {
                    this.searchText.setText(label.getText());
                    this.dropDownMenu.getChildren().clear();  //pulisce il drop-menu generale
                });
            }
        }
        return dropDownMenu; // alla fine restituire il VBox (cioè il menu a tendina)
    }

    public void addFieldRace() {
        List<String> listRace = this.petRepo.searchAllRace();
        this.textPetRace = new ComboBox<>(FXCollections
                .observableArrayList(listRace));
        this.textPetRace.setId("petRace");
        this.textPetRace.setPromptText("Razza");
        this.textPetRace.setMaxWidth(MAX_SIZE);
        this.textPetRace.setPrefWidth(Region.USE_COMPUTED_SIZE);
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


    public DatePicker getTextdateBirth() {
        return textdateBirth;
    }

    public ComboBox<String> getTextPetRace() {
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

    public TextField getSearchText() {
        return searchText;
    }

}
