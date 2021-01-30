package controller;

import dao.ConcreteAppointmentDAO;
import datasource.ConnectionDBH2;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import model.Appointment;
import model.Doctor;
import model.Owner;
import model.Pet;
import javafx.scene.paint.Color;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
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
    private final String idDoctor;
    public VBox vbox_main;
    public VBox vboxLabel;

    public Label labelDottoreName;
    public Label labelDottoreSurname;
    public Label labelDottoreSpecialization;
    private ConcreteAppointmentDAO appointmentRepo;
    private Appointment appointment;

    public ShowInfoOwnerPetController(Appointment appointment) {
        this.idOwner = appointment.getId_owner();
        this.idPet = appointment.getId_pet();
        this.appointment = appointment;
        this.idDoctor = appointment.getId_doctor();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

            this.appointmentRepo = new ConcreteAppointmentDAO(ConnectionDBH2.getInstance());
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

            Doctor doctor = this.appointmentRepo.searchDoctorById(idDoctor);
            if(doctor!=null){
                setFieldDataDoctor(doctor);
            }else
            {
                JOptionPane.showMessageDialog(null,"Errore nel caricamento dei dati del Dottore");
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/showInfoBooking.fxml"));
            loader.setController(new ShowInfoBooking(this.appointment));
            VBox mainPane = null;
            try {
                mainPane = loader.load();
            } catch (IOException ioException) {
                ioException.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error" + ioException.getMessage());
            }
            vbox_main.getChildren().add(mainPane);
        }

    private void setFieldDataDoctor(Doctor doctor) {

        labelDottoreName.setText(doctor.getName());
        labelDottoreSurname.setText(doctor.getSurname());
//        labelDottoreSpecialization.setText("Tipo visita: " + doctor.getSpecialization());
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
        this.nameOwner.setText(owner.getName());
        this.surnameOwner.setText(owner.getSurname());
        this.sexOwner.setText(owner.getSex().toString());
        this.datebirthOwner.setText(owner.getDatebirth().toString());
        this.addressOwner.setText(owner.getAddress());
        this.addressOwner.setTooltip(new Tooltip(this.addressOwner.getText()));
        this.cityOwner.setText(owner.getCity());
        this.cityOwner.setTooltip(new Tooltip(this.cityOwner.getText()));
        this.codFiscalOwner.setText(owner.getFiscalCode());
        this.emailOwner.setText(owner.getEmail());
        this.emailOwner.setTooltip(new Tooltip(this.emailOwner.getText()));
    }

    public String getIdOwner() {
        return idOwner;
    }

    public String getIdPet() {
        return idPet;
    }
}
