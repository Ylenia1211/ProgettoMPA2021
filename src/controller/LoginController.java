package controller;

import dao.ConcreteLoginDAO;
import datasource.ConnectionDBH2;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.User;
import util.SessionUser;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Implementando i metodi di 'Inizializable' {@link Initializable} inizializza la view associata al controller.
 * Il controller dell'interfaccia di login, setta i valori nella {@link LoginController#textRoleUser} e controlla gli
 * accessi degli utenti
 */
public class LoginController implements Initializable {
    public BorderPane borderPane;
    public Button btnLogin;
    public PasswordField textPassword;
    public TextField textUsername;
    public Pane pane;
    @FXML
    private ComboBox<String> textRoleUser;
    private final ConcreteLoginDAO loginRepo;
    private User userLogged;

    /**
     * Inizializza il campo {@link LoginController#loginRepo} creando un nuovo oggetto {@link ConcreteLoginDAO} richimando la Connessione singleton {@link ConnectionDBH2} del database.
     */
    public LoginController() {
        this.loginRepo = new ConcreteLoginDAO(ConnectionDBH2.getInstance());
    }

    /**
     *  {@inheritDoc}
     * Inizializza i campi della view in modo appropriato e
     * setta i campi del {@link LoginController#textRoleUser} con le tipologie di accesso utente
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> roles = FXCollections.observableArrayList("Dottore", "Segreteria", "Amministratore");
        this.textRoleUser = new ComboBox<>(roles);
        this.textRoleUser.setLayoutX(194.0);
        this.textRoleUser.setLayoutY(248.0);
        this.textRoleUser.setPrefHeight(25.0);
        this.textRoleUser.setPrefWidth(228.0);
        this.textRoleUser.setPromptText("Scegli Ruolo");
        this.pane.getChildren().add(this.textRoleUser);
    }

    /**
     * Permette la chiusura dell'interfaccia utente
     */
    public void close() {
        Stage stage = (Stage) borderPane.getScene().getWindow();
        stage.close();
    }

    /**
     * La funzione associata al bottone di login {@link LoginController#btnLogin}, verifica il corretto accesso
     * dell'utente
     *
     * @param actionEvent L'evento rilevato (es. click del mouse)
     */
    public void loginAction(ActionEvent actionEvent)  {
        if (textRoleUser.getValue() != null) { //se è stato settato il valore della combobox
            this.userLogged = createUser();
            System.out.println(userLogged.toString());
            //ricerca del username e password in base al ruolo
            boolean result = this.loginRepo.searchUser(userLogged);

            if (result) {
                btnLogin.getScene().getWindow().hide();
                SessionUser.login(this.userLogged);
                Stage home = new Stage();
                AtomicReference<Double> x = new AtomicReference<>((double) 0);
                AtomicReference<Double> y = new AtomicReference<>((double) 0);
                try {
                    //cambia schermata --> dashboard
                    Parent root = FXMLLoader.load(getClass().getResource("/view/dashboard.fxml"));
                    Scene scene = new Scene(root);
                    root.setOnMousePressed(mouseEvent -> {
                        x.set(mouseEvent.getSceneX());
                        y.set(mouseEvent.getSceneY());
                    });
                    root.setOnMouseDragged(mouseEvent -> {
                        home.setX(mouseEvent.getScreenX() - x.get());
                        home.setY(mouseEvent.getScreenY() - y.get());
                    });

                    home.setScene(scene);
                    home.initStyle(StageStyle.TRANSPARENT); //per nascondere la barra in alto
                    home.setResizable(false);
                    home.show();
                }catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setTitle("Login errato");
                alert.setContentText("Username e/o password sono errate! Riprova.");
                alert.showAndWait();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setTitle("Attenzione! Nessun ruolo assegnato.");
            alert.setContentText("Nessun ruolo assegnato. Inserisci il tuo ruolo!");
            alert.showAndWait();
        }
    }

    /**
     * Crea un nuovo oggetto di tipo {@link User} usando i valori inseriti nei campi {@link LoginController#textUsername}
     * e {@link LoginController#textPassword} e i valori nella {@link LoginController#textRoleUser}
     *
     * @return Un oggetto di tipo {@link User}
     */
    public User createUser(){
        return new User.Builder()
                .setUsername(textUsername.getText())
                .setPassword(textPassword.getText())
                .setRole(textRoleUser.getValue())
                .build();
    }
}
