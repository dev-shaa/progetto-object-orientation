package Exceptions.Database;

/**
 * Eccezione per indicare che qualcosa è andato storto nell'interazione con il database degli autori.
 */
public class AuthorDatabaseException extends Exception {
    public AuthorDatabaseException() {
        super();
    }

    public AuthorDatabaseException(String message) {
        super(message);
    }
}
