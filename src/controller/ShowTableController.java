package controller;

import dao.ConcreteOwnerDAO;
import datasource.ConnectionDBH2;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Owner;


import javax.swing.*;
import java.net.URL;

import java.sql.ResultSet;
import java.util.ResourceBundle;
public class ShowTableController  implements Initializable {

    public TableView<Owner> tableClient;
    public TableColumn<Owner, String> col_name;
    public TableColumn<Owner, String> col_surname;
    public TableColumn<Owner, String> col_address;
    public TableColumn<Owner, String> col_city;
    public TableColumn<Owner, String> col_tel;
    public TableColumn<Owner, String> col_email;
    private ConcreteOwnerDAO clientRepo;
    public ObservableList<Owner> listItems = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_surname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        col_address.setCellValueFactory(new PropertyValueFactory<>("address"));
        col_city.setCellValueFactory(new PropertyValueFactory<>("city"));
        col_tel.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        col_email.setCellValueFactory(new PropertyValueFactory<>("email"));

        try{
            ConnectionDBH2 connection = new ConnectionDBH2();
            clientRepo = new ConcreteOwnerDAO(connection);
            ResultSet r =  clientRepo.findAll();
            while(r.next()){
                listItems.add(new Owner(
                        /*r.getString("name"),
                        r.getString("surname"),
                        r.getString("address"),
                        r.getString("city"),
                        r.getString("telephone"),
                        r.getString("email")
                        */));
            }
            tableClient.setItems(listItems);
        }
        catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
        }
    }
}


/*  //OLD
public class ShowTableController  implements Initializable {
    public ObservableList<String> items;
    public ListView<String> listView;
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
}*/
