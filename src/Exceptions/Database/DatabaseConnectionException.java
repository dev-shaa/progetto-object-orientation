package Exceptions.Database;

/**
 * Eccezione per indicare che non è stato possibile stabilire una connessione al database.
 */
public class DatabaseConnectionException extends DatabaseException {
    public DatabaseConnectionException() {
        super();
    }

    public DatabaseConnectionException(String message) {
        super(message);
    }
}
