package util.email;

import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Classe 'EmailProcessor' {@link EmailProcessor} che gestisce la catena l'handler per l'invio dell'email.
 */
public class EmailProcessor {

    private static Consumer<Communication> handler;

    public static Consumer<Communication> outlookHandler = email -> {
        if (email.getMyAccountEmail().endsWith("hotmail.it")) {
            ServiceClientMail.sendMail(email, "smtp.live.com");
            System.out.println("Invio email da un account outlook");
        }
    };

    public static Consumer<Communication> gMailHandler = email -> {
        if (email.getMyAccountEmail().endsWith("gmail.com")) {
            ServiceClientMail.sendMail(email, "smtp.gmail.com");
            System.out.println("Invio email da un account gmail");
        }
    };

    public static Consumer<Communication> initialHandler = email -> System.out.println("Prepazione invio email in corso...");

    /**
     * Processa gli handler per email e trova il gestore più appropriato per l'invio dell'email
     */
    @SafeVarargs
    public static void process(Communication email, Consumer<Communication>... handlers) {
        handler = Stream.of(handlers).reduce(initialHandler, Consumer::andThen);
        handler.accept(email);
    }

}
