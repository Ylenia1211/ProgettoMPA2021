package controller;
import dao.ConcreteDoctorDAO;
import dao.ConcreteSecretariatDAO;
import datasource.ConnectionDBH2;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import model.Doctor;
import model.Gender;
import model.Secretariat;
import util.FieldVerifier;
import util.SessionUser;
import javax.swing.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PersonalProfileController implements Initializable , FieldVerifier{
    public ImageView picProfile;
    public CheckBox checkboxUpdate;
    public TextField name;
    public TextField surname;
    public HBox gender;
    public RadioButton rbM;
    public RadioButton rbF;
    public DatePicker birthDate;
    public TextField city;
    public TextField address;
    public TextField telephone;
    public TextField fiscalCode;
    public TextField email;
    public VBox labelsFields;
    public ToggleGroup genderGroup;
    public PasswordField password;
    private ComboBox<String> specialization;
    public TextField username;
    private final ConcreteSecretariatDAO secretariatRepo;
    private final ConcreteDoctorDAO doctorRepo;
    private Button saveBtn;
    private Button saveBtnPwd;
    private Doctor doctor;
    private String id_doctor = "";
    private String id_secretariat = "";
    private Secretariat secretariat;
    public double MAX_SIZE = 1.7976931348623157E308;
    public Button ok;
    private List<TextField> fieldsControlRestrict;

    public PersonalProfileController(String roleUserLogged)  {
        this.secretariatRepo = new ConcreteSecretariatDAO(ConnectionDBH2.getInstance());
        this.doctorRepo = new ConcreteDoctorDAO(ConnectionDBH2.getInstance());

        switch (roleUserLogged) {
            case "Dottore" -> {
                this.id_doctor = this.doctorRepo.search(SessionUser.getDoctor());
                this.doctor = this.doctorRepo.searchById(this.id_doctor);
            }
            case "Segreteria" -> {
                this.id_secretariat = this.secretariatRepo.search(SessionUser.getSecretariat());
                this.secretariat = this.secretariatRepo.searchById(this.id_secretariat);
            }
            default -> throw new IllegalStateException("Unexpected value");
        }

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (doctor != null) {
            addFieldSpecialization(doctor.getSpecialization());
            setParamDoctor(this.doctor);
        } else if (secretariat != null) {
            setParamSecretariat(this.secretariat);
        }
        this.password.setDisable(true);
        addButtonChangePassword();
        addButtonSave();

        setListenerCriticalFields(this.fiscalCode, this.telephone, this.email);
        this.fieldsControlRestrict = List.of(fiscalCode,
               telephone,
               email);

        this.checkboxUpdate.setOnAction(actionEvent -> {
            if (!checkboxUpdate.isSelected()) {
                this.saveBtnPwd.setDisable(true);
                this.saveBtn.setDisable(true);
                this.name.setEditable(false);
                this.surname.setEditable(false);
                this.address.setEditable(false);
                this.city.setEditable(false);
                this.telephone.setEditable(false);
                this.email.setEditable(false);
                this.fiscalCode.setEditable(false);
                this.birthDate.setDisable(true);
                this.rbM.setDisable(true);
                this.rbF.setDisable(true);
                this.username.setEditable(false);
                this.password.setEditable(false);

                if (doctor != null) {
                    this.specialization.setDisable(true);
                }
            } else {
                this.saveBtnPwd.setDisable(false);
                this.saveBtn.setDisable(false);
                this.name.setEditable(true);
                this.surname.setEditable(true);
                this.address.setEditable(true);
                this.city.setEditable(true);
                this.telephone.setEditable(true);
                this.email.setEditable(true);
                this.fiscalCode.setEditable(true);
                this.birthDate.setDisable(false);
                this.rbM.setDisable(false);
                this.rbF.setDisable(false);
                if (doctor != null) {
                    this.specialization.setDisable(false);
                }
                this.username.setEditable(true);
                this.password.setEditable(false);


            }
        });
    }

    public void updateProfile(ActionEvent actionEvent) {
       if(!checkAllFieldWithControlRestricted(this.fieldsControlRestrict.stream())) {
           if (doctor != null) {
               Doctor d = createDoctor();
               if (this.doctorRepo.isNotDuplicate(d)) {
                   try {
                       this.doctorRepo.update(this.id_doctor, d);
                       SessionUser.updateProfile(d);
                   } catch (Exception ex) {
                       ex.printStackTrace();
                   }
               } else {
                   JOptionPane.showMessageDialog(null, "Impossibile creare il dottore! Già esistente!");
               }
           } else if (secretariat != null) {
               Secretariat s = createSecretariat();
               if (this.secretariatRepo.isNotDuplicate(s)) {
                   try {
                       this.secretariatRepo.update(this.id_secretariat, s);
                       SessionUser.updateProfile(s);
                   } catch (Exception ex) {
                       ex.printStackTrace();
                   }
               } else {
                   JOptionPane.showMessageDialog(null, "Impossibile creare l'utente segreteria! Già esistente!");
               }
           }
       }else
       {
           JOptionPane.showMessageDialog(null, "Per completare la registrazione devi completare TUTTI i campi correttamente!");
       }
    }


    public void setParamDoctor(Doctor data) {
        this.name.setText(data.getName().trim());
        this.surname.setText(data.getSurname().trim());
        this.address.setText(data.getAddress().trim());
        this.city.setText(data.getCity().trim());
        this.telephone.setText(data.getTelephone().trim());
        this.email.setText(data.getEmail().trim());
        this.fiscalCode.setText(data.getFiscalCode().trim());
        this.birthDate.setValue(data.getDatebirth());
        if (data.getSex().compareTo(Gender.M) == 0) {
            this.rbM.setSelected(true);
        } else {
            this.rbF.setSelected(true);
        }
        this.username.setText(data.getUsername().trim());
        this.password.setText(data.getPassword().trim());
    }

    public void setParamSecretariat(Secretariat data) {
        this.name.setText(data.getName().trim());
        this.surname.setText(data.getSurname().trim());
        this.address.setText(data.getAddress().trim());
        this.city.setText(data.getCity().trim());
        this.telephone.setText(data.getTelephone().trim());
        this.email.setText(data.getEmail().trim());
        this.fiscalCode.setText(data.getFiscalCode().trim());
        this.birthDate.setValue(data.getDatebirth());
        if (data.getSex().compareTo(Gender.M) == 0) {
            this.rbM.setSelected(true);
        } else {
            this.rbF.setSelected(true);
        }
        this.username.setText(data.getUsername().trim());
        this.password.setText(data.getPassword().trim());
    }

    public void addFieldSpecialization(String specialization) {
        this.specialization = new ComboBox<>();
        this.specialization.setId("specialization");
        this.specialization.setPromptText("Scegli specializzazione");
        this.specialization.setTooltip(new Tooltip("Specializzazione in un tipo di visita"));
        this.specialization.setMaxWidth(MAX_SIZE);
        this.specialization.setPrefWidth(Region.USE_COMPUTED_SIZE);
        List<String> listSpecialization = this.doctorRepo.searchAllSpecialization();
        this.specialization = new ComboBox<>(FXCollections
                .observableArrayList(listSpecialization));
        this.specialization.setValue(specialization);
        this.specialization.setDisable(true);
        this.labelsFields.getChildren().add(this.specialization);
    }

    public Doctor createDoctor() {
        RadioButton chk = (RadioButton) this.genderGroup.getSelectedToggle();
        return new Doctor.Builder<>()
                .addName(this.name.getText().toUpperCase())
                .addSurname(this.surname.getText().toUpperCase())
                .addSex((chk.getText().equals("M") ? Gender.M : Gender.F)) //toString()
                .addDateBirth(this.birthDate.getValue())
                .addAddress(this.address.getText().toUpperCase())
                .addCity(this.city.getText().toUpperCase())
                .addTelephone(this.telephone.getText())
                .addEmail(this.email.getText())
                .addFiscalCode(this.fiscalCode.getText().toUpperCase())
                .addSpecialization(this.specialization.getValue().toUpperCase())
                .addUsername(this.username.getText())
                .addPassword(this.password.getText())
                .build();
    }

    public Secretariat createSecretariat() {
        RadioButton chk = (RadioButton) this.genderGroup.getSelectedToggle();
        return new Secretariat.Builder<>()
                .addName(this.name.getText().toUpperCase())
                .addSurname(this.surname.getText().toUpperCase())
                .addSex((chk.getText().equals("M") ? Gender.M : Gender.F)) //toString()
                .addDateBirth(this.birthDate.getValue())
                .addAddress(this.address.getText().toUpperCase())
                .addCity(this.city.getText().toUpperCase())
                .addTelephone(this.telephone.getText())
                .addEmail(this.email.getText())
                .addFiscalCode(this.fiscalCode.getText().toUpperCase())
                .addUsername(this.username.getText())
                .addPassword(this.password.getText())
                .build();
    }

    public void addButtonSave() {
        this.saveBtn = new Button("Salva");
        this.saveBtn.setId("saveBtn");
        this.saveBtn.setDisable(true);
        this.saveBtn.setMaxWidth(MAX_SIZE); //MAX_SIZE
        this.saveBtn.setPrefWidth(Region.USE_COMPUTED_SIZE);
        this.saveBtn.setPrefHeight(30);
        this.saveBtn.setStyle("-fx-background-color: #3DA4E3;-fx-text-fill: white;" +
                " -fx-border-color: transparent; -fx-font-size: 16px; ");
        this.saveBtn.setOnAction(this::updateProfile);
        this.labelsFields.getChildren().add(this.saveBtn);
    }


    public void addButtonChangePassword() {
        this.saveBtnPwd = new Button("Cambia Password");
        this.saveBtnPwd.setId("saveBtnPassword");
        this.saveBtnPwd.setDisable(true);
        this.saveBtnPwd.setMaxWidth(MAX_SIZE); //MAX_SIZE
        this.saveBtnPwd.setPrefWidth(Region.USE_COMPUTED_SIZE);
        this.saveBtnPwd.setPrefHeight(30);
        this.saveBtnPwd.setStyle("-fx-background-color: #3DA4E3;-fx-text-fill: white;" +
                " -fx-border-color: transparent; -fx-font-size: 16px; ");
        this.saveBtnPwd.setOnAction(this::changePassword);
        this.labelsFields.getChildren().add(this.saveBtnPwd);
    }

    private Dialog<VBox> createDialog(Label oldPasswordTitle, PasswordField password) {
        VBox container = new VBox();
        container.getChildren().add(oldPasswordTitle);
        container.getChildren().add(password);
        Dialog<VBox> dialog = new Dialog<>();
        dialog.getDialogPane().setPrefSize(300, 100);
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(container);
        return dialog;
    }


    private void changePassword(ActionEvent actionEvent) {
        Label oldPasswordTitle = new Label("Inserisci vecchia password");
        Label newPasswordTitle = new Label("Inserisci Nuova password");
        this.password = new PasswordField();

        Dialog<?> dialog1 = createDialog(oldPasswordTitle, this.password);
        var result = dialog1.showAndWait();
        if (result.isPresent()) {
            if (result.get() == dialog1.getDialogPane().getButtonTypes().get(0)) {
                if (doctor != null) {
                    if (password.getText().equals(this.doctor.getPassword())) {
                        PasswordField newPassword = new PasswordField();
                        Dialog<?> dialog2 = createDialog(newPasswordTitle, newPassword);
                        var result2 = dialog2.showAndWait();
                        if (result2.get() == dialog2.getDialogPane().getButtonTypes().get(0)) {
                            //cambia password in db
                            if ( !(newPassword.getText().length() >= 6 &&newPassword.getText().matches(".*\\d.*"))) {
                                JOptionPane.showMessageDialog(null, "La password non è sicura: immetti una password lunga almeno 6 caratteri che contenga almeno una cifra");
                            }else {
                                doctorRepo.updatePassword(this.id_doctor, newPassword.getText());
                                SessionUser.updateProfile(this.doctor);
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Password Errata! Riprova!");
                    }
                } else if (secretariat != null) {
                    if (password.getText().equals(this.secretariat.getPassword())) {
                        PasswordField newPassword = new PasswordField();
                        Dialog<?> dialog2 = createDialog(newPasswordTitle, newPassword);
                        var result2 = dialog2.showAndWait();
                        if (result2.get() == dialog2.getDialogPane().getButtonTypes().get(0)) {
                            //cambia password in db
                            if ( !(newPassword.getText().length() >= 6 &&newPassword.getText().matches(".*\\d.*"))) {
                                JOptionPane.showMessageDialog(null, "La password non è sicura: immetti una password lunga almeno 6 caratteri che contenga almeno una cifra");
                            }else{
                                secretariatRepo.updatePassword(this.id_secretariat, newPassword.getText());
                                SessionUser.updateProfile(this.secretariat);
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Password Errata! Riprova!");
                    }
                }


            }

        }
    }



}

//fare controllo su ruolo
        /*
        if (doctor != null) {
            if (password.getText().equals(this.doctor.getPassword())) {
                miniWindow.getChildren().clear();
                password.clear();
                ok = JOptionPane.showConfirmDialog(null, miniWindow, "Inserisci nuova password ", JOptionPane.OK_CANCEL_OPTION);
                if (ok == JOptionPane.OK_OPTION) {
                    System.out.println(password.getText());
                }
            } else {
                JOptionPane.showMessageDialog(null, "Errore! Password non corretta...");
            }
        }
}*/


