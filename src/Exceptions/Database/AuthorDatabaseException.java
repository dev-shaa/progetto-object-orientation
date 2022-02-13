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
}
