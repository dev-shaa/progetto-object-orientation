package Exceptions.Database;

/**
 * Eccezione per indicare che qualcosa è andato storto nell'interazione con il database degli utenti.
 */
public class UserDatabaseException extends DatabaseException {

    public UserDatabaseException() {
        super();
    }

    public UserDatabaseException(String message) {
        super(message);
    }

    public UserDatabaseException(Throwable cause) {
        super(cause);
    }

    public UserDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserDatabaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}