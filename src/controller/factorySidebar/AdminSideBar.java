package controller.factorySidebar;

import javafx.scene.control.Button;

import java.util.List;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Gestisce la sidebar dell'Amministratore
 */
public class AdminSideBar implements SideBarAction{
    private final Button aggiungi = new Button("Aggiungi");
    private final Button dottori = new Button("Dottori");
    private final Button segreteria = new Button("Segreteria");
    private final Button clinica = new Button("Clinica");
    private final Button logout = new Button("Logout");

    /**
     * Funzione che ritorna una lista con i comandi da inserire nella sidebar dell'Amministratore
     *
     * @return Un oggetto di tipo {@link List<Button>} con i bottoni per i comandi
     */
    @Override
    public List<Button> getSpecificAction() {
        return List.of(aggiungi,dottori, segreteria, clinica,logout);
    } //aggiungere qui nuovi comandi per l'ammistratore
}
