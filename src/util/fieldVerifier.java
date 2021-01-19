package util;

import javafx.scene.layout.VBox;

import java.awt.*;
import java.util.ArrayList;

public interface fieldVerifier {

    default ArrayList<Object> emptyFieldChecker(VBox form) {
        ArrayList<Object> emptyFields = new ArrayList<>();
        for (Object field: form.getChildren()){
            if (field instanceof TextField){
                TextField textField = (TextField) field;
                if (textField.getText() == null || textField.getText().trim().isEmpty())
                    emptyFields.add(field);
            }
            if (field instanceof TextArea){
                TextArea textField = (TextArea) field;
                if (textField.getText() == null || textField.getText().trim().isEmpty())
                    emptyFields.add(field);
            }
        }
        return emptyFields;
    }
    default boolean verifyPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("^(\\((00|\\+)39\\)|(00|\\+)39)?(38[890]|34[7-90]|36[680]|33[3-90]|32[89])" +
                "\\d{7}$");
    }

    default boolean verifyFiscalCode(String fiscalCode) {
        return fiscalCode.matches("^([A-Za-z]{6}[0-9lmnpqrstuvLMNPQRSTUV]{2}[abcdehlmprstABCDEHLMPRST]{1}[0-9" +
                "lmnpqrstuvLMNPQRSTUV]{2}[A-Za-z]{1}[0-9lmnpqrstuvLMNPQRSTUV]{3}[A-Za-z]{1})$|([0-9]{11})$");
    }

    default boolean verifyEmail(String email) {
        return email.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01" +
                "-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\"" +
                ")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-" +
                "4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9" +
                "-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c" +
                "\\x0e-\\x7f])+)\\])");
    }
}
