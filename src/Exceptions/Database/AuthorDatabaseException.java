package Exceptions.Database;

/**
 * Eccezione per indicare che qualcosa è andato storto nell'interazione con il database degli autori.
 */
public class AuthorDatabaseException extends DatabaseException {

    public AuthorDatabaseException() {
        super();
    }

    public AuthorDatabaseException(String message) {
        super(message);
    }

    public AuthorDatabaseException(Throwable cause) {
        super(cause);
    }

    public AuthorDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthorDatabaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}