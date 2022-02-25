package Exceptions.Database;

/**
 * Eccezione per indicare che qualcosa è andato storto nell'interazione con il database delle parole chiave.
 */
public class TagDatabaseException extends DatabaseException {

    public TagDatabaseException() {
        super();
    }

    public TagDatabaseException(String message) {
        super(message);
    }

    public TagDatabaseException(Throwable cause) {
        super(cause);
    }

    public TagDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public TagDatabaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}