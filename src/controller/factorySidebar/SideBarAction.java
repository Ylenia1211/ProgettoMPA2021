package controller.factorySidebar;

import dao.Crud;
import javafx.scene.control.Button;

import java.util.List;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Interfaccia Data Access Object per i tipi di oggetto {@link AdminSideBar}, {@link DoctorSideBar} e
 * {@link SecretariatSideBar}.
 * Dichiara un metodo per ritornare una lista di azioni.
 */
public interface SideBarAction {
    List<Button> getSpecificAction();
}
