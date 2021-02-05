package controller;

import dao.ConcretePetDAO;
import datasource.ConnectionDBH2;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import model.Gender;
import model.Pet;
import util.FieldVerifier;

import javax.swing.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Implementando i metodi di 'Inizializable' {@link Initializable} inizializza la view associata al controller.
 * Il controller per la registrazione di un nuovo paziente.
 * Implementa 'FieldVerifier' {@link FieldVerifier} per richiamare i metodi di controllo sui campi della registrazione.
 */
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
    public TextField textParticularSign;
    public Button btn;
    private final ConcretePetDAO petRepo;
    private Map<String, String> listClient;
    public double MAX_SIZE = 1.7976931348623157E308;
    //servono per il campo ricerca
    private GridPane container;
    private HBox searchBox;
    private TextField searchText;
    private VBox dropDownMenu;
    private List<TextField> fieldsTextPet;
    private List<ComboBox<?>> fieldsComboBox;

    /**
     * Il costruttore della classe, inizializza alcuni parametri necessari per la registrazione e crea
     * {@link  RegistrationPetController#petRepo} un oggetto di tipo {@link ConcretePetDAO} richiamando la Connessione singleton {@link ConnectionDBH2} del database.
     */
    public RegistrationPetController() {
        this.listClient = new HashMap<>();
        this.rbM = new RadioButton(Gender.M.getDeclaringClass().descriptorString());
        this.rbF = new RadioButton(Gender.F.getDeclaringClass().descriptorString());
        this.petRepo = new ConcretePetDAO(ConnectionDBH2.getInstance());
    }

    /**
     * Getter dell'attributo {@link RegistrationPetController#petRepo}
     *
     * @return oggetto di tipo {@link ConcretePetDAO}
     */
    public ConcretePetDAO getPetRepo() {
        return petRepo;
    }

    /**
     * Setta i campi della view e vi inserisce nuovi elementi grafici
     * <p>
     * {@inheritDoc}
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.textdateBirth.setValue(LocalDate.now()); //non fa modificare all'utente il textfield  per evitare di mettere valori non consentiti
        this.textdateBirth.setEditable(false);
        this.textdateBirth.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(!(empty || date.compareTo(today) < 0));
            }
        });
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
        this.textPetRace.setBackground(new Background(new BackgroundFill(Color.valueOf("#94D6F1"), CornerRadii.EMPTY, Insets.EMPTY)));
    }

    /**
     * Aggiunge il {@link Button} {@link RegistrationPetController#btn} alla view della registrazione
     * del  pet
     */
    public void addButtonSave() {
        this.btn = new Button("Salva");
        this.btn.setId("btn");
        this.btn.setMaxWidth(MAX_SIZE); //MAX_SIZE
        this.btn.setPrefWidth(Region.USE_COMPUTED_SIZE);
        this.btn.setStyle("-fx-background-color: #3DA4E3;-fx-text-fill: white;" +
                " -fx-border-color: transparent; -fx-font-size: 14px; ");
        this.pane_main_grid.getChildren().add(this.btn);
        VBox.setMargin(btn, new Insets(0, 100, 0, 100));
    }

    /**
     * Assegna al {@link Button} {@link RegistrationPetController#btn} la funzione
     * {@link RegistrationPetController#registrationPet(ActionEvent)}
     */
    public void addActionButton() {
        this.btn.setOnAction(this::registrationPet);
    }

    /**
     * Funzione associata al {@link Button} {@link RegistrationPetController#btn}, registra un nuovo paziente se non
     * esiste già
     *
     * @param actionEvent L'evento registrato, il click sul {@link Button} {@link RegistrationPetController#btn}
     */
    public void registrationPet(ActionEvent actionEvent) {
        if (checkSearchFieldIsCorrect(this.listClient.values(), this.searchText.getText()) &&
                !checkEmptyTextField(this.fieldsTextPet.stream()) &&
                !checkEmptyComboBox(this.fieldsComboBox.stream())) {
            Pet p = createPet();
            if (this.petRepo.isNotDuplicate(p)) {
                try {
                    this.petRepo.add(p);
                    this.refreshTab();
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
   /**
    * metodo per che compie l'azione di refresh delle tab dopo l'inserimento di un nuovo Paziente
    */
    private void refreshTab() {
        try {
            Scene scene = this.btn.getScene();
            BorderPane borderPane = (BorderPane) scene.lookup("#borderPane");
            TabPane tabPane = new TabPane();
            Tab nuovoClient = new Tab("Nuovo Cliente", FXMLLoader.load(getClass().getResource("/view/registrationClient.fxml")));
            Tab nuovoPaziente = new Tab("Nuovo Paziente", FXMLLoader.load(getClass().getResource("/view/registrationPet.fxml")));
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/bookingAppointment.fxml"));
            loader.setControllerFactory(p -> new BookingAppointmentController());
            Tab bookingVisits = new Tab("Inserisci Prenotazione Visita", loader.load());
            tabPane.getTabs().clear();
            tabPane.getTabs().add(nuovoClient);
            tabPane.getTabs().add(nuovoPaziente);
            tabPane.getTabs().add(bookingVisits);
            tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
            borderPane.setCenter(tabPane);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    /**
     * Crea un nuovo oggetto di tipo {@link Pet} con i parametri inseriti nei campi della view
     *
     * @return Un nuovo oggetto di tipo {@link Pet}
     */
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

    /**
     * Getter dell'attributo {@link RegistrationPetController#fieldsTextPet}
     *
     * @return la lista di campi testuali della view {@link RegistrationPetController#fieldsTextPet}
     */
    public List<TextField> getFieldsTextPet() {
        return fieldsTextPet;
    }

    /**
     * Getter dell'attributo {@link RegistrationPetController#fieldsComboBox}
     *
     * @return Il campo {@link RegistrationPetController#fieldsComboBox}
     */
    public List<ComboBox<?>> getFieldsComboBox() {
        return fieldsComboBox;
    }

    /**
     * Metodo che ricerca a partire da una mappa e una chiave. Restituisce la coppia (chiave,valore) grazie alla ricerca sul valore passato a parametro.
     *
     * @param map   mappa <key,value> in cui effettuare la ricerca
     * @param value valore da ricercare nella mappa
     * @return una mappa di un solo elemento (ricercato) di tipo (key, value)
     */
    public static <T, E> String getKeyByValue(Map<String, String> map, Object value) {
        return map.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), value))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
        //.collect(Collectors.toSet());
    }

    /**
     * Aggiunge alla view il componente grafico per la ricerca dei dati relativi al proprietario del paziente
     */
    public void addFieldOwner() {
        this.listClient = this.petRepo.searchAllClientByFiscalCod(); //ricerca per codice fiscale
        this.container = new GridPane();
        this.searchBox = new HBox();
        this.searchText = new TextField("");
        this.searchText.setPrefWidth(800);
        this.container.setAlignment(Pos.CENTER);
        this.searchText.setStyle("-fx-border-color:#3da4e3; -fx-prompt-text-fill:#163754");
        this.searchText.setPromptText("Inserisci Codice Fiscale Cliente");
        //System.out.println(this.container.getAlignment().name());
        // aaggiungere un ascoltatore per ascoltare le modifiche nel campo di testo
        this.searchText.textProperty().addListener((observable, oldValue, newValue) -> {
            if (container.getChildren().size() > 1) { //se gia contiene drop-down menu -> rimuovi
                container.getChildren().remove(1);
            }
            container.add(populateDropDownMenu(newValue, this.listClient.values()), 0, 1); //  quindi aggiungere il menu a tendina popolato alla seconda riga del riquadro della griglia
        });
        final ImageView imageView = new ImageView(
                new Image("resources/delete.png")
        );
        imageView.setFitHeight(18);
        imageView.setFitWidth(18);
        Button clean = new Button("", imageView);
        //clean.setPrefWidth(100);
        clean.setStyle("-fx-text-fill: white; -fx-background-color: #3da4e3");
        clean.setOnMouseClicked((e) -> {
            this.searchText.clear();
            if (this.dropDownMenu != null) { // necessario sennò NUllPointer exception
                this.dropDownMenu.getChildren().clear();
            }
        });
        this.searchBox.getChildren().addAll(this.searchText, clean); //, search);
        //aggiunge il campo di ricerca
        this.container.add(this.searchBox, 0, 0);
        //this.root.getChildren().add(this.container);
        this.pane_main_grid.getChildren().add(this.container); //aggiunge drop-menu a view
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
                    this.dropDownMenu.getChildren().clear();  //pulisce il drop-menu generale
                });
            }
        }
        return dropDownMenu; // alla fine restituire il VBox (cioè il menu a tendina)
    }

    /**
     * Aggiunge alla view il componente grafico relativo alla tipologia di paziente
     */
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

    /**
     * Getter dell'attributo {@link RegistrationPetController#pane_main_grid}
     *
     * @return Il campo {@link RegistrationPetController#pane_main_grid}
     */
    public VBox getPane_main_grid() {
        return pane_main_grid;
    }

    /**
     * Getter dell'attributo {@link RegistrationPetController#labelTitle}
     *
     * @return Il campo {@link RegistrationPetController#labelTitle}
     */
    public Label getLabelTitle() {
        return labelTitle;
    }

    /**
     * Getter dell'attributo {@link RegistrationPetController#textName}
     *
     * @return Il campo {@link RegistrationPetController#textName}
     */
    public TextField getTextName() {
        return textName;
    }

    /**
     * Getter dell'attributo {@link RegistrationPetController#textSurname}
     *
     * @return Il campo {@link RegistrationPetController#textSurname}
     */
    public TextField getTextSurname() {
        return textSurname;
    }

    /**
     * Getter dell'attributo {@link RegistrationPetController#gender}
     *
     * @return Il campo {@link RegistrationPetController#gender}
     */
    public HBox getGender() {
        return gender;
    }

    /**
     * Getter dell'attributo {@link RegistrationPetController#rbM}
     *
     * @return Il campo {@link RegistrationPetController#rbM}
     */
    public RadioButton getRbM() {
        return rbM;
    }

    /**
     * Getter dell'attributo {@link RegistrationPetController#textdateBirth}
     *
     * @return Il campo {@link RegistrationPetController#textdateBirth}
     */
    public DatePicker getTextdateBirth() {
        return textdateBirth;
    }

    /**
     * Getter dell'attributo {@link RegistrationPetController#textPetRace}
     *
     * @return Il campo {@link RegistrationPetController#textPetRace}
     */
    public ComboBox<String> getTextPetRace() {
        return textPetRace;
    }

    /**
     * Getter dell'attributo {@link RegistrationPetController#textParticularSign}
     *
     * @return Il campo {@link RegistrationPetController#textParticularSign}
     */
    public TextField getTextParticularSign() {
        return textParticularSign;
    }

    /**
     * Getter dell'attributo {@link RegistrationPetController#btn}
     *
     * @return Il campo {@link RegistrationPetController#btn}
     */
    public Button getBtn() {
        return btn;
    }

    /**
     * Getter dell'attributo {@link RegistrationPetController#listClient}
     *
     * @return Il campo {@link RegistrationPetController#listClient} ovvero la mappa con la lista dei Clienti memorizzati nel db.
     */
    public Map<String, String> getListClient() {
        return listClient;
    }

    /**
     * Getter dell'attributo {@link RegistrationPetController#container}
     *
     * @return Il campo {@link RegistrationPetController#container}
     */
    public GridPane getContainer() {
        return container;
    }

    /**
     * Getter dell'attributo {@link RegistrationPetController#searchText}
     *
     * @return Il campo {@link RegistrationPetController#searchText}
     */
    public TextField getSearchText() {
        return searchText;
    }
}