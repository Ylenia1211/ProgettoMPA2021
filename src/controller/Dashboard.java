package controller;
import com.sun.tools.javac.Main;
import controller.factorySidebar.SideBarAction;
import controller.factorySidebar.SideBarFactory;
import dao.ConcreteDoctorDAO;
import datasource.ConnectionDBH2;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Lighting;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.User;
import util.SessionUser;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;


public class Dashboard  implements Initializable {
    public BorderPane borderPane;
    public Label labelWelcome;

    public VBox sidebar;
    public Button pazienti = new Button("Pazienti");
    public Button agenda = new Button("Agenda");
    public Button utenti = new Button("Utenti");
    public Button prenotazioni = new Button("Prenotazioni");
    public Button profilo = new Button("Profilo");
    private String roleUserLogged;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUserLogged(SessionUser.getUserLogged());
       //costruzione delle azioni della sideBar in base al Ruolo dell'utente loggato,  utilizzando il Pattern Factory
        SideBarAction sideBarByRole = SideBarFactory.createSideBar(this.roleUserLogged);  //mi serve il ruolo dell'utente loggato
        try {
            this.setButtons(sidebar, sideBarByRole.getSpecificAction()); //costruzione specifica delle azioni dell'user loggato

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        Stage stage = (Stage) borderPane.getScene().getWindow();
        stage.close();
    }

    public void setUserLogged(User user){
        this.roleUserLogged = user.getRole();
        this.labelWelcome.setText("Benvenuto "+ this.roleUserLogged + "!");
    }

    public BorderPane getBorderPane() {
        return borderPane;
    }

    public void setButtons (VBox vBox, List<Button> buttons) throws IOException {
        DropShadow shadow = new DropShadow();
        Lighting lighting = new Lighting();
        TabPane tabPane = new TabPane();

        for (Button button: buttons) {
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
            button.setFont(Font.font("Arial", 20.0));
            button.setMnemonicParsing(false);
            button.setPrefWidth(150.0);
            button.setPrefHeight(25.0);
            button.setTextFill(Paint.valueOf("WHITE"));
            if (button.getText().equals("Logout")){
                button.setStyle("-fx-background-color: #fb4a4a; -fx-background-radius: 30px, 30px, 30px, 30px;");

            }
            button.setOnMouseClicked(e -> {
                Parent root;
                try {
                    switch (button.getText()) {
                        case "Pazienti" -> {  //segretaria
                            Tab pazienti = new Tab("Clienti", FXMLLoader.load(getClass().getResource("/view/showTableOwner.fxml")));
                            Tab nuovoClient = new Tab("Nuovo Cliente", FXMLLoader.load(getClass().getResource("/view/registrationClient.fxml")));
                            Tab nuovoPaziente = new Tab("Nuovo Paziente", FXMLLoader.load(getClass().getResource("/view/registrationPet.fxml")));
                            tabPane.getTabs().clear();
                            tabPane.getTabs().add(pazienti);
                            tabPane.getTabs().add(nuovoClient);
                            tabPane.getTabs().add(nuovoPaziente);
                            tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
                            borderPane.setCenter(tabPane);
                        }
                        case "Utenti" -> { //admin
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/registrationClient.fxml"));
                                loader.setControllerFactory(p -> new RegistrationDoctorController());
                                Tab dottore = new Tab("Nuovo Dottore", loader.load());
                                tabPane.getTabs().clear();
                                tabPane.getTabs().add(dottore);
                                // Tab nuovaSegreteria = new Tab("Nuova Segreteria", FXMLLoader.load(getClass().getResource("")));
                                // tabPane.getTabs().clear();
                                //tabPane.getTabs().add(nuovoPaziente);
                                borderPane.setCenter(tabPane);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }

                        }
                        case "Prenotazioni" -> { //segretaria
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/bookingAppointment.fxml"));
                                loader.setControllerFactory(p -> new BookingAppointmentController());
                                Tab bookingVisits = new Tab("Inserisci Prenotazione Visita", loader.load());
                                tabPane.getTabs().clear();
                                tabPane.getTabs().add(bookingVisits);
                                borderPane.setCenter(tabPane);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                        case "Profilo" -> { // Aggiunto solo per testare il profilo
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/personalProfile.fxml"));
                                loader.setControllerFactory(p -> new PersonalProfileController(this.roleUserLogged));
                                borderPane.setCenter(loader.load());
                                /*
                                var profile = FXMLLoader.load(getClass().getResource("/view/personalProfile.fxml"));
                                borderPane.setCenter((Node) profile);*/
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }

                        }
                        case "Logout" ->{
                            Stage home = new Stage();
                            try {
                                //cambia schermata --> login
                                close();
                                Parent rootLogin = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
                                SessionUser.logout();
                                Scene scene = new Scene(rootLogin);
                                home.setScene(scene);
                                home.initStyle(StageStyle.TRANSPARENT); //per nascondere la barra in alto
                                home.setResizable(false);
                                home.show();
                            }catch (IOException ex) {
                                ex.printStackTrace();
                            }
                       }
                        default -> {
                            root = FXMLLoader.load(getClass().getResource("/view/" + button.getText().toLowerCase(Locale.ROOT) + ".fxml"));
                            borderPane.setCenter(root);
                        }
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

}
