package model;

import util.ServiceMail;

public class ConcreteObserver implements Observer {
    //aggiungere costruttore di concrete observer con campi:
    // l'indirizzo email del owner associato
    // data, ora inizio, ora fine (prevista) della visita
    @Override
    public void update() {
        ServiceMail.sendMail("provampa3@gmail.com"); //passare i parametri qui //immettere l'email a cui inviare il messaggio
        System.out.println("mando email: password cambiata!" );
    }
}
