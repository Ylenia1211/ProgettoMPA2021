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
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class DoctorDashboard implements Initializable{
    public VBox sidebar;
    public Button pazienti = new Button("Pazienti");
    public Button agenda = new Button("Agenda");
    public Button utenti = new Button("Utenti");
    public Button prenotazioni = new Button("Prenotazioni");
    public BorderPane borderPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            this.setButtons(sidebar, pazienti, agenda, utenti, prenotazioni);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BorderPane getBorderPane() {
        return borderPane;
    }

    public void setButtons (VBox vBox, Button... buttons) throws IOException {
        for (Button button: buttons) {
            DropShadow shadow = new DropShadow();
            Lighting lighting = new Lighting();
            TabPane tabPane = new TabPane();

            //Adding the shadow when the mouse cursor is on
            button.addEventHandler(MouseEvent.MOUSE_ENTERED,
                    e -> button.setEffect(shadow));
            //Removing the shadow when the mouse cursor is off
            button.addEventHandler(MouseEvent.MOUSE_EXITED,
                    e -> button.setEffect(null));

            button.addEventHandler(MouseEvent.MOUSE_CLICKED,
                    e -> button.setEffect(lighting));

            button.addEventHandler(MouseEvent.MOUSE_PRESSED,
                    e -> button.setEffect(lighting));

            button.setStyle("-fx-background-color: #3DA4E3; -fx-background-radius: 30px, 30px, 30px, 30px;"); //ffffff00 transparent
            button.setFont(Font.font("Bauhaus 93", 20.0));
            button.setMnemonicParsing(false);
            button.setPrefWidth(134.0);
            button.setPrefHeight(25.0);
            button.setTextFill(Paint.valueOf("WHITE"));


                button.setOnMouseClicked(e -> {
                    Parent root;

                    try {
                        if (button.getText().equals("Pazienti")) {
                            Tab pazienti = new Tab("Clienti", FXMLLoader.load(getClass().getResource("/view/showOwner.fxml")));
                            Tab nuovoClient= new Tab("Nuovo Cliente", FXMLLoader.load(getClass().getResource("/view/registrationClient.fxml")));
                            Tab nuovoPaziente= new Tab("Nuovo Paziente", FXMLLoader.load(getClass().getResource("/view/registrationPet.fxml")));
                            tabPane.getTabs().clear();
                            tabPane.getTabs().add(pazienti);
                            tabPane.getTabs().add(nuovoClient);
                            tabPane.getTabs().add(nuovoPaziente);
                            borderPane.setCenter(tabPane);
                        }else
                            if (button.getText().equals("Utenti")) {
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/registrationClient.fxml"));
                                loader.setControllerFactory(new Callback<Class<?>, Object>() {
                                    public Object call(Class<?> p) {
                                        return  new RegistrationDoctorController();
                                    }
                                });
                                Tab dottore = new Tab("Nuovo Dottore", loader.load());
                                tabPane.getTabs().clear();
                                tabPane.getTabs().add(dottore);
                                // Tab nuovaSegreteria = new Tab("Nuova Segreteria", FXMLLoader.load(getClass().getResource("")));
                                // tabPane.getTabs().clear();
                                //tabPane.getTabs().add(nuovoPaziente);
                                borderPane.setCenter(tabPane);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            };

                        }
                            else {
                            root = FXMLLoader.load(getClass().getResource("/view/" + button.getText().toLowerCase(Locale.ROOT) + ".fxml"));
                            borderPane.setCenter(root);
                        }
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
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
