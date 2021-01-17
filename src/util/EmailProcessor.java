package util;

import java.util.function.Consumer;
import java.util.stream.Stream;

public class EmailProcessor {

    private static Consumer<Communication> handler;

    public static Consumer<Communication> businessHandler = email -> {if (email.getMyAccountEmail().endsWith("hotmail.it")) {
        ServiceOutlookMail.sendMail(email);
        System.out.println("Invio email ad un account outlook");}};

    public static Consumer<Communication> gMailHandler = email -> {if (email.getMyAccountEmail().endsWith("gmail.com")) {
        ServiceGMail.sendMail(email);
        System.out.println("Invio email ad un account gmail");}};

    public static Consumer<Communication> initialHandler = email ->   System.out.println("Prepazione invio email in corso...");

    public static void process (Communication email, Consumer<Communication>... handlers){
        handler = Stream.of(handlers).reduce(initialHandler,Consumer::andThen);
        handler.accept(email);
    }

}
