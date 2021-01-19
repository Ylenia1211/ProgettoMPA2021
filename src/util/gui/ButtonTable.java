package util.gui;

import controller.UpdatePetController;
import dao.ConcretePetDAO;
import datasource.ConnectionDBH2;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import model.Appointment;
import model.Pet;

import javax.swing.*;
import java.io.IOException;

public class ButtonTable{

    public static TableColumn<?, ?> addButtonUpdateToTable(String resourceFxml, TableView<?> tableView) {
        var colBtn = new TableColumn<>("");
        Callback<TableColumn<Object, Object>, TableCell<Object, Object>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Object, Object> call(final TableColumn<Object, Object> param) {
                final TableCell<Object, Object> cell = new TableCell<>() {
                    private final Button btn = new Button("Modifica");
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            var data = getTableView().getItems().get(getIndex());
                            Scene scene = this.getScene();
                            BorderPane borderPane = (BorderPane) scene.lookup("#borderPane");
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource(resourceFxml));  //#Todo: (aggiungere switch) in base all'elemento fxml mi prendo il controller giusto
                                loader.setControllerFactory(p -> new UpdatePetController((Pet) data));
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
                final TableCell<Object, Object> cell = new TableCell<>() {
                    private final Button btn = new Button("Cancella");
                    {
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
                               }
                                //#Todo: lo stesso per gli altri elementi
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
}