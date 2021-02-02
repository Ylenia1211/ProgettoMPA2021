package controller.factorySidebar;

import javafx.scene.control.Button;

import java.util.List;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Interfaccia che dichiara un metodo per ritornare una lista di azioni specifiche.
 */
public interface SideBarAction {
    List<Button> getSpecificAction();
}
