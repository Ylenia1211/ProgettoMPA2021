package util.email;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Questa classe rappresenta un oggetto osservabile  nel paradigma model-view.
 * Può essere richiamata per rappresentare un oggetto che l'applicazione vuole far osservare.
 */

public interface Subject {
    /**
     * Aggiunge un osservatore all'insieme degli osservatori per questo oggetto
     */
    void register(Observer o);

    /**
     * Elimina un osservatore dall'insieme degli osservatori di questo oggetto.
     */
    void unregister(Observer o);

    /**
     * notifica tutti i suoi osservatori
     */
    void notifyObservers();
}
