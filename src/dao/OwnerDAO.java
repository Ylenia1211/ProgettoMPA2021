package dao;
import model.Owner;
/**
 *
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 *
 * Interfaccia Data Access Object per il tipo di oggetto {@link Owner} estende l'interfaccia generica {@link Crud}.
 * Dichiara i metodi specifici per un oggetto di tipo Owner {@link Owner}.
 *
 */
public interface OwnerDAO extends Crud<Owner>{
    String search(Owner client);
    boolean isNotDuplicate(Owner p); //fa controlli più stretti rispetto al search
}
