package controller.factorySidebar;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Classe che implementa i metodi dell'interfaccia 'SideBarAction': {@link SideBarAction}.
 * Gestisce la sidebar della Segreteria
 */
public class SecretariatSideBar implements SideBarAction {
    private final Button add = new Button("Aggiungi");
    private final Button profilo = new Button("Profilo");
    private final Button pazienti = new Button("Pazienti");
    private final Button clienti = new Button("Clienti");
    private final Button prenotazioni = new Button("Prenotazioni");
    public Button notifica = new Button("Notifica");
    private final Button logout = new Button("Logout");

    /**
     * Funzione che ritorna una lista con i comandi da inserire nella sidebar della segreteria
     *
     * @return Un oggetto di tipo {@link List<Button>} con i bottoni per i comandi
     */
    @Override
    public List<Button> getSpecificAction() {
        final ImageView imageView = new ImageView(
                new Image("./notification.png")
        );
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        notifica = new Button("Notifica", imageView);
        return List.of(add, clienti, pazienti, prenotazioni, notifica, profilo, logout);//aggiungere qui nuovi comandi per segretaria
    }
}
