package controller;

import dao.ConcreteDoctorDAO;
import dao.ConcreteLoginDAO;
import datasource.ConnectionDBH2;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.User;

import javax.swing.*;
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
    public LoginController() {
        try{

            ConnectionDBH2 connection = new ConnectionDBH2();
            this.loginRepo = new ConcreteLoginDAO(connection);
        }
        catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
        }
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

    public void loginAction(ActionEvent actionEvent) {
        User userLogged = createUser();
        //System.out.println(userLogged.toString());
        //TODO: ricerca del username e password in base al ruolo
        boolean b = this.loginRepo.searchUser(userLogged);
    }


    public User createUser(){
        return new User.Builder()
                .setUsername(textUsername.getText())
                .setPassword(textPassword.getText())
                .setRole(textRoleUser.getValue())
                .build();
    }
}
