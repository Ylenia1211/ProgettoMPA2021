package controller;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.effect.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import static javafx.scene.input.KeyCode.R;

public class DoctorDashboard implements Initializable{
    public VBox sidebar;
    public Button pazienti = new Button("Pazienti");
    public Button agenda = new Button("Agenda");
    public BorderPane borderPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.setButtons(sidebar, pazienti, agenda);
    }

    public void setButtons (VBox vBox, Button... buttons) {
        for (Button button: buttons) {
            DropShadow shadow = new DropShadow();
            Blend lighting = new Blend();

            //Adding the shadow when the mouse cursor is on
            button.addEventHandler(MouseEvent.MOUSE_ENTERED,
                    e -> button.setEffect(shadow));
            //Removing the shadow when the mouse cursor is off
            button.addEventHandler(MouseEvent.MOUSE_EXITED,
                    e -> button.setEffect(null));

            button.addEventHandler(MouseEvent.MOUSE_CLICKED,
                    e -> button.setEffect(lighting));

            button.setStyle("-fx-background-color: #3DA4E3; -fx-background-radius: 30px, 30px, 30px, 30px;"); //ffffff00 transparent
            button.setFont(Font.font("Bauhaus 93", 20.0));
            button.setMnemonicParsing(false);
            button.setPrefWidth(134.0);
            button.setPrefHeight(25.0);
            button.setTextFill(Paint.valueOf("WHITE"));

            // Qua voglio fare che se il bottone da creare è "pazienti" allora mi crea un tabpane con 2 tab,
            // una con la lista dei clienti e una dove inserirne uno nuovo

//            if (button.getText().equals("Pazienti")) {
//                TabPane tabPane = new TabPane();
//                Tab pazienti = new Tab("Pazienti");
//                pazienti.
//                Tab nuovoPaziente = new Tab("Nuovo Paziente");
//                tabPane.getTabs().add(pazienti);
//                tabPane.getTabs().add(nuovoPaziente);
//                borderPane.setCenter();
//            }
            button.setOnMouseClicked(e -> {
                Parent root = null;
                try {
                    root = FXMLLoader.load(getClass().getResource("/view/" + button.getText().toLowerCase(Locale.ROOT) + ".fxml"));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

                borderPane.setCenter(root);
            });

            button.setId(button.getText().toLowerCase(Locale.ROOT));
            vBox.getChildren().add(button);
        }
        vBox.setSpacing(3.0);
    }

    public void close() {
        Stage stage = (Stage) borderPane.getScene().getWindow();
        stage.close();
    }
}