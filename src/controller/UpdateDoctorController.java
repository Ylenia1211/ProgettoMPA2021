package controller;

import model.Doctor;
import model.Gender;
import model.Owner;

import java.net.URL;
import java.util.ResourceBundle;

public class UpdateDoctorController extends RegistrationDoctorController{
    private final String id;
    private final Doctor doctor;
    public UpdateDoctorController(Doctor doctor) {
        super();
        this.doctor = doctor;
        this.id = super.getDoctorRepo().search(doctor);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        setParam(doctor);
    }

    public void setParam(Doctor data) {
        super.getTitle().setText("Modifica Dati Dottore");
        super.getTextName().setText(data.getName().trim());
        super.getTextSurname().setText(data.getSurname().trim());
        super.getTextAddress().setText(data.getAddress().trim());
        super.getTextCity().setText(data.getCity().trim());
        super.getTextTelephone().setText(data.getCity().trim());
        super.getTextEmail().setText(data.getEmail().trim());
        super.getTextFiscalCode().setText(data.getFiscalCode().trim());
        super.getTextdateBirth().setValue(data.getDatebirth());
        super.getSpecialization().setValue(data.getSpecialization().trim());
        super.getUsername().setText(data.getUsername().trim());
        super.getPassword().setText(data.getPassword().trim());
        if(data.getSex().compareTo(Gender.M) == 0){
            super.rbM.setSelected(true);
        }else{
            super.rbF.setSelected(true);
        }
    }

   @Override
    public void addActionButton() {
       this.getSaveBtn().setOnAction(e -> {
           Doctor d = createDoctor();
           super.getDoctorRepo().update(id, d);
           System.out.println("modifica qui");
       });
   }
}
