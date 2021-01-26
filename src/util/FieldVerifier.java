package util;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.Objects;
import java.util.stream.Stream;

public interface FieldVerifier {

    default boolean fiscalCodeVerifier(String fiscalCode){
        return fiscalCode.matches("^([A-Za-z]{6}[0-9lmnpqrstuvLMNPQRSTUV]{2}[abcdehlmprstABCDEHLMPRST]{1" +
                "}[0-9lmnpqrstuvLMNPQRSTUV]{2}[A-Za-z]{1}[0-9lmnpqrstuvLMNPQRSTUV]{3}[A-Za-z]{1})$|([0-9]{11})$");
    }
    default boolean emailVerifier(String email){
        return email.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[" +
                "\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-" +
                "\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(" +
                "?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|" +
                "[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\" +
                "\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
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
}
