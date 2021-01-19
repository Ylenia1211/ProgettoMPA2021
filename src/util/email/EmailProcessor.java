package util.email;

import java.util.function.Consumer;
import java.util.stream.Stream;

public class EmailProcessor {

    private static Consumer<Communication> handler;

    public static Consumer<Communication> outlookHandler = email -> {if (email.getMyAccountEmail().endsWith("hotmail.it")) {
        ServiceClientMail.sendMail(email,"smtp.live.com");
        System.out.println("Invio email da un account outlook");}};

    public static Consumer<Communication> gMailHandler = email -> {if (email.getMyAccountEmail().endsWith("gmail.com")) {
        ServiceClientMail.sendMail(email, "smtp.gmail.com");
        System.out.println("Invio email da un account gmail");}};

    public static Consumer<Communication> initialHandler = email ->   System.out.println("Prepazione invio email in corso...");

    public static void process (Communication email, Consumer<Communication>... handlers){
        handler = Stream.of(handlers).reduce(initialHandler,Consumer::andThen);
        handler.accept(email);
    }

}
