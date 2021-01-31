package controller.factorySidebar;

import javafx.scene.control.Button;

import java.util.List;

public class DoctorSideBar implements SideBarAction {
    //bottoni per la sidebar del dottore
    private final Button agenda = new Button("Agenda");
    private final Button report = new Button("Report");
    private final Button profilo = new Button("Profilo");
    private final Button logout = new Button("Logout");


    @Override
    public List<Button> getSpecificAction() {
        return List.of(this.agenda,report,profilo, logout);  ////aggiungere qui nuovi comandi per dottore
    }
}
