package controller;

import dao.ConcreteDoctorDAO;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.User;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public BorderPane borderPane;
    public Button btnLogin;
    public PasswordField textPassword;
    public TextField textUsername;
    public Pane pane;
    @FXML
    private ComboBox<String> textRoleUser;
    private ConcreteLoginDAO loginRepo;


    private static LoginController instance; // mi serve per prendere l'utente loggato
    private User userLogged;

    public LoginController() {
        instance = this;
        try{

            ConnectionDBH2 connection = new ConnectionDBH2();
            this.loginRepo = new ConcreteLoginDAO(connection);
        }
        catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
        }
    }
    public static LoginController getInstance(){
        return instance;
    }

    public User getUserLogged() {
        return userLogged;
    }

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

    public void close() {
        Stage stage = (Stage) borderPane.getScene().getWindow();
        stage.close();
    }

    public void loginAction(ActionEvent actionEvent)  {
        if (textRoleUser.getValue() != null) { //se è stato settato il valore della combobox
            this.userLogged = createUser();
            System.out.println(userLogged.toString());
            //TODO: ricerca del username e password in base al ruolo
            boolean result = this.loginRepo.searchUser(userLogged);
            if (result) {
                btnLogin.getScene().getWindow().hide();
                Stage home = new Stage();
                try {
                    //cambia schermata --> dashboard
                    Parent root = FXMLLoader.load(getClass().getResource("/view/dashboard.fxml"));
                    Scene scene = new Scene(root);
                    home.setScene(scene);
                    home.initStyle(StageStyle.TRANSPARENT); //per nascondere la barra in alto
                    home.setResizable(false);
                    home.show();
                }catch (IOException e) {
                    e.printStackTrace();
                };

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setTitle("Login errato");
                alert.setContentText("Username e/o password sono errate! Riprova.");
                alert.show();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setTitle("Attenzione! Nessun ruolo assegnato.");
            alert.setContentText("Nessun ruolo assegnato. Inserisci il tuo ruolo!");
            alert.show();
        }
    }


    public User createUser(){
        return new User.Builder()
                .setUsername(textUsername.getText())
                .setPassword(textPassword.getText())
                .setRole(textRoleUser.getValue())
                .build();
    }
}
