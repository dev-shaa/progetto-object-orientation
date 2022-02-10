package Exceptions.Database;

/**
 * Eccezione per indicare che qualcosa è andato storto nell'interazione con il database degli utenti.
 */
public class UserDatabaseException extends Exception {

    public UserDatabaseException() {
        super();
    }

    public UserDatabaseException(String message) {
        super(message);
    }

}
