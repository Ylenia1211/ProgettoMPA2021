package controller.factorySidebar;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;

public class SecretariatSideBar implements SideBarAction{
    private final Button  add = new Button("Aggiungi");
    private final Button  profilo = new Button("Profilo");
    private final Button  pazienti = new Button("Pazienti");
    private final Button  clienti = new Button("Clienti");
    private final Button  prenotazioni = new Button("Prenotazioni");

    public Button notifica = new Button( "Notifica" );
    private final Button logout = new Button("Logout");
    @Override
    public List<Button> getSpecificAction() {
        final ImageView imageView = new ImageView(
                new Image("./notification.png")
        );
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        notifica = new Button("Notifica", imageView);
        return List.of(add,clienti,pazienti,prenotazioni,notifica, profilo,logout);//aggiungere qui nuovi comandi per segretaria
    }
}
