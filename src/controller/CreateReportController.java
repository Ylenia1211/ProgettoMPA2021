package controller;

import dao.ConcreteAppointmentDAO;
import datasource.ConnectionDBH2;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import model.Appointment;
import model.Owner;
import model.Pet;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

//ha lo stesso file fxml di ShowInfoOwnerPetController
public class CreateReportController  implements Initializable {

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

    public CreateReportController(Appointment appointment) {
        this.appointment = appointment;
        this.idOwner = appointment.getId_owner();
        this.idPet = appointment.getId_pet();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            //ConnectionDBH2 connection = new ConnectionDBH2();
            this.appointmentRepo = new ConcreteAppointmentDAO(ConnectionDBH2.getInstance());

            //ricerca di dei dati del owner
            Owner owner = this.appointmentRepo.searchOwnerById(idOwner);
            if (owner != null) {
                setFieldDataOwner(owner);
            } else {
                JOptionPane.showMessageDialog(null, "Errore nel caricamento dei dati del Cliente");
            }

            //ricerca di dei dati del Pet
            Pet pet = this.appointmentRepo.searchPetById(idPet);
            if (pet != null) {
                setFieldDataPet(pet);
            } else {
                JOptionPane.showMessageDialog(null, "Errore nel caricamento dei dati del Paziente");
            }

            //view estesa con un'altra
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/reportView.fxml"));
            loader.setControllerFactory(p -> new ReportViewController(appointment));
            //loader.setController(new ReportViewController());
            VBox mainPane = loader.load();
            vbox_main.getChildren().add(mainPane);

        } catch (Exception e) {
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
