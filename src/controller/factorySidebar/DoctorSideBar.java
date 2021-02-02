package controller.factorySidebar;

import javafx.scene.control.Button;

import java.util.List;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Classe che implementa i metodi dell'interfaccia 'SideBarAction': {@link SideBarAction}.
 * Gestisce la sidebar del Dottore
 */
public class DoctorSideBar implements SideBarAction {
    //bottoni per la sidebar del dottore
    private final Button agenda = new Button("Agenda");
    private final Button report = new Button("Report");
    private final Button profilo = new Button("Profilo");
    private final Button logout = new Button("Logout");

    /**
     * Funzione che ritorna una lista con i comandi da inserire nella sidebar del dottore
     *
     * @return Un oggetto di tipo {@link List<Button>} con i bottoni per i comandi
     */
    @Override
    public List<Button> getSpecificAction() {
        return List.of(this.agenda, report, profilo, logout);
    }
}
