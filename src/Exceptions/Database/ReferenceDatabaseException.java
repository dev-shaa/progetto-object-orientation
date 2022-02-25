package Exceptions.Database;

/**
 * Eccezione per indicare che qualcosa è andato storto nell'interazione con il database dei riferimenti.
 */
public class ReferenceDatabaseException extends DatabaseException {

    public ReferenceDatabaseException() {
        super();
    }

    public ReferenceDatabaseException(String message) {
        super(message);
    }

    public ReferenceDatabaseException(Throwable cause) {
        super(cause);
    }

    public ReferenceDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReferenceDatabaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}