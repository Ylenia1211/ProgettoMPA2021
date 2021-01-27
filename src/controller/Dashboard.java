package controller;

import controller.factorySidebar.SideBarAction;
import controller.factorySidebar.SideBarFactory;
import dao.ConcreteAdminDAO;
import dao.ConcreteAppointmentDAO;
import datasource.ConnectionDBH2;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Lighting;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Appointment;
import model.User;
import util.Common;
import util.SessionUser;
import util.email.ConcreteObserver;
import util.email.Observer;
import util.email.Subject;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;


public class Dashboard implements Initializable, Common, Subject {
    public BorderPane borderPane;
    public Label labelWelcome;
    public VBox sidebar;
    private String roleUserLogged;
    private List<Observer> observers;

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

    public void setUserLogged(User user) {
        this.roleUserLogged = user.getRole();
        this.labelWelcome.setText("  Benvenuto " + this.roleUserLogged + "!");
    }

    public BorderPane getBorderPane() {
        return borderPane;
    }

    public void setButtons(VBox vBox, List<Button> buttons) throws IOException {
        DropShadow shadow = new DropShadow();
        Lighting lighting = new Lighting();
        TabPane tabPane = new TabPane();

        for (Button button : buttons) {
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
            if (button.getText().equals("Logout")) {
                button.setStyle("-fx-background-color: #fb4a4a; -fx-background-radius: 30px, 30px, 30px, 30px;");

            }
            button.setOnMouseClicked(e -> {
                Parent root;
                try {
                    switch (button.getText()) {
                        case "Aggiungi" -> {  //segretaria
                            if (SessionUser.getUserLogged().getRole().equals("Segreteria")) {
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
                            } else {
                                FXMLLoader loaderDoctor = new FXMLLoader(getClass().getResource("/view/registrationClient.fxml"));
                                loaderDoctor.setControllerFactory(p -> new RegistrationDoctorController());
                                Tab nuovoDottore = new Tab("Nuovo Dottore", loaderDoctor.load());
                                FXMLLoader loaderSecretariat = new FXMLLoader(getClass().getResource("/view/registrationClient.fxml"));
                                loaderSecretariat.setControllerFactory(p -> new RegistrationSecretariatController());
                                Tab nuovaSegreteria = new Tab("Nuovo Segreteria", loaderSecretariat.load());
                                tabPane.getTabs().clear();
                                tabPane.getTabs().add(nuovoDottore);
                                tabPane.getTabs().add(nuovaSegreteria);
                                tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
                                borderPane.setCenter(tabPane);

                            }
                        }
                        case "Clienti" -> {  //segretaria
                            Tab clienti = new Tab("Clienti", FXMLLoader.load(getClass().getResource("/view/showTableOwner.fxml")));
                            tabPane.getTabs().clear();
                            tabPane.getTabs().add(clienti);
                            tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
                            borderPane.setCenter(tabPane);
                        }
                        case "Pazienti" -> {  //segretaria
                            FXMLLoader loaderPet = new FXMLLoader(getClass().getResource("/view/showTablePet.fxml"));
                            loaderPet.setControllerFactory(p -> new ShowTableAllPetController());
                            Tab pazienti = new Tab("Pazienti", loaderPet.load());
                            tabPane.getTabs().clear();
                            tabPane.getTabs().add(pazienti);
                            tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
                            borderPane.setCenter(tabPane);
                        }
                        case "Dottori" -> { //admin
                            try {
                                Tab dottori = new Tab("Dottori", FXMLLoader.load(getClass().getResource("/view/showTableDoctor.fxml")));
                                tabPane.getTabs().clear();
                                tabPane.getTabs().add(dottori);
                                tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
                                borderPane.setCenter(tabPane);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }

                        }
                        case "Segreteria" -> { //admin
                            try {
                                Tab segreteria = new Tab("Segreteria", FXMLLoader.load(getClass().getResource("/view/showTableSecretariat.fxml")));
                                tabPane.getTabs().clear();
                                tabPane.getTabs().add(segreteria);
                                tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
                                borderPane.setCenter(tabPane);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                        case "Prenotazioni" -> { //segretaria
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/searchBookingByPet.fxml"));
                                Tab bookingVisitsFuture = new Tab("Prenotazioni Visite (ancora da effettuare)", loader.load());
                                tabPane.getTabs().clear();
                                tabPane.getTabs().add(bookingVisitsFuture);
                                tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
                                borderPane.setCenter(tabPane);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                        case "Clinica" -> { //admin
                            TextField oldEmailClinic = new TextField();
                            PasswordField oldPassword = new PasswordField();
                            ConcreteAdminDAO adminRepo = new ConcreteAdminDAO(ConnectionDBH2.getInstance());
                            String emailDB = adminRepo.searchEmailClinic();
                            String pswEmailDB = adminRepo.searchPasswordClinic();
                            Dialog<?> dialog = Common.createDialogText(new Label("Inserisci Vecchia Email della Clinica"), oldEmailClinic, oldPassword);
                            var result = dialog.showAndWait();
                            if (result.isPresent()) {
                                if (result.get() == dialog.getDialogPane().getButtonTypes().get(0)) {
                                    if (oldEmailClinic.getText().equals(emailDB) && oldPassword.getText().equals(pswEmailDB)) {
                                        TextField newEmailClinic = new TextField();
                                        PasswordField newPassword = new PasswordField();
                                        Dialog<?> dialog2 = Common.createDialogText(new Label("Inserisci Nuova Email della Clinica"), newEmailClinic,newPassword);
                                        var result2 = dialog2.showAndWait();
                                        if (result2.get() == dialog2.getDialogPane().getButtonTypes().get(0)) {
                                            //cambia email in db
                                            adminRepo.updateEmailClinic(newEmailClinic.getText());
                                            //cambia psw email in db
                                            adminRepo.updatePasswordClinic(newPassword.getText());
                                        }
                                    } else {
                                        JOptionPane.showMessageDialog(null, "Email e/o password non compatibile nel DB! Riprova!");
                                    }
                                }
                            }
                        }
                        case "Report" -> { //dottore
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/searchReportbyPet.fxml"));
                                loader.setControllerFactory(p -> new SearchReportController());
                                Tab bookingVisits = new Tab("Tutte le visite passate", loader.load());
                                tabPane.getTabs().clear();
                                tabPane.getTabs().add(bookingVisits);
                                tabPane.getTabs().forEach(x -> x.setStyle("-fx-color:  #3DA4E3; -fx-text-base-color: #163754;"));
                                tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
                                borderPane.setCenter(tabPane);

                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                        case "Notifica" -> { // manda notifica email se cliccato a tutti gli utenti con prenotaizone il giorno dopo
                            this.observers  = new ArrayList<>();
                            ConcreteAppointmentDAO bookingDao = new ConcreteAppointmentDAO(ConnectionDBH2.getInstance());
                            List<Appointment> ownersBookingTomorrow = bookingDao.searchAppointmentsByDate((LocalDate.now().plusDays(1)).toString());
                            //List<String> emailsOwners =new ArrayList<>();
                            if(!ownersBookingTomorrow.isEmpty()) {
                                ownersBookingTomorrow.forEach(booking -> {
                                    // emailsOwners.add(bookingDao.searchOwnerById(booking.getId_owner()).getEmail()); //test
                                    ConcreteObserver observerChanges = new ConcreteObserver.Builder()
                                            .setEmailOwner(bookingDao.searchOwnerById(booking.getId_owner()).getEmail()) //passare email owner associatato alla prenotazione
                                            .setDataVisit(booking.getLocalDate())
                                            .setTimeStartVisit(booking.getLocalTimeStart())
                                            .setTimeEndVisit(booking.getLocalTimeEnd())
                                            .build();
                                    this.register(observerChanges);
                                });
                                //emailsOwners.forEach(System.out::println);
                                this.notifyObservers();
                                JOptionPane.showMessageDialog(null, "Notifiche delle prenotazioni di domani mandate correttamente ai Clienti!");
                            }else
                                JOptionPane.showMessageDialog(null, "Nessuna prenotazione prevista per domani!");
                        }
                        case "Profilo" -> { //il profilo
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/personalProfile.fxml"));
                                loader.setControllerFactory(p -> new PersonalProfileController(this.roleUserLogged));
                                borderPane.setCenter(loader.load());
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }

                        }
                        case "Logout" -> {
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
                            } catch (IOException ex) {
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
    @Override
    public void register(Observer o) {
        observers.add(o);
    }

    @Override
    public void unregister(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for(Observer obs: observers){
            obs.update();
        }
    }

}
