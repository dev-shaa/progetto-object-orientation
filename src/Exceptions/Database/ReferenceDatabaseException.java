package Exceptions.Database;

/**
 * Eccezione per indicare che qualcosa è andato storto nell'interazione con il database dei riferimenti.
 */
public class ReferenceDatabaseException extends Exception {
    public ReferenceDatabaseException() {
        super();
    }

    public ReferenceDatabaseException(String message) {
        super(message);
    }
}
