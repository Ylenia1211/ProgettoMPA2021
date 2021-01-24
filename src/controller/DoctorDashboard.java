package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.effect.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
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
    private static BorderPane staticBorderPane;
    public VBox sidebar;
    public Button pazienti = new Button("Pazienti");
    public Button agenda = new Button("Agenda");
    public Button utenti = new Button("Utenti");
    public Button prenotazioni = new Button("Prenotazioni");
    public Button profilo = new Button("Profilo");
    public BorderPane borderPane;
    private static final int reportID = 0;
    public Pane borderPanePane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            this.setButtons(sidebar, pazienti, agenda, utenti, prenotazioni, profilo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        staticBorderPane = borderPane;
    }


    public static BorderPane getStaticBorderPane() {
        return staticBorderPane;
    }

    public void setButtons (VBox vBox, Button... buttons) throws IOException {
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
            button.setOnMouseClicked(e -> {
                Parent root = null;
                try {
                    switch (button.getText()) {
                        case "Pazienti" -> {
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
                        case "Utenti" -> {
                            try {
                                FXMLLoader loaderDoctor = new FXMLLoader(getClass().getResource("/view/registrationClient.fxml"));
                                loaderDoctor.setControllerFactory(p -> new RegistrationDoctorController());
                                Tab nuovoDottore = new Tab("Nuovo Dottore", loaderDoctor.load());
                                Tab dottori = new Tab("Dottori", FXMLLoader.load(getClass().getResource("/view/showTableDoctor.fxml")));

                                //segreteria
                                FXMLLoader loaderSecretariat = new FXMLLoader(getClass().getResource("/view/registrationClient.fxml"));
                                loaderSecretariat.setControllerFactory(p -> new RegistrationSecretariatController());
                                Tab nuovaSegreteria = new Tab("Nuovo Segreteria", loaderSecretariat.load());
                                Tab segreteria = new Tab("Segreteria", FXMLLoader.load(getClass().getResource("/view/showTableSecretariat.fxml")));
                                tabPane.getTabs().clear();
                                tabPane.getTabs().add(nuovoDottore);
                                tabPane.getTabs().add(dottori);
                                tabPane.getTabs().add(nuovaSegreteria);
                                tabPane.getTabs().add(segreteria);
                                tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
                                borderPane.setCenter(tabPane);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }

                        }
                        case "Prenotazioni" -> {
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
                                root = FXMLLoader.load(getClass().getResource("/view/personalProfile.fxml"));
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                            borderPane.setCenter(root);
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

    public void close() {
        Stage stage = (Stage) borderPane.getScene().getWindow();
        stage.close();
    }

//    public void createReport() throws IOException {
//        int ID = ++DoctorDashboard.reportID;
//        //Creating PDF document object
//        PDDocument document = new PDDocument();
//        PDPage my_page = new PDPage();
//        PDPageContentStream contentStream = new PDPageContentStream(document, my_page);
//        //Begin the Content stream
//        contentStream.beginText();
//
//        //Setting the font to the Content stream
//        contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
//        contentStream.setLeading(14.5f);
//        //Setting the position for the line
//        contentStream.newLineAtOffset(25, 700);
//
//        String text = "Report numero " + ID;
//
//        //Adding text in the form of string
//        contentStream.showText(text);
//
//        //Ending the content stream
//        contentStream.endText();
//
//        System.out.println("Content added");
//
//        //Closing the content stream
//        contentStream.close();
//        document.addPage(my_page);
//        //Saving the document
//        String currentPath = System.getProperty("user.dir");
//        document.save(currentPath + "/report/report_" + ID + ".pdf");
//
//        System.out.println("PDF created");
//
//        //Closing the document
//        document.close();
//    }
}
