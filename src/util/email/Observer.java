package util.email;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Una classe può implementare l'interfaccia Observer quando vuole essere informata dei cambiamenti negli oggetti osservabili.
 */
public interface Observer {
    /**
     * Questo metodo viene chiamato ogni volta che l'oggetto osservato viene cambiato.
     */
    void update();
}
