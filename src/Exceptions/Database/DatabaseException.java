package Exceptions.Database;

/**
 * Eccezione per indicare che qualcosa è andato storto nell'interazione con il database.
 */
public class DatabaseException extends Exception {
    public DatabaseException() {
        super();
    }

    public DatabaseException(String message) {
        super(message);
    }
}
