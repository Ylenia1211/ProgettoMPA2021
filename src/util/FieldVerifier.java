package util;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Interfaccia con metodi default che permette di verificare i dati inseriti dall'utente a livello di sintassi (un
 * indirizzo email ad esempio deve avere un pattern specifico), se i campi sono stati effettivamente compilati e ne
 * verifica la sicurezza
 */
public interface FieldVerifier {

    /**
     * Verifica il match con il pattern del codice fiscale tramite espressione regolare
     *
     * @param fiscalCode Il codice fiscale da verificare
     * @return true in caso di match, false altrimenti
     */
    default boolean fiscalCodeVerifier(String fiscalCode){
        return fiscalCode.matches("^([A-Za-z]{6}[0-9lmnpqrstuvLMNPQRSTUV]{2}[abcdehlmprstABCDEHLMPRST]{1" +
                "}[0-9lmnpqrstuvLMNPQRSTUV]{2}[A-Za-z]{1}[0-9lmnpqrstuvLMNPQRSTUV]{3}[A-Za-z]{1})$|([0-9]{11})$");
    }

    /**
     * Verifica il match con il pattern dell'email tramite espressione regolare
     *
     * @param email L'email da verificare
     * @return true in caso di match, false altrimenti
     */
    default boolean emailVerifier(String email){
        return email.matches("(^[a-zA-Z0-9_.+-]+@(?:(?:[a-zA-Z0-9-]+\\.)?[a-zA-Z]+\\.)?(gmail.com|hotmail.it|unipa.it|live.it|outlook.com)$)");
    }

    /**
     * Verifica il match con il pattern dei numeri di cellulare italiani, prefissi inclusi, tramite espressione regolare
     *
     * @param number Il numero di cellulare da verificare
     * @return true in caso di match, false altrimenti
     */
    default boolean phoneNumberVerifier(String number){
        return number.matches("^(\\((00|\\+)39\\)|(00|\\+)39)?(38[890]|35[01]|34[7-90]|36[680]|33[3-90]|32[0-9])\\d{7}$");
    }

    /**
     * Controlla se qualche campo ha il valore null
     *
     * @param fields Lo Stream di oggetti da controllare
     * @return true se trova qualche campo null, false altrimenti
     */
    default boolean checkNull(Stream<Object> fields) {
        return fields.anyMatch(Objects::isNull);
    }

    /**
     * Controlla se qualche campo non è stato compilato
     *
     * @param fields Lo Stream di oggetti da controllare
     * @return true se trova qualche campo vuoto, false altrimenti
     */
    default boolean checkEmptyTextField(Stream<TextField> fields) {
        return fields.anyMatch( x -> x.getText().isEmpty());
    }

    /** #Todo: da verificare
     * Controlla se lo stream di oggetti inseriti a parametro abbia il valore null
     *
     * @param fields Lo Stream di oggetti da controllare
     * @return true se trova qualche campo null, false altrimenti
     */
    default boolean checkEmptyComboBox(Stream<ComboBox<? extends Object>> fields) {
        return fields.anyMatch( x -> x.getValue()==null);
    }

    /**
     * Verifica se ci sono campi con controlli da effettuare su di essi
     *
     * @param fields Lo stream di campi da verificare
     * @return true se rileva dei campi da verificare, false altrimenti
     */
    default boolean checkAllFieldWithControlRestricted(Stream<TextField> fields) {
        return fields.anyMatch( x -> x.getStyle().contains("-fx-border-color: red"));
    }

    /**
     * Controlla se il campo inserito a paramtro ha un bordo rosso o arancione, che indica se il campo sia poco sicuro.
     * Usato per il controllo delle password.
     *
     * @param field Il campo da verificare
     * @return true se rileva che il valore inserito sia poco sicuro, false altrimenti
     */
    default boolean checkifNotSecurePassword(Label field) {
        return (field.getStyle().contains("-fx-text-fill: red") || field.getStyle().contains("-fx-text-fill: orange"));
    }

    /**
     * Verifica che il valore inserito nel campo di ricerca sia corretto
     *
     * @param values La Collection di valori con cui fare il confronto
     * @param text Il valore da ricercare all'interno della Collection
     * @return true se rileva che il valore inserito sia corretto, false altrimenti
     */
    default boolean checkSearchFieldIsCorrect(Collection<String> values, String text) {
        return  values.stream().anyMatch(option -> option.toUpperCase().contains(text.toUpperCase()));
    }

    /**
     * Assegna un listener ai campi passati a parametro, che permette di assegnare un bordo rosso ai campi errati dopo
     * averne effettuato il controllo
     *
     * @param fiscalCode Il codice fiscale del proprietario
     * @param telephone Il numero di telefono del proprietario
     * @param email L'email del proprietario
     */
    default void setListenerCriticalFields(TextField fiscalCode, TextField telephone, TextField email) {
        fiscalCode.textProperty().addListener((observableFC, oldValueFC, newValueFC) -> {
            if (!fiscalCodeVerifier(newValueFC))
                fiscalCode.setStyle("-fx-border-color: red; -fx-border-width: 2px");
            else
                fiscalCode.setStyle("-fx-border-color: lightgreen;  -fx-border-width: 2px");
        });
        telephone.textProperty().addListener((observableT, oldValueT, newValueT) -> {
            if (!this.phoneNumberVerifier(newValueT))
                telephone.setStyle("-fx-border-color: red;  -fx-border-width: 2px");
            else
                telephone.setStyle("-fx-border-color: lightgreen;  -fx-border-width: 2px");
        });
        email.textProperty().addListener((observableE, oldValueE, newValueE) -> {
            if (!this.emailVerifier(newValueE))
                email.setStyle("-fx-border-color: red;  -fx-border-width: 2px");
            else
                email.setStyle("-fx-border-color: lightgreen;  -fx-border-width: 2px");
        });
    }
}
