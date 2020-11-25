package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Client;
import repository.ClientRepository;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ShowTableController  implements Initializable {
    public ObservableList<String> items;
    public ListView<String> listView;
    /*public TableView<String> tableClient;
    public TableColumn col_name;
    public TableColumn col_surname;
    public TableColumn col_address;
    public TableColumn col_email;
    public TableColumn col_id;
    public TableColumn col_city;
    public TableColumn col_tel;*/
    Object[] options = {"Si, cancella",
            "No",
          };
    private ClientRepository clientRepo = new ClientRepository();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {

            items = FXCollections.observableArrayList(clientRepo.findAll());
            listView.setItems(items);
            listView.setOnMouseClicked( e-> {
                JPanel pan = new JPanel();

                int ok = JOptionPane.showConfirmDialog(
                        null,
                        "Sei sicuro di voler cancellare?",
                        "Cancellazione Utente",
                        JOptionPane.YES_NO_OPTION);
                //System.out.println(ok);
                if(ok ==0){ //cancella
                   int index = listView.getSelectionModel().getSelectedIndex();
                   //clientRepo.deleteItem(index);
                   listView.getItems().remove(index); //elimina graficamente
                }
            });

            //tableClient.setItems(items);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}
