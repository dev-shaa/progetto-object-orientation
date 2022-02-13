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
}
