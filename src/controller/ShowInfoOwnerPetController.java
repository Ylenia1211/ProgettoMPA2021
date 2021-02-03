package controller;

import dao.ConcreteAppointmentDAO;
import datasource.ConnectionDBH2;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import model.Appointment;
import model.Doctor;
import model.Owner;
import model.Pet;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Implementando i metodi di 'Inizializable' {@link Initializable} inizializza la view associata al controller.
 * Mostra le informazioni relative a proprietario, paziente e medico legati alla stessa visita
 */
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
    public Label labelDottoreName;
    public Label labelDottoreSurname;
    private ConcreteAppointmentDAO appointmentRepo;
    private final Appointment appointment;

    /**
     * Costruttore della classe, setta i parametri {@link ShowInfoOwnerPetController#idOwner},
     * {@link ShowInfoOwnerPetController#idPet}, {@link ShowInfoOwnerPetController#appointment} e
     * {@link ShowInfoOwnerPetController#idDoctor} con l'oggetto di tipo {@link Appointment} passato a parametro
     *
     * @param appointment L'appuntamento specifico
     */
    public ShowInfoOwnerPetController(Appointment appointment) {
        this.idOwner = appointment.getId_owner();
        this.idPet = appointment.getId_pet();
        this.appointment = appointment;
        this.idDoctor = appointment.getId_doctor();
    }

    /**
     * Inizializza i campi della view in modo appropriato e
     * Inserisce i dati all'interno delle tabelle grazie alle funzioni
     * {@link ShowInfoOwnerPetController#setFieldDataOwner(Owner)},
     * {@link ShowInfoOwnerPetController#setFieldDataDoctor(Doctor)} e
     * {@link ShowInfoOwnerPetController#setFieldDataPet(Pet)}
     *
     * {@inheritDoc}
     */
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

    /**
     * Inserisce i dati del medico nelle label della tabella Dottore
     *
     * @param doctor L'oggetto {@link Doctor} da cui prendere i valori da settare nella tabella
     */
    private void setFieldDataDoctor(Doctor doctor) {
        labelDottoreName.setText(doctor.getName());
        labelDottoreSurname.setText(doctor.getSurname());
    }

    /**
     * Inserisce i dati del paziente nelle label della tabella Paziente
     *
     * @param pet L'oggetto {@link Pet} da cui prendere i valori da settare nella tabella
     */
    private void setFieldDataPet(Pet pet) {
        namePet.setText(pet.getName());
        surnamePet.setText(pet.getSurname());
        sexPet.setText(pet.getSex().toString());
        datebirthPet.setText(pet.getDatebirth().toString());
        racePet.setText(pet.getId_petRace());
        particularSignPet.setText(pet.getParticularSign());
        particularSignPet.setTooltip(new Tooltip(this.particularSignPet.getText()));
    }

    /**
     * Inserisce i dati del proprietario nelle label della tabella Cliente
     *
     * @param owner L'oggetto {@link Owner} da cui prendere i valori da settare nella tabella
     */
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

    /**
     * Getter dell'attributo {@link ShowInfoOwnerPetController#idOwner}
     *
     * @return Il campo {@link ShowInfoOwnerPetController#idOwner}
     */
    public String getIdOwner() {
        return idOwner;
    }

    /**
     * Getter dell'attributo {@link ShowInfoOwnerPetController#idPet}
     *
     * @return Il campo {@link ShowInfoOwnerPetController#idPet}
     */
    public String getIdPet() {
        return idPet;
    }
}
