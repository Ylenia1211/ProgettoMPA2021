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

    default boolean fiscalCodeVerifier(String fiscalCode){
        return fiscalCode.matches("^([A-Za-z]{6}[0-9lmnpqrstuvLMNPQRSTUV]{2}[abcdehlmprstABCDEHLMPRST]{1" +
                "}[0-9lmnpqrstuvLMNPQRSTUV]{2}[A-Za-z]{1}[0-9lmnpqrstuvLMNPQRSTUV]{3}[A-Za-z]{1})$|([0-9]{11})$");
    }

    default boolean emailVerifier(String email){
        return email.matches("(^[a-zA-Z0-9_.+-]+@(?:(?:[a-zA-Z0-9-]+\\.)?[a-zA-Z]+\\.)?(gmail.com|hotmail.it|unipa.it|live.it|outlook.com)$)");
    }

    default boolean phoneNumberVerifier(String number){
        return number.matches("^(\\((00|\\+)39\\)|(00|\\+)39)?(38[890]|35[01]|34[7-90]|36[680]|33[3-90]|32[0-9])\\d{7}$");
    }

    default boolean checkNull(Stream<Object> fields) {
        return fields.anyMatch(Objects::isNull);
    }

    default boolean checkEmptyTextField(Stream<TextField> fields) {
        return fields.anyMatch( x -> x.getText().isEmpty());
    }

    default boolean checkEmptyComboBox(Stream<ComboBox<? extends Object>> fields) {
        return fields.anyMatch( x -> x.getValue()==null);
    }

    /* controlla che i campi telefono email codice fiscale siano idonei */
    default boolean checkAllFieldWithControlRestricted(Stream<TextField> fields) {
        return fields.anyMatch( x -> x.getStyle().contains("-fx-border-color: red"));
    }

    default boolean checkifNotSecurePassword(Label field) {
        return (field.getStyle().contains("-fx-text-fill: red") || field.getStyle().contains("-fx-text-fill: orange"));
    }

    default boolean checkSearchFieldIsCorrect(Collection<String> values, String text) {
        return  values.stream().anyMatch(option -> option.toUpperCase().contains(text.toUpperCase()));
    }

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
