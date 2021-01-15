package controller;

import dao.ConcreteAppointmentDAO;
import datasource.ConnectionDBH2;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import model.Appointment;
import model.Owner;
import model.Pet;
import javafx.scene.paint.Color;
import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class ShowInfoOwnerPetController implements Initializable {

    public Label nameOwner;
    public Label surnameOwner;
    public Label sexOwner;
    public Label datebirthOwner;
    public Label addressOwner;
    public Label cityOwner;
    public Label codFiscalOwner;
    public Label emailOwner;
    public Label namePet;
    public Label surnamePet;
    public Label sexPet;
    public Label datebirthPet;
    public Label racePet;
    public Label particularSignPet;
    private final String idOwner;
    private final String idPet;
    public VBox vbox_main;
    private ConcreteAppointmentDAO appointmentRepo;
    private Appointment appointment;

    public ShowInfoOwnerPetController(Appointment appointment) {
        this.idOwner = appointment.getId_owner();
        this.idPet = appointment.getId_pet();
        this.appointment = appointment;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try{
            ConnectionDBH2 connection = new ConnectionDBH2();
            this.appointmentRepo = new ConcreteAppointmentDAO(connection);

            //ricerca di dei dati del owner
           Owner owner =  this.appointmentRepo.searchOwnerById(idOwner);
            if(owner!=null){
                setFieldDataOwner(owner);
            }else
            {
                JOptionPane.showMessageDialog(null,"Errore nel caricamento dei dati del Cliente");
            }

            //ricerca di dei dati del Pet
            Pet pet = this.appointmentRepo.searchPetById(idPet);
            if(pet!=null){
                setFieldDataPet(pet);
            }else
            {
                JOptionPane.showMessageDialog(null,"Errore nel caricamento dei dati del Paziente");
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/showInfoBooking.fxml"));
            loader.setController(new ShowInfoBooking(this.appointment));
            VBox mainPane = loader.load();
            vbox_main.getChildren().add(mainPane);

        }
        catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
        }
    }

    private void setFieldDataPet(Pet pet) {
        namePet.setText(pet.getName());
        surnamePet.setText(pet.getSurname());
        sexPet.setText(pet.getSex().toString());
        datebirthPet.setText(pet.getDatebirth().toString());
        racePet.setText(pet.getId_petRace());
        particularSignPet.setText(pet.getParticularSign());
    }

    private void setFieldDataOwner(Owner owner) {
        nameOwner.setText(owner.getName());
        surnameOwner.setText(owner.getSurname());
        sexOwner.setText(owner.getSex().toString());
        datebirthOwner.setText(owner.getDatebirth().toString());
        addressOwner.setText(owner.getAddress());
        cityOwner.setText(owner.getCity());
        codFiscalOwner.setText(owner.getFiscalCode());
        emailOwner.setText(owner.getEmail());
    }

    public String getIdOwner() {
        return idOwner;
    }

    public String getIdPet() {
        return idPet;
    }
}
