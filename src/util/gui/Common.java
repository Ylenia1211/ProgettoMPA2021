package util.gui;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.time.Duration;
import java.time.LocalDate;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Classe utilizzata per richiamare metodi comuni a più classi, soprattutto per quanto riguarda l'aspetto grafico.
 *
 */
public interface Common {
    /**
     * Metodo che restrige in range di date selezionabili in un DataPicker.
     *
     * @param datePicker dataPicker su cui applicare la restrinzione dei giorni selezionabili.
     * @param minDate    data minima selezionabile.
     * @param maxDate    data massima selezionabile.
     */
    static void restrictDatePicker(DatePicker datePicker, LocalDate minDate, LocalDate maxDate) {
        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item.isBefore(minDate)) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        } else if (item.isAfter(maxDate)) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            }
        };
        datePicker.setDayCellFactory(dayCellFactory);
    }

    /**
     * Metodo che rende leggibile un orario di tipo 'Duration' {@link Duration}.
     *
     * @param duration oggetto di tipo Duration' {@link Duration}.
     */
    static String humanReadableFormat(Duration duration) {
        return duration.toString()
                .substring(2)
                .replaceAll("(\\d[HMS])(?!$)", "$1 ")
                .toLowerCase();
    }

    /**
     * Metodo per la creazione di un mini Dialog  {@link Dialog} per l'inserimento di Email e password.
     *
     * @param title titolo assegnato a una Label.
     * @param text  campo di tipo 'TextField' {@link TextField} dove inserire l'email.
     * @param psw   campo di tipo 'PasswordField' {@link PasswordField} dove inserire la password.
     *
     */
    static Dialog<VBox> createDialogText(Label title, TextField text, PasswordField psw) {
        VBox container = new VBox();
        container.getChildren().add(title);
        container.getChildren().add(text);
        container.getChildren().add(new Label("Inserisci Password Email"));
        container.getChildren().add(psw);
        Dialog<VBox> dialog = new Dialog<>();
        dialog.getDialogPane().setPrefSize(300, 100);
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(container);
        return dialog;
    }
    /**
     * Metodo per la visualizzazione di un Alert di tipo Information per indicare una "ricerca vuota".
     *
     */
    default void searchEmpty() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Ricerca Vuota");
        alert.setContentText("Nessun Utente trovato con queste credenziali! Riprova.");
        alert.showAndWait();
    }
}
