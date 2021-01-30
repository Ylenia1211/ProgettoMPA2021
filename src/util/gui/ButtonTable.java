package util.gui;

import controller.*;
import dao.*;
import datasource.ConnectionDBH2;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import model.*;

import javax.swing.*;
import java.io.IOException;
import java.time.LocalDate;

public class ButtonTable{
    public static double MAX_SIZE = 1.7976931348623157E308;
    public static TableColumn<?, ?> addButtonUpdateToTable(String resourceFxml, TableView<?> tableView, int controlView) {
        var colBtn = new TableColumn<>("");

        Callback<TableColumn<Object, Object>, TableCell<Object, Object>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Object, Object> call(final TableColumn<Object, Object> param) {
                final TableCell<Object, Object> cell = new TableCell<>() {

                    //private final Button btn = new Button("Modifica");
                    final ImageView imageView = new ImageView(
                            new Image("./edit.png")
                    );

                    private final  Button btn = new Button("",imageView);
                    {
                        //btn.setAlignment(Pos.CENTER);
                        btn.setMaxWidth(ButtonTable.MAX_SIZE);
                        btn.setTooltip(new Tooltip("Modifica"));
                        imageView.setFitWidth(18);
                        imageView.setFitHeight(18);

                        btn.setOnAction((ActionEvent event) -> {
                            var data = getTableView().getItems().get(getIndex());
                            Scene scene = this.getScene();
                            BorderPane borderPane = (BorderPane) scene.lookup("#borderPane");
                            try { //# in base all'elemento fxml mi prendo il controller giusto
                                switch (resourceFxml) {
                                    case "/view/registrationPet.fxml" -> {
                                        FXMLLoader loader = new FXMLLoader(getClass().getResource(resourceFxml));
                                        loader.setControllerFactory(p -> new UpdatePetController((Pet) data));
                                        borderPane.setCenter(loader.load());
                                    }
                                    case "/view/registrationClient.fxml" -> { //qua è un problema perchè non possiamo generalizzare anche per update registrazione di cliente e segretaria
                                       switch (controlView){
                                           case 0 ->{
                                               // faccio l'update dell dottore
                                               FXMLLoader loader = new FXMLLoader(getClass().getResource(resourceFxml));
                                               loader.setControllerFactory(p -> new UpdateDoctorController((Doctor) data));
                                               borderPane.setCenter(loader.load());
                                           }
                                           case 1 -> {
                                                // faccio l'update della segretaria
                                               FXMLLoader loader = new FXMLLoader(getClass().getResource(resourceFxml));
                                               loader.setControllerFactory(p -> new UpdateSecretariatController((Secretariat) data));
                                               borderPane.setCenter(loader.load());
                                           }
                                           case 2 -> {
                                               // faccio l'update dell' owner
                                               FXMLLoader loader = new FXMLLoader(getClass().getResource(resourceFxml));
                                               loader.setControllerFactory(p -> new UpdateClientController((Owner) data));
                                               borderPane.setCenter(loader.load());
                                           }
                                       }
                                    }
                                    case "/view/bookingAppointment.fxml" ->{
                                        FXMLLoader loader = new FXMLLoader(getClass().getResource(resourceFxml));
                                        loader.setControllerFactory(p -> new UpdateBookingAppointmentController((Appointment) data));
                                        borderPane.setCenter(loader.load());
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                    @Override
                    public void updateItem(Object item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };
        colBtn.setCellFactory(cellFactory);
        return colBtn;
    }

    public static TableColumn<?, ?> addButtonDeleteToTable(TableView<?> tableView, Class object)  {
        var colBtn = new TableColumn<>("");

        Callback<TableColumn<Object, Object>, TableCell<Object, Object>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Object, Object> call(final TableColumn<Object, Object> param) {
                return new TableCell<>() {
                    //private final Button btn = new Button("Cancella");
                    final ImageView imageView = new ImageView(
                            new Image("./delete.png")
                    );

                    private final  Button btn = new Button("",imageView);
                    {
                        btn.setMaxWidth(ButtonTable.MAX_SIZE);
                        imageView.setFitWidth(18);
                        imageView.setFitHeight(18);
                        btn.setTooltip(new Tooltip("Cancella"));

                             btn.setOnAction((ActionEvent event) -> {
                                 var data = getTableView().getItems().get(getIndex());
                                 JPanel pan = new JPanel();
                                 int ok = JOptionPane.showConfirmDialog(
                                    null,
                                    "Sei sicuro di voler cancellare?",
                                    "Cancellazione",
                                    JOptionPane.YES_NO_OPTION);
                            if(ok ==0) { //cancella
                                if(object.equals(Pet.class)){
                                    var petRepo = new ConcretePetDAO(ConnectionDBH2.getInstance());
                                    String id = petRepo.search((Pet) data);
                                    petRepo.delete(id);
                                    tableView.getItems().remove(data); //elimina graficamente
                                }else if (object.equals(Doctor.class)){
                                    var doctorRepo = new ConcreteDoctorDAO(ConnectionDBH2.getInstance());
                                    String id = doctorRepo.search((Doctor) data);
                                    doctorRepo.delete(id);
                                    tableView.getItems().remove(data); //elimina graficamente
                                }
                                else if (object.equals(Secretariat.class)){
                                    var secretariatRepo = new ConcreteSecretariatDAO(ConnectionDBH2.getInstance());
                                    String id = secretariatRepo.search((Secretariat) data);
                                    secretariatRepo.delete(id);
                                    tableView.getItems().remove(data); //elimina graficamente
                                }else if(object.equals((Appointment.class))){
                                    var appointmentRepo = new ConcreteAppointmentDAO(ConnectionDBH2.getInstance());
                                    String id = appointmentRepo.search((Appointment) data);
                                    appointmentRepo.delete(id);
                                    tableView.getItems().remove(data); //elimina graficamente
                                }
                                else if(object.equals((Owner.class))){
                                    var clientRepo = new ConcreteOwnerDAO(ConnectionDBH2.getInstance());
                                    String id = clientRepo.search((Owner) data);
                                    clientRepo.delete(id);
                                    tableView.getItems().remove(data); //elimina graficamente
                                }
                            }
                        });
                    }

                    @Override
                    public void updateItem(Object item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        };
        colBtn.setCellFactory(cellFactory);
        return colBtn;
    }

    public static TableColumn<?, ?> addButtonViewInfoOwnerPet(TableView<?> tableView)  {
        var colBtn = new TableColumn<>("");
        Callback<TableColumn<Object, Object>, TableCell<Object, Object>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Object, Object> call(final TableColumn<Object, Object> param) {
                return new TableCell<>() {
                    private final Button btn = new Button("Dettagli");
                    /*final ImageView imageView = new ImageView(
                            new Image("./details.png")
                    );*/

                    // private final  Button btn = new Button("",imageView);
                    {
                        btn.setMaxWidth(ButtonTable.MAX_SIZE);
                        /*imageView.setFitWidth(18);
                        imageView.setFitHeight(18);
                        btn.setTooltip(new Tooltip("Dettagli"));*/

                        btn.setOnAction((ActionEvent event) -> {
                            var data = getTableView().getItems().get(getIndex());
                            Scene scene = this.getScene();
                            BorderPane borderPane = (BorderPane) scene.lookup("#borderPane");
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/showInfoOwnerPet.fxml"));
                                loader.setControllerFactory(p -> new ShowInfoOwnerPetController((Appointment) data));
                                borderPane.setCenter(loader.load());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }

                    @Override
                    public void updateItem(Object item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };

            }
        };
        colBtn.setCellFactory(cellFactory);
        return colBtn;
    }

    public static TableColumn<?, ?> addButtonCreateReport(TableView<?> tableView)  {
        var colBtn = new TableColumn<>("");
        Callback<TableColumn<Object, Object>, TableCell<Object, Object>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Object, Object> call(final TableColumn<Object, Object> param) {
                return new TableCell<Object, Object>() {
                    private final Button btn = new Button("Crea Report");
                    /*final ImageView imageView = new ImageView(
                            new Image("./create_report.png")
                    );*/

                   // private final  Button btn = new Button("",imageView);
                    {
                        btn.setMaxWidth(ButtonTable.MAX_SIZE);
                       /* imageView.setFitWidth(18);
                        imageView.setFitHeight(18);
                        btn.setTooltip(new Tooltip("Cre Report"));*/
                        btn.setOnAction((ActionEvent event) -> {
                            var data = getTableView().getItems().get(getIndex());
                            Scene scene = this.getScene();
                            BorderPane borderPane = (BorderPane) scene.lookup("#borderPane");
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/createReport.fxml"));
                                loader.setControllerFactory(p -> new CreateReportController((Appointment) data, false));
                                borderPane.setCenter(loader.load());

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }

                    @Override
                    public void updateItem(Object item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            var ap = getTableColumn().getTableView().getItems().get(getIndex());
                            var appointmentRepo = new ConcreteAppointmentDAO(ConnectionDBH2.getInstance());
                            String id_appointment = appointmentRepo.search((Appointment)ap);
                            if (appointmentRepo.searchIfExistAppointmentInReport(id_appointment)) {
                                setGraphic(null);
                            }
                            //posso creare il report solo se la data della visita è precedente alla data di oggi  // data di oggi +1
                            else if (((Appointment)getTableColumn().getTableView().getItems().get(getIndex())).getLocalDate().isBefore(LocalDate.now().plusDays(1))) {
                                setGraphic(btn);
                            } else {
                                setGraphic(null);
                            }
                        }
                    }
                };
            }
        };
        colBtn.setCellFactory(cellFactory);
        return colBtn;
    }

    public static  TableColumn<?, ?>  addButtonViewReport(TableView<?> tableView) {
        var colBtn = new TableColumn<>("");
        Callback<TableColumn<Object, Object>, TableCell<Object, Object>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Object, Object> call(final TableColumn<Object, Object> param) {
                return new TableCell<Object, Object>() {
                    private final Button btn = new Button("Report");
                  /* final ImageView imageView = new ImageView(
                           new Image("./report.png")
                   );*/

                    //private final  Button btn = new Button("",imageView);
                    {
                        btn.setMaxWidth(ButtonTable.MAX_SIZE);
                       /* imageView.setFitWidth(18);
                        imageView.setFitHeight(18);
                        btn.setTooltip(new Tooltip("Report"));*/

                        btn.setOnAction((ActionEvent event) -> {
                            var data = getTableView().getItems().get(getIndex());
                            Scene scene = this.getScene();
                            BorderPane borderPane = (BorderPane) scene.lookup("#borderPane");
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/createReport.fxml"));
                                loader.setControllerFactory(p -> new CreateReportController((Appointment) data, true));
                                borderPane.setCenter(loader.load());

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }

                    @Override
                    public void updateItem(Object item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty) {
                            setGraphic(null);
                        } else {
                            var ap = getTableColumn().getTableView().getItems().get(getIndex());
                            var appointmentRepo = new ConcreteAppointmentDAO(ConnectionDBH2.getInstance());
                            String id_appointment = appointmentRepo.search((Appointment) ap);
                            if (appointmentRepo.searchIfExistAppointmentInReport(id_appointment)) {
                                setGraphic(btn);

                            } else {
                                setGraphic(null);
                            }
                        }
                    }
                };
            }
        };
        colBtn.setCellFactory(cellFactory);
        return colBtn;
    }
}
